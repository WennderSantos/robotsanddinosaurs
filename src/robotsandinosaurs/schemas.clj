(ns robotsandinosaurs.schemas
  "Schemas used to validate schema in data coming through requests."
  (:require [schema.core :as s]))

(def ^:private grid {:lenght 50})

(def ^:private Cell (s/constrained s/Int
                                    #(and
                                     (>= % 0)
                                     (< % (:lenght grid)))))

(def Coord {:x Cell :y Cell})

(def Dinosaur {:coord Coord})

(def directions '(:north :east :south :west))
(def ^:private Face-direction (s/enum :north :east :south :west))

(def Robot {:coord Coord
            :face-direction Face-direction})

(def Sides-to-turn {:side-to-turn (s/enum :left :right)})

(def Move-instruction {:instruction (s/enum :move-forward :move-backwards)})

(defn check-schema [schema to-check]
  (s/check schema to-check))