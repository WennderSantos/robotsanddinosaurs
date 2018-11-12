(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]))

(defonce grid {:lenght 50})
(def Cell (s/constrained s/Int #(and (>= % 0) (< % (:lenght grid)))))
(def Coord {:x Cell :y Cell})

(defn space->list-objects [current-space]
  (let [dinosaurs (map #(into {} {:coord (logic/coord-into-map %)}) (:dinosaur current-space))
        robots (map #(into {} {:coord (logic/coord-into-map (first %)) :face-direction (last %)}) (:robot current-space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))