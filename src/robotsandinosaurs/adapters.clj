(ns robotsandinosaurs.adapters
  (:require [robotsandinosaurs.logic :as logic]))

(defn space->list-objects [current-space]
  (let [dinosaurs (map #(into {} {:coord (logic/coord-into-map %)}) (:dinosaur current-space))
        robots (map #(into {} {:coord (logic/coord-into-map (first %)) :face-direction (last %)}) (:robot current-space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))