(ns robotsandinosaurs.adapters-test
  (:require [robotsandinosaurs.logic :as logic]
						[midje.sweet :refer :all]
						[robotsandinosaurs.adapters :as adapters]))

(fact "Convert space into a list of creatures"
  (let [empty-space {}
        space {:robots {"234-qwr-124" {:id "234-qwr-124"
                                       :coord {:x 1 :y 1}
                                       :face-direction :north}}
               :dinosaurs {"456-dfg-865" {:id "234-qwr-124"
                                          :coord {:x 2 :y 2}}}}
        creatures-list {:dinosaurs '({:coord {:x 2 :y 2}
                                      :id "234-qwr-124"})
                        :robots '({:coord {:x 1 :y 1}
                                   :face-direction :north
                                   :id "234-qwr-124"})}]
    (adapters/space->map-creatures empty-space) => {:dinosaurs '() :robots '()}
    (adapters/space->map-creatures space) => creatures-list))