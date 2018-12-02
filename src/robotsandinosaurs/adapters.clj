(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]))

(defn creatures->list
"Creatures will be flattened
  From: {id {...}}
  To: {...}"
  [creatures]
  (map #(into {} (rest %)) creatures))

(defn space->list-creatures
  "Returns a map os :robots and :dinosaurs"
  [space]
  (let [dinosaurs (creatures->list (:dinosaurs space))
        robots (creatures->list (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))
