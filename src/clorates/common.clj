(ns clorates.common
  (:require [clojure.string :as str]))

(defn map-map
  "Returns a new map with each key-value pair in `m` transformed by `f`. `f` takes the arguments `[key value]` and should return a value castable to a map entry, such as `{transformed-key transformed-value}`."
  [f m]
  (into (empty m) (map #(apply f %) m)) )

(defn map-map-keys [f m]
  (map-map (fn [key value] {(f key) value}) m) )

(defn map-map-values [f m]
  (map-map (fn [key value] {key (f value)}) m) )

(defn keys-to-lowercase [v]
  (map-map-keys #(keyword (str/lower-case (.getName %))) v))

(defn keep-only-keys [keys m]
  (conj {} (select-keys m keys)))

(defn parse-int [value] 
  (Integer/parseInt value))

(defn having-vals-count-of [m cnt] 
  (into {} (filter #(-> % val count (= cnt)) m)))
