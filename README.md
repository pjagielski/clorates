# clorates

A Clojure REST API for [DevRates.com](http://devrates.com)

[![Build Status](https://travis-ci.org/pjagielski/clorates.png)](https://travis-ci.org/pjagielski/clorates)


## Usage

```lein run```

Visit [http://localhost:3000/swagger/index.html](http://localhost:3000/swagger/index.html) for [Swagger](https://github.com/wordnik/swagger-ui) generated documentation.

Uses provided H2 database for some data (mostly random generated).


## Stack

- [Korma](http://sqlkorma.com/)
- [compojure](https://github.com/weavejester/compojure)
- [swag](https://github.com/narkisr/swag)
- [metrics-clojure](https://github.com/sjl/metrics-clojure)

## License

Copyright © 2014 DevRates.com

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
