# indigenous

> "just take the uberjar and go"

A Clojure library to manage _native libraries_ in a sane way.

**Indigenous** provides helpers to allow java or clojure code
that depends on native libraries to provide these libraries
inside the JAR and not with the dreaded java.library.path.

This way, you can provide native code for several platforms
and ship it _inside_ an uberjar, and link it when needed
(usually when you start your application).
No need to setup and install external binaries on your target host,
no need to manage JVM's java.library.path.

Most of the core is borrowed from gaverhae/naughtmq, by Gary Verhaegen.
I kept attribution of many functions to Gary in the comments of the code.

## Version

        [indigenous "0.1.0"]

        (require [indigenous.core :as indi])

## Usage

see my lib [Cauchy](http://github.com/pguillebert/cauchy) to see some
example usage.

TODO : write examples when I figure out a clear interface.


## Thanks

Gary Verhaegen for writing most of the original code and allowing me
to use it.


## License

Copyright © 2014 Philippe Guillebert, some parts Gary Verhaegen.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
