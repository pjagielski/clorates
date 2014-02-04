(ns clorates.main
    (:require 
        [clorates.core :refer [app]]
        [ring.adapter.jetty :refer [run-jetty]])
    (:gen-class))

(defn -main [& args]
  (run-jetty app {:port 3000 :join? false }))
