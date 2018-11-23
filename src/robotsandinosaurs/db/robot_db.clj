(ns robotsandinosaurs.db.robot-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create-robot! [robot storage]
  (storage-client/put! storage #(update % :robots conj robot)))

(defn get-robot [id storage]
  (-> (storage-client/read-all storage)
      (:robots)
      (get id)))

(defn update-face-direction! [id face-direction storage]
  (storage-client/put! storage #(assoc-in %
                                          [:robots id :face-direction]
                                          face-direction)))

(defn get-robot-face-direction [storage coord]
  (-> (storage-client/read-all storage)
      (get-in [:robot coord])))

(defn remove-robot! [storage coord]
  (storage-client/put! storage #(update % :robot dissoc coord)))