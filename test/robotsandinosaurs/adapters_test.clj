(ns robotsandinosaurs.adapters-test
  (:require [robotsandinosaurs.logic :as logic]
						[midje.sweet :refer :all]
						[robotsandinosaurs.adapters :as adapters]))

(fact "Convert space into a list of creatures"
  (let [empty-space {}
        space {:robots {"234-qwr-124" {:id "234-qwr-124"
                                       :coord {:x 1 :y 1}
                                       :face-direction :north}}
               :dinosaurs {"456-dfg-865" {:id "456-dfg-865"
                                          :coord {:x 2 :y 2}}}}
        creatures-list {:dinosaurs '({:coord {:x 2 :y 2}
                                      :id "456-dfg-865"})
                        :robots '({:coord {:x 1 :y 1}
                                   :face-direction :north
                                   :id "234-qwr-124"})}]
    (adapters/space->list-creatures empty-space) => {:dinosaurs '() :robots '()}
    (adapters/space->list-creatures space) => creatures-list))

(fact "Convert robots into a list"
  (let [robots {"234-qwr-124" {:id "234-qwr-124"
                               :coord {:x 1 :y 1}
                               :face-direction :north}
                "456-dfg-865" {:id "456-dfg-865"
                               :coord {:x 2 :y 2}
                               :face-direction :south}}]
    (adapters/robots->list robots) => '({:coord {:x 1 :y 1}
                                        :face-direction :north
                                        :id "234-qwr-124"}
                                       {:coord {:x 2 :y 2}
                                       :face-direction :south
                                       :id "456-dfg-865"})))

(fact "Convert dinosaurs into a list"
  (let [dinosaurs {"234-qwr-124" {:id "234-qwr-124"
                                  :coord {:x 1 :y 1}}
                   "456-dfg-865" {:id "456-dfg-865"
                                  :coord {:x 2 :y 2}}}]
    (adapters/dinosaurs->list dinosaurs) => '({:coord {:x 1 :y 1}
                                               :id "234-qwr-124"}
                                              {:coord {:x 2 :y 2}
                                               :id "456-dfg-865"})))