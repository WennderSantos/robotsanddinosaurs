(ns robotsandinosaurs.db.dinosaur-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create-dinosaur! [storage coord]
  (storage-client/put! storage #(update % :dinosaur conj coord)))