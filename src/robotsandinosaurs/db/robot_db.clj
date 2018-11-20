(ns robotsandinosaurs.db.robot-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create-robot! [storage coord face-direction]
  (storage-client/put! storage #(assoc-in % [:robot coord] face-direction)))