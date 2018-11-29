(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]))

(defn creatures->list [creatures]
"Creatures will be flattened
  From: {id {...}}
  To: {...}"
  (map #(into {} (rest %)) creatures))

(defn space->list-creatures [space]
  "Returns a map os :robots and :dinosaurs"
  (let [dinosaurs (creatures->list (:dinosaurs space))
        robots (creatures->list (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))
