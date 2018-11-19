(ns robotsandinosaurs.db.space-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn get-space [storage]
  (storage-client/read-all storage))

(defn delete-space! [storage]
  (storage-client/clear-all! storage))
