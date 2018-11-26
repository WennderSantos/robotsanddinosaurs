(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]))

(defn space->map-creatures [space]
  (let [dinosaurs (map #(into {} (rest %)) (:dinosaurs space))
        robots (map #(into {} (rest %)) (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))