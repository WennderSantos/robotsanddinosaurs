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

(defn space->objects [space]
  (let [dinosaurs (map #(into {} (rest %)) (:dinosaurs space))
        robots (map #(into {} (rest %)) (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))