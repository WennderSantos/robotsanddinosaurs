(ns robotsandinosaurs.controllers.space-ctrl
  (:require [robotsandinosaurs.db.space :as db]))

(defn get-current-space [storage]
  (db/get-current-space storage))

(defn restart! [storage]
  (db/delete-space! storage))