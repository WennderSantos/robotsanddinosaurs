(ns robotsandinosaurs.controllers.space-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.space-db :as db]))

(defn get-space [storage]
  (db/get-space storage))

(defn restart! [storage]
  (db/delete-space! storage)
  (get-space storage))

(defn create-dinosaur! [storage coord]
  (db/create-dinosaur! storage (logic/coord-into-string coord))
  coord)