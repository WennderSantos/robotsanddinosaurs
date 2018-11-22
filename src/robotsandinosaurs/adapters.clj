(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]
            [clojure.string :as str]))

(defn uuid->id [uuid]
  (str uuid))

;;(def Cell (s/constrained
;;            s/Int
;;            #(and
;;              (>= % 0)
;;              (< % (:lenght grid)))))
;;
;;(def Coord {:x Cell
;;            :y Cell})
;;
;;(def Dinosaur {:coord Coord})
;;
(def directions #{"north" "east" "south" "west"})
;;(def Face-direction (s/enum "north" "east" "south" "west"))
;;
;;(def Robot {:coord Coord
;;            :face-direction Face-direction})
;;
;;(def Side (s/enum :left :right))
;;
;;(def Instruction-to-turn-robot-face {:coord Coord :direction-to-turn Side})
;;
;;(def Instruction-robot-attack {:coord Coord})
;;
;;(def Moves (s/enum :move-forward :move-backwards))
;;(def Instruction-robot-move {:coord Coord :where Moves})
;;
(defn space->objects [space]
  (let [dinosaurs (map #(into {} (rest %)) (:dinosaurs space))
        robots (map #(into {} (rest %)) (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))