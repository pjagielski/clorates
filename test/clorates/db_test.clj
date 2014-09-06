(ns clorates.db-test
  (:require [midje.sweet :refer :all]
            [clorates.db :refer :all]))

(facts "about clorates db"
  (fact "returns results"
    (get-projects-with-all-tags-from "java" 10 0) => ..projects..
      (provided
        (get-projects-ids-with-all-tags-from "java") => ..ids..
        (get-projects-with-ids ..ids.. 10 0) => ..projects..)))
