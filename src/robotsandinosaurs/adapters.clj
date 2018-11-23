(ns robotsandinosaurs.adapters
  (:require [schema.core :as s]
            [robotsandinosaurs.logic :as logic]
            [clojure.string :as str]))

(defn uuid->id [uuid]
  (str uuid))

(defn space->objects [space]
  (let [dinosaurs (map #(into {} (rest %)) (:dinosaurs space))
        robots (map #(into {} (rest %)) (:robots space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))