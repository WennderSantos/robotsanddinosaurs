(ns robotsandinosaurs.controllers.space-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.space-db :as db.space]))

(defn get-space [storage]
  (db.space/get-space storage))

(defn restart! [storage]
  (db.space/delete-space! storage))

(defn coord-exist-in-space? [coord storage]
  "Returns true or false.
  Given all coords been used in the space
  checks if a coord is present."
  (let [space (get-space storage)]
    (->> (conj (:robots space) (:dinosaurs space))
         (vals)
         (map #(:coord %))
         (logic/contains-coord? coord))))