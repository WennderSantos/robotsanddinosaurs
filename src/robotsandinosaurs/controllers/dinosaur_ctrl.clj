(ns robotsandinosaurs.controllers.dinosaur-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.dinosaur-db :as db.dinosaur]
            [clojure.string :as str])
  (:import [java.util UUID]))

(defn create-dinosaur! [{:keys [coord]} storage]
  (let [id (str (UUID/randomUUID))
        dinosaur (logic/new-dinosaur (:x coord) (:y coord) id)]
    (db.dinosaur/create! dinosaur storage)
    {:id id}))

(defn get-dinosaur [id storage]
  (db.dinosaur/get-dinosaur id storage))

(defn get-dinosaurs [storage]
  (db.dinosaur/get-dinosaurs storage))