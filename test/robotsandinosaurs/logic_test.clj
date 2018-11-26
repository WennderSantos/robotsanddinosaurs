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
	(let [face-direction :east]
		(fact "to the left"
			(logic/turn-face-direction face-direction
																 schemas/directions
																 :left) => :north)
		(fact "to the right"
			(logic/turn-face-direction face-direction
																 schemas/directions
																 :right) => :south)))

(fact "Update coord"
	(let [coord {:x 1 :y 1}]
		(fact "x + 1"
			(#'logic/coord-x+1 coord) => {:x 2 :y 1})
		(fact "x - 1"
			(#'logic/coord-x-1 coord) => {:x 0 :y 1})
		(fact "y + 1"
			(#'logic/coord-y+1 coord) => {:x 1 :y 2})
		(fact "y - 1"
			(#'logic/coord-y-1 coord) => {:x 1 :y 0})))

(fact "contains-coord?"
	(let [coords #{{:x 1 :y 1} {:x 2 :y 2} {:x 3 :y 3}}
				coord-in {:x 1 :y 1}
				coord-out {:x 4 :y 4}]
		(#'logic/contains-coord? coords coord-in) => true
		(#'logic/contains-coord? coords coord-out) => nil))

(fact "Robot attack should remove all dinosaurs around it"
	(let [robot-coord {:x 1 :y 1}
				dinosaurs {"234-qwer-1234-dg" {:id "234-qwer-1234-dg" :coord {:x 0 :y 1}}
									 "098-asgd-879-ffg" {:id "098-asgd-879-ffg" :coord {:x 1 :y 0}}
									 "123-xsdf-765-mnb" {:id "123-xsdf-765-mnb" :coord {:x 3 :y 3}}}]
		(logic/robot-attack robot-coord dinosaurs) => {"123-xsdf-765-mnb" {:id "123-xsdf-765-mnb" :coord {:x 3 :y 3}}}))

(fact "Robot move"
	(logic/move-robot :move-forward {:x 1 :y 1} :north) => {:x 1 :y 2}
	(logic/move-robot :move-backwards {:x 2 :y 1} :east) => {:x 1 :y 1})
