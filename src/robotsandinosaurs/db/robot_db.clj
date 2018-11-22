(ns robotsandinosaurs.db.robot-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn- create-robot-or-update-its-face-direction! [storage coord face-direction]
  (storage-client/put! storage #(assoc-in % [:robot coord] face-direction)))

(defn create-robot! [robot storage]
  (storage-client/put! storage #(update % :robots conj robot)))

(defn get-robot [id storage]
  (-> (storage-client/read-all storage)
      (:robots)
      (get id)))


(defn update-robot-face-direction! [storage coord face-direction]
  (create-robot-or-update-its-face-direction! storage coord face-direction))

(defn get-robot-face-direction [storage coord]
  (-> (storage-client/read-all storage)
      (get-in [:robot coord])))

(defn remove-robot! [storage coord]
  (storage-client/put! storage #(update % :robot dissoc coord)))