(ns robotsandinosaurs.controllers.robot-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.robot-db :as db.robot]
            [robotsandinosaurs.adapters :as adapters]))

(defn create-robot! [storage {:keys [coord face-direction]}]
  (db.robot/create-robot! storage
                          (logic/coord-into-string coord)
                          face-direction)
  {:coord coord :face-direction face-direction})

(defn turn-robot-face! [storage coord direction-to-turn]
  (let [robot-id (logic/coord-into-string coord)
        face-direction (db.robot/get-robot-face-direction storage robot-id)
        new-face-direction (logic/turn-robot-face face-direction
                                                  adapters/directions
                                                  direction-to-turn)]
    (db.robot/update-robot-face-direction! storage
                                           robot-id
                                           new-face-direction)))