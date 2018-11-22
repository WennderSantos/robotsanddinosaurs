(ns robotsandinosaurs.controllers.dinosaur-ctrl
  (:import [java.util UUID])
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.dinosaur-db :as db.dinosaur]
            [robotsandinosaurs.adapters :as adapters]))

(defn create-dinosaur! [storage {:keys [coord]}]
  (let [id (adapters/uuid->id (UUID/randomUUID))
        dinosaur (logic/new-dinosaur (:x coord) (:y coord) id)]
    (db.dinosaur/create! storage dinosaur)
    id))

(defn get-dinosaur [id storage]
  (db.dinosaur/get-dinosaur id storage))