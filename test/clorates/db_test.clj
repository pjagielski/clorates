(ns clorates.db-test
  (:require [clojure.test :refer :all]
  			[korma.core :refer :all]
            [clorates.db :refer :all]))

(deftest count-all-projects-test
  (let [all-projects (get-all-projects 100 0)]
    (is (= 68 (count all-projects)))
    (let [first-project (dissoc (first all-projects) :LOGO)]
      (is (= 5 (count (get first-project :tag)))))))

(deftest transform-project-test
  (let [transformed (transform-project (first (get-all-projects 10 0)))]
    (and
      (is (= 5 (count (:tags transformed))))
      (is (= "Play" (:name transformed)))
      (is (nil? (:logo transformed))))))
