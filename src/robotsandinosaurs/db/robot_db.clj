(ns robotsandinosaurs.db.robot-db
  (:require [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defn create-robot! [robot storage]
  (storage-client/put! storage #(update % :robots conj robot)))

(defn get-robot [id storage]
  (-> (storage-client/read-all storage)
      (:robots)
      (get id)))

(defn get-robots [storage]
  (-> (storage-client/read-all storage)
      (:robots)))

(defn update-face-direction! [id face-direction storage]
  (storage-client/put! storage #(assoc-in %
                                          [:robots id :face-direction]
                                          face-direction)))
(defn update-coord! [id coord storage]
  (storage-client/put! storage #(assoc-in %
                                           [:robots id :coord]
                                           coord)))