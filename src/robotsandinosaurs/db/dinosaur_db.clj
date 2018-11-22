(ns robotsandinosaurs.db.dinosaur-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create! [dinosaur storage]
  (storage-client/put! storage #(update % :dinosaurs conj dinosaur)))

(defn update-dinosaurs! [storage dinosaurs]
  (storage-client/put! storage #(assoc % :dinosaur dinosaurs)))

(defn get-dinosaurs [storage]
  (-> (storage-client/read-all storage)
      (:dinosaur)))

(defn get-dinosaur [id storage]
  (-> (storage-client/read-all storage)
      (:dinosaurs)
      (get id)))