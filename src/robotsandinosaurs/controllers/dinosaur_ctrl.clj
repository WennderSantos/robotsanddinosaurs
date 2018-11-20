(ns robotsandinosaurs.controllers.dinosaur-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.dinosaur-db :as db.dinosaur]))

(defn create-dinosaur! [storage coord]
  (db.dinosaur/create-dinosaur! storage (logic/coord-into-string coord))
  {:coord coord})