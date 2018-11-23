(ns robotsandinosaurs.logic-test
  (:require [robotsandinosaurs.logic :as logic]
						[midje.sweet :refer :all]
						[robotsandinosaurs.adapters :as adapters]
						[robotsandinosaurs.schemas :as schemas])
	(:import [java.util UUID]))

(def dinosaur-id (adapters/uuid->id (UUID/randomUUID)))

(fact "Generate a new dinosaur"
	(logic/new-dinosaur 1 1 dinosaur-id) => {dinosaur-id {:id dinosaur-id
																												:coord {:x 1	:y 1}}})

(def robot-id (adapters/uuid->id (UUID/randomUUID)))

(fact "Generate a new robot"
	(logic/new-robot 1 2 :north robot-id) => {robot-id {:id robot-id
																											:coord {:x 1 :y 2}
																											:face-direction :north}})

(fact "Turn robots face direction"
	(let [face-direction :east]
		(fact "to the left"
			(logic/turn-face-direction face-direction
																 schemas/directions
																 :left) => :north)
		(fact "to the right"
			(logic/turn-face-direction face-direction
																 schemas/directions
																 :right) => :south)))