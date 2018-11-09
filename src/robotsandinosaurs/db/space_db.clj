(ns robotsandinosaurs.db.space-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn get-current-space [storage]
  (-> (storage-client/read-all storage)
      (last)))

(defn delete-space! [storage]
  (storage-client/clear-all! storage))