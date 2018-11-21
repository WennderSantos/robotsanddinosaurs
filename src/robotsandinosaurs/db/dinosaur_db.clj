(ns robotsandinosaurs.db.dinosaur-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create-dinosaur! [storage coord]
  (storage-client/put! storage #(update % :dinosaur conj coord)))

(defn update-dinosaurs! [storage dinosaurs]
  (storage-client/put! storage #(assoc % :dinosaur dinosaurs)))

(defn get-dinosaurs [storage]
  (-> (storage-client/read-all storage)
      (:dinosaur)))