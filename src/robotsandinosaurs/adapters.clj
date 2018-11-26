(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]))

(defn creatures->list [creatures]
  (map #(into {} (rest %)) creatures))

(defn space->list-creatures [space]
  (let [dinosaurs (creatures->list (:dinosaurs space))
        robots (creatures->list (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))
