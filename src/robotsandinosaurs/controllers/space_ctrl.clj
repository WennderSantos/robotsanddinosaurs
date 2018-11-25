(ns robotsandinosaurs.controllers.space-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.space-db :as db.space]))

(defn get-space [storage]
  (db.space/get-space storage))

(defn restart! [storage]
  (db.space/delete-space! storage))
