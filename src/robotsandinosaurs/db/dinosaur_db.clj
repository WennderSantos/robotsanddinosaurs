(ns robotsandinosaurs.db.dinosaur-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create! [dinosaur storage]
  (storage-client/put! storage #(update % :dinosaurs conj dinosaur)))

(defn delete-dinosaurs! [dinosaurs storage]
  (storage-client/put! storage
                       #(apply update-in % [:dinosaurs] dissoc dinosaurs)))

(defn get-dinosaurs [storage]
  (-> (storage-client/read-all storage)
      (:dinosaurs)))

(defn get-dinosaur [id storage]
  (-> (storage-client/read-all storage)
      (:dinosaurs)
      (get id)))