(ns clorates.core-test
  (:import (java.io InputStreamReader BufferedReader))
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [korma.core :refer :all]
            [clorates.db :refer :all]
            [clorates.core :refer [app]]
            [ring.mock.request :refer [request header]]))

(deftest get-projects-test
  (let [get-projects-resp (app (request :get "/projects?tags=java,mvc"))
        body-stream (:body get-projects-resp)
        body-string (apply str (line-seq (BufferedReader.
                        (InputStreamReader. body-stream))))]
    (is (= (:status get-projects-resp) 200))
    (is (= (count (json/read-str body-string)) 1))))
          