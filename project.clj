(defproject clorates "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
    [org.clojure/clojure "1.5.1"]
    [korma "0.3.0-RC6"]
    [com.h2database/h2 "1.3.170"] 
    [postgresql/postgresql "9.1-901-1.jdbc4"]
    [ch.qos.logback/logback-classic "1.1.0"]
    [swag "0.2.7-SNAPSHOT"]
    [ring-middleware-format "0.3.0"]
    [ring/ring-jetty-adapter "1.2.0"]
    [ring "1.2.0"]
    [ring-mock "0.1.5"]
    [org.clojure/data.json "0.2.4"]
    [clabango "0.5"]
    [lib-noir "0.7.9"]
    [compojure "1.1.6" :exclusions  [ring/ring-core]]
    [metrics-clojure-ring "1.0.1"]]
  :main clorates.main)
