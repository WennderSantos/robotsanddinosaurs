(ns robotsandinosaurs.controllers.robot-ctrl
  (:import [java.util UUID])
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.robot-db :as db.robot]
            [robotsandinosaurs.db.dinosaur-db :as db.dinosaur]
            [robotsandinosaurs.schemas :as schemas]
            [robotsandinosaurs.adapters :as adapters]))

(defn get-robot [id storage]
  (db.robot/get-robot id storage))

(defn create-robot! [{:keys [coord face-direction]} storage]
  (let [id (adapters/uuid->id (UUID/randomUUID))
        robot (logic/new-robot (:x coord)
                               (:y coord)
                               face-direction
                               id)]
    (db.robot/create-robot! robot storage)
    id))

(defn turn-robot-face! [storage coord direction-to-turn]
  (let [robot-id (logic/coord-into-string coord)
        face-direction (db.robot/get-robot-face-direction storage robot-id)
        new-face-direction (logic/turn-robot-face face-direction
                                                  schemas/directions
                                                  direction-to-turn)]
    (db.robot/update-robot-face-direction! storage
                                           robot-id
                                           new-face-direction)))

(defn robot-attack! [storage coord]
  (let [dinosaurs (db.dinosaur/get-dinosaurs storage)
        dinosaurs-after-attack (logic/robot-attack coord dinosaurs)]
    (db.dinosaur/update-dinosaurs! storage dinosaurs-after-attack)))

(defn robot-move! [storage coord where]
  (let [robot-id (logic/coord-into-string coord)
        face-direction (db.robot/get-robot-face-direction storage robot-id)
        new-coord (logic/move-robot where coord face-direction)]
    (db.robot/remove-robot! storage robot-id)
    (db.robot/create-robot! storage new-coord face-direction)))
