(ns robotsandinosaurs.controllers.robot-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.robot-db :as db.robot]))

(defn create-robot! [storage {:keys [coord face-direction]}]
  (db.robot/create-robot! storage (logic/coord-into-string coord) face-direction)
  {:coord coord :face-direction face-direction})