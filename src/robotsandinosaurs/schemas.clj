(ns robotsandinosaurs.schemas
  (:require [schema.core :as s]))

(def ^:private grid {:lenght 50})

(def ^:private Cell (s/constrained s/Int
                                    #(and
                                     (>= % 0)
                                     (< % (:lenght grid)))))

(def Dinosaur {:coord {:x Cell
                       :y Cell}})

(def directions [:north :east :south :west])
(def ^:private Face-direction (s/enum :north :east :south :west))

(def Robot {:coord {:x Cell
                    :y Cell}
            :face-direction Face-direction})

(def Sides-to-turn {:side-to-turn (s/enum :left :right)})

(def Move-instruction {:instruction (s/enum :move-forward :move-backwards)})