(ns robotsandinosaurs.service
  (:require [compojure.api.sweet :refer :all]
            [ring.util.response :as ring-resp]
            [com.stuartsierra.component :as component]
            [robotsandinosaurs.adapters :as adapters]
            [robotsandinosaurs.schemas :as schemas]
            [robotsandinosaurs.controllers.space-ctrl :as ctrl.space]
            [robotsandinosaurs.controllers.dinosaur-ctrl :as ctrl.dinosaur]
            [robotsandinosaurs.controllers.robot-ctrl :as ctrl.robot]))

(defn get-space [storage]
  (-> (ctrl.space/get-space storage)
      (adapters/space->objects)
      (ring-resp/response)))

(defn restart-space [storage]
  (-> (ctrl.space/restart! storage)
      (ring-resp/response)))

(defn get-dinosaur [id storage]
  (let [dinosaur (ctrl.dinosaur/get-dinosaur id storage)]
    (if dinosaur
      (ring-resp/response dinosaur)
      (ring-resp/not-found {}))))

(defn create-dinosaur [dinosaur storage]
  (let [dinosaur-id (ctrl.dinosaur/create-dinosaur! dinosaur storage)]
    (ring-resp/created
      (str "/dinosaur/" dinosaur-id)
      dinosaur-id)))

(defn get-robot [id storage]
  (let [robot (ctrl.robot/get-robot id storage)]
    (if robot
      (ring-resp/response robot)
      (ring-resp/not-found {}))))

(defn create-robot [robot storage]
  (let [robot-id (ctrl.robot/create-robot! robot storage)]
    (ring-resp/created
      (str "/robot/" robot-id)
      robot-id)))

(defn turn-robot-face [id side-to-turn storage]
  (-> (ctrl.robot/turn-robot-face! id side-to-turn storage)
      (ring-resp/response)))


;;(defn robot-attack [storage {:keys [coord]}]
;;  (ctrl.robot/robot-attack! storage coord)
;;  (get-space storage))
;;
;;(defn robot-move [storage {:keys [coord where]}]
;;  (ctrl.robot/robot-move! storage coord where)
;;  (get-space storage))

(defn all-routes [storage]
  (api
    (context "/space" []
      (GET "/" []
        (get-space storage))
      (PUT "/restart" []
        (restart-space storage)))
    (context "/dinosaur" []
      (GET "/:id" [id]
        (get-dinosaur id storage))
      (POST "/" []
        :body [dinosaur schemas/Dinosaur]
        (create-dinosaur dinosaur storage)))
    (context "/robot" []
      (POST "/" []
        :body [robot schemas/Robot]
        (create-robot robot storage))
      (context "/:id" [id]
        (GET "/" [id]
          (get-robot id storage))
        (POST "/face-direction" [id]
          :body [side-to-turn schemas/Sides-to-turn]
          (turn-robot-face id side-to-turn storage))))))
;;      (PUT "/attack" []
;;        :body [instruction adapters/Instruction-robot-attack]
;;        (robot-attack storage instruction))
;;      (PUT "/move" []
;;        :body [instruction adapters/Instruction-robot-move]
;;        (robot-move storage instruction)))))
;;
(defn app [storage]
  (all-routes storage))
