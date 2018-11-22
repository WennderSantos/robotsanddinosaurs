(ns robotsandinosaurs.schemas
  (:require [schema.core :as s]))

(defonce grid {:lenght 50})

(def Cell (s/constrained
            s/Int
            #(and
              (>= % 0)
              (< % (:lenght grid)))))

(def Dinosaur {:coord {:x Cell
                       :y Cell}})