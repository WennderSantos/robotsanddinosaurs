(ns robotsandinosaurs.controllers.dinosaur-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.space-db :as db]))

(defn create-dinosaur! [storage coord]
  (db/create-dinosaur! storage (logic/coord-into-string coord))
  coord)