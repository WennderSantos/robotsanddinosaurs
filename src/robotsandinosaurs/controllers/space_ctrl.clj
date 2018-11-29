(ns robotsandinosaurs.controllers.space-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.space-db :as db.space]))

(defn get-space [storage]
  (db.space/get-space storage))

(defn restart! [storage]
  (db.space/delete-space! storage))

(defn coord-exist-in-space? [coord storage]
  (let [space (get-space storage)]
    (->> (conj (:robots space) (:dinosaurs space))
         (vals)
         (map #(:coord %))
         (logic/contains-coord? coord))))