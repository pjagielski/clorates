(ns clorates.props)

(defn load-properties [filename]
  (into {} (doto (java.util.Properties.) 
     (.load (java.io.FileInputStream. filename)))))

(def props-file (or (System/getProperty "clorates.properties") "clorates.properties"))

(println "Loading properties from:" props-file)

(def props (load-properties props-file))