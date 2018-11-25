(ns robotsandinosaurs.db.dinosaur-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create! [dinosaur storage]
  (storage-client/put! storage #(update % :dinosaurs conj dinosaur)))

(defn update-dinosaurs! [dinosaurs storage]
  (storage-client/put! storage #(assoc % :dinosaurs dinosaurs)))

(defn get-dinosaurs [storage]
  (-> (storage-client/read-all storage)
      (:dinosaurs)))

(defn get-dinosaur [id storage]
  (-> (storage-client/read-all storage)
      (:dinosaurs)
      (get id)))