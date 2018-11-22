(ns robotsandinosaurs.logic-test
  (:require [robotsandinosaurs.logic :as logic]
						[midje.sweet :refer :all]
						[robotsandinosaurs.adapters :as adapters])
	(:import [java.util UUID]))

(def dinosaur-id (adapters/uuid->id (UUID/randomUUID)))

(fact "Generate a new dinosaur"
	(logic/new-dinosaur 1 1 dinosaur-id) => {dinosaur-id {:id dinosaur-id
																												:coord {:x 1	:y 1}}})