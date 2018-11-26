(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]))

(defn dinosaurs->list [dinosaurs]
  (map #(into {} (rest %)) dinosaurs))

(defn robots->list [robots]
  (map #(into {} (rest %)) robots))

(defn space->list-creatures [space]
  (let [dinosaurs (dinosaurs->list (:dinosaurs space))
        robots (robots->list (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))
