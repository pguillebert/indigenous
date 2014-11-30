(defproject indigenous "0.1.0-SNAPSHOT"
  :description "A Clojure library to manage native libraries in a sane way."
  :url "https://github.com/pguillebert/indigenous"
  :scm {:name "git"
        :url "https://github.com/pguillebert/indigenous"}
  :pom-addition [:developers
                 [:developer
                  [:name "Gary Verhaegen"]
                  [:url "https://github.com/gaverhae"]
                  [:email "gary.verhaegen@gmail.com"]
                  [:timezone "+1"]]
                 [:developer
                  [:name "Philippe Guillebert"]
                  [:url "https://github.com/pguillebert"]
                  [:timezone "+1"]]]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.0"]]
  :java-source-paths ["src"])
