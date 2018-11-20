(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]))

(defonce grid {:lenght 50})

(def Cell (s/constrained
            s/Int
            #(and
              (>= % 0)
              (< % (:lenght grid)))))

(def Coord {:x Cell
            :y Cell})

(def Dinosaur {:coord Coord})

(def directions #{"north" "east" "south" "west"})
(def Face-direction (s/enum "north" "east" "south" "west"))

(def Robot {:coord Coord
            :face-direction Face-direction})

(def Side (s/enum :left :right))

(def Instruction-to-turn-robot-face {:coord Coord :direction-to-turn Side})

(defn space->list-objects [current-space]
  (let [dinosaurs (map #(into {} {:coord (logic/coord-into-map %)}) (:dinosaur current-space))
        robots (map #(into {} {:coord (logic/coord-into-map (first %)) :face-direction (last %)}) (:robot current-space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))