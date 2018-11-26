(ns robotsandinosaurs.controllers.robot-ctrl
  (:require [robotsandinosaurs.logic :as logic]
            [robotsandinosaurs.db.robot-db :as db.robot]
            [robotsandinosaurs.db.dinosaur-db :as db.dinosaur]
            [robotsandinosaurs.schemas :as schemas]
            [clojure.string :as str])
  (:import [java.util UUID]))

(defn get-robot [id storage]
  (db.robot/get-robot id storage))

(defn get-robots [storage]
  (db.robot/get-robots storage))

(defn create-robot! [{:keys [coord face-direction]} storage]
  (let [id (str (UUID/randomUUID))
        robot (logic/new-robot (:x coord)
                               (:y coord)
                               face-direction
                               id)]
    (db.robot/create-robot! robot storage)
    {:id id}))

(defn turn-robot-face! [robot {side-to-turn :side-to-turn} storage]
  (let [face-direction (logic/turn-face-direction (:face-direction robot)
                                                   schemas/directions
                                                   side-to-turn)]
    (db.robot/update-face-direction! (:id robot) face-direction storage)
    (get-robot (:id robot) storage)))

(defn robot-attack! [{coord :coord} storage]
  (let [dinosaurs (db.dinosaur/get-dinosaurs storage)
        dinosaurs-after-attack (logic/robot-attack coord dinosaurs)]
    (db.dinosaur/update-dinosaurs! dinosaurs-after-attack storage)
    dinosaurs-after-attack))

(defn robot-move! [robot {instruction :instruction} storage]
  (let [coord (logic/move-robot instruction
                                (:coord robot)
                                (:face-direction robot))]
    (db.robot/update-coord! (:id robot) coord storage)
    (get-robot (:id robot) storage)))
