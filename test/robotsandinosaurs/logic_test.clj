(ns robotsandinosaurs.logic-test
  (:require [robotsandinosaurs.logic :as logic]
						[midje.sweet :refer :all]
						[robotsandinosaurs.adapters :as adapters]
						[robotsandinosaurs.schemas :as schemas]
						[clojure.string :as str])
  (:import [java.util UUID]))

(fact "Generate a new dinosaur"
	(let [dinosaur-id (str (UUID/randomUUID))]
		(logic/new-dinosaur 1 1 dinosaur-id) => {dinosaur-id {:id dinosaur-id
																													:coord {:x 1	:y 1}}}))

(fact "Generate a new robot"
	(let [robot-id (str (UUID/randomUUID))]
		(logic/new-robot 1 2 :north robot-id) => {robot-id {:id robot-id
																												:coord {:x 1 :y 2}
																												:face-direction :north}}))

(fact "Turn robots face direction"
	(fact "to the left when facing north"
		(logic/turn-face-direction :left
															 schemas/directions
															 :north) => :west)
	(fact "to the right when facing west"
		(logic/turn-face-direction :right
															 schemas/directions
															 :west) => :north)
	(fact "to the left when facing east"
		(logic/turn-face-direction :left
															 schemas/directions
															 :east) => :north)
	(fact "to the right when facing south"
		(logic/turn-face-direction :right
															 schemas/directions
															 :south) => :west))

(fact "Robot attack should remove all dinosaurs around it"
	(let [robot-coord {:x 1 :y 1}
				dinosaurs {"234-qwer-1234-dg" {:id "234-qwer-1234-dg" :coord {:x 1 :y 2}}
									 "098-asgd-879-ffg" {:id "098-asgd-879-ffg" :coord {:x 0 :y 1}}
									 "123-xsdf-765-mnb" {:id "123-xsdf-765-mnb" :coord {:x 1 :y 0}}
									 "876-mnvc-096-lko" {:id "876-mnvc-096-lko" :coord {:x 2 :y 1}}
									 "420-ytre-405-poo" {:id "420-ytre-405-poo" :coord {:x 9 :y 4}}}]
		(logic/robot-attack robot-coord dinosaurs) => '("234-qwer-1234-dg"
																										"098-asgd-879-ffg"
																										"123-xsdf-765-mnb"
																										"876-mnvc-096-lko")))

(fact "Robot move"
	(let [coord {:x 1 :y 1}]
		(fact "when facing north"
			(fact "move-forward"
				(logic/move-robot :move-forward coord :north) => {:x 1 :y 2})
			(fact "move-backwards"
				(logic/move-robot :move-backwards coord :north) => {:x 1 :y 0}))
		(fact "when facing east"
			(fact "move-forward"
				(logic/move-robot :move-forward coord :east) => {:x 2 :y 1}))
			(fact "move-backwards"
				(logic/move-robot :move-backwards coord :east) => {:x 0 :y 1})
		(fact "when facing south"
			(fact "move-forward"
				(logic/move-robot :move-forward coord :south) => {:x 1 :y 0}))
			(fact "move-backwards"
				(logic/move-robot :move-backwards coord :south) => {:x 1 :y 2})
		(fact "when facing west"
			(fact "move-forward"
				(logic/move-robot :move-forward coord :west) => {:x 0 :y 1}))
			(fact "move-backwards"
				(logic/move-robot :move-backwards coord :west) => {:x 2 :y 1})))
