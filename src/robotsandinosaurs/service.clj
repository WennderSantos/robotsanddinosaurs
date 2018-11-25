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
  (if-let [dinosaur (ctrl.dinosaur/get-dinosaur id storage)]
    (ring-resp/response dinosaur)
    (ring-resp/not-found {})))

(defn create-dinosaur [dinosaur storage]
  (let [dinosaur-id (ctrl.dinosaur/create-dinosaur! dinosaur storage)]
    (ring-resp/created
      (str "/dinosaurs/" dinosaur-id)
      dinosaur-id)))

(defn get-robot [id storage]
  (if-let [robot (ctrl.robot/get-robot id storage)]
    (ring-resp/response robot)
    (ring-resp/not-found {})))

(defn create-robot [robot storage]
  (let [robot-id (ctrl.robot/create-robot! robot storage)]
    (ring-resp/created
      (str "/robots/" robot-id)
      robot-id)))

(defn turn-robot-face [id side-to-turn storage]
  (-> (ctrl.robot/turn-robot-face! id side-to-turn storage)
      (ring-resp/response)))

(defn robot-attack [id storage]
  (-> (ctrl.robot/robot-attack! id storage)
      (ring-resp/response)))

(defn robot-move [id instruction storage]
  (-> (ctrl.robot/robot-move! id instruction storage)
      (ring-resp/response)))

(defn all-routes [storage]
  (api
    (context "/space" []
      (GET "/" []
        (get-space storage))
      (PUT "/restart" []
        (restart-space storage)))
    (context "/dinosaurs" []
      (GET "/:id" [id]
        (get-dinosaur id storage))
      (POST "/" []
        :body [dinosaur schemas/Dinosaur]
        (create-dinosaur dinosaur storage)))
    (context "/robots" []
      (POST "/" []
        :body [robot schemas/Robot]
        (create-robot robot storage))
      (context "/:id" [id]
        (GET "/" [id]
          (get-robot id storage))
        (POST "/face-direction" [id]
          :body [side-to-turn schemas/Sides-to-turn]
          (turn-robot-face id side-to-turn storage))
        (POST "/attack" [id]
          (robot-attack id storage))
        (PUT "/move" [id]
          :body [instruction schemas/Move-instruction]
          (robot-move id instruction storage))))))

(defn app [storage]
  (all-routes storage))
