(ns clorates.props
  (:import (java.util Properties)
           (java.io FileInputStream)))

(defn load-properties [filename]
  (into {} (doto (Properties.)
     (.load (FileInputStream. filename)))))

(def props-file (or (System/getProperty "clorates.properties") "clorates.properties"))

(println "Loading properties from:" props-file)

(def props (load-properties props-file))