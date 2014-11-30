(ns indigenous.core
  (:require [clojure.tools.logging :as log]
            [clojure.java.io :as io])
  (:import [indigenous PrivateLoader]))

;; Copyright Gary Verhaegen.
(defn os
  "Returns a string representing the current operating system, one of win,
  linux, or mac."
  []
  (let [os-arch (. (System/getProperty "os.arch") toLowerCase)
        os-name (. (System/getProperty "os.name") toLowerCase)]
    (cond (.startsWith os-name "win")    "win"
          (.startsWith os-name "mac")    "mac"
          (.startsWith os-name "linux")  "linux"
          :else
          (throw (UnsupportedOperationException.
                   (str "Unsupported platform: " os-name ", " os-arch))))))


;; Copyright Gary Verhaegen.
(defn arch
  "Returns a string representing the current architecture, one of x86 or
  x86_64."
  []
  (let [os-arch (. (System/getProperty "os.arch") toLowerCase)
        os-name (. (System/getProperty "os.name") toLowerCase)]
    (condp = os-arch
      "x86"    "x86"
      "i386"   "x86"
      "i686"   "x86"
      "x86_64" "x86_64"
      "amd64"  "x86_64"
      (throw (UnsupportedOperationException.
               (str "Unsupported platform: " os-name ", " os-arch))))))

(defn- os-temp-dir
  "Returns an OS-specific temp dir. Mostly needed because java.io.tmpdir yields
  nondeterministic results on Mac OS X, which, combined with the rather rigid
  way in which Mac OS X resolves dynamic libraries, makes it unreliable."
  [lib-name]
  (io/file (str (cond (= "mac" (os)) "/tmp/"
                      :else          (System/getProperty "java.io.tmpdir"))
                "/" lib-name "/")))


;; Copyright Gary Verhaegen.
(defn os-name
  "Returns an os-specific name for the given library name. For example, zmq
  will become libzmq.dylib on OS X."
  [lib-name]
  (condp = (os)
    "mac"   (str "lib" lib-name ".dylib")
    "linux" (str "lib" lib-name ".so")
    "win"   (str lib-name ".dll")))

(defn- save-library
  [lib-name lib-path]
  (let [tmp-dir  (os-temp-dir lib-name)
        tmp-path (-> (str tmp-dir "/" (os-name lib-name)) io/file)]
    (if (not (.exists tmp-dir)) (.mkdirs tmp-dir))
    (if (not (.exists tmp-path))
      (with-open [in  (-> lib-path io/resource io/input-stream)
                  out (-> tmp-path io/output-stream)]
        (io/copy in out)
        (log/info (str "Saved lib to: " tmp-path)))
      (with-open [in  (-> lib-path io/resource io/input-stream)
                  out (-> tmp-path io/input-stream)]
        (let [to-byte-seq (fn [is] (let [os (java.io.ByteArrayOutputStream.)]
                                     (io/copy is os)
                                     (-> os .toByteArray seq)))]
          (if (not (= (to-byte-seq in)
                      (to-byte-seq out)))
            (throw
              (IllegalStateException.
                (str "File "
                     tmp-path
                     " already exists but has different content.")))
            (log/info (str "Lib was already there: " tmp-path))))))
    (.getAbsolutePath tmp-path)))

(defn load-library
  "Loads the given file as a native library. The file must be in the native
  folder in the CLASSPATH."
  [lib-name lib-path]
  (let [file_path (save-library lib-name lib-path)]
    (try (PrivateLoader/load file_path)
      (catch java.io.IOException e
        (log/error e (str "Could not load native file: " lib-path))
        (throw e)))))
