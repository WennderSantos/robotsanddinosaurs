(ns robotsandinosaurs.db.dinosaur-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create! [dinosaur storage]
  (storage-client/put! storage #(update % :dinosaurs conj dinosaur)))

(defn delete-dinosaurs! [dinosaurs storage]
  (when (not-empty dinosaurs)
    (do
      (storage-client/put! storage #(update-in % [:dinosaurs] dissoc (first dinosaurs)))
      (delete-dinosaurs! (rest dinosaurs) storage))))

(defn get-dinosaurs [storage]
  (-> (storage-client/read-all storage)
      (:dinosaurs)))

(defn get-dinosaur [id storage]
  (-> (storage-client/read-all storage)
      (:dinosaurs)
      (get id)))