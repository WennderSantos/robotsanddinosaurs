(ns robotsandinosaurs.service
  (:require [compojure.api.sweet :refer :all]
            [ring.util.response :as ring-resp]
            [com.stuartsierra.component :as component]
            [robotsandinosaurs.adapters :as adapters]
            [robotsandinosaurs.controllers.space-ctrl :as ctrl.space]
            [robotsandinosaurs.controllers.dinosaur-ctrl :as ctrl.dinosaur]
            [robotsandinosaurs.controllers.robot-ctrl :as ctrl.robot]))

(defn get-space [storage]
  (-> (ctrl.space/get-space storage)
      (adapters/space->list-objects)
      (ring-resp/response)))

(defn restart-space [storage]
  (-> (ctrl.space/restart! storage)
      (adapters/space->list-objects)
      (ring-resp/response)))

(defn create-dinosaur [storage {:keys [coord]}]
  (let [new-dinosaur (ctrl.dinosaur/create-dinosaur! storage coord)]
    (ring-resp/created
      "/space"
      new-dinosaur)))

(defn create-robot [storage robot]
  (let [new-robot (ctrl.robot/create-robot! storage robot)]
    (ring-resp/created
      "/space"
      new-robot)))

(defn turn-robot-face [storage {:keys [coord direction-to-turn]}]
  (-> (ctrl.robot/turn-robot-face! storage coord direction-to-turn)
      (ring-resp/response)))

(defn all-routes [storage]
  (api
    (context "/space" []
      (GET "/" []
        (get-space storage))
      (PUT "/restart" []
        (restart-space storage)))
    (context "/dinosaur" []
      (POST "/" []
        :body [dinosaur adapters/Dinosaur]
        (create-dinosaur storage dinosaur)))
    (context "/robot" []
      (POST "/" []
        :body [robot adapters/Robot]
        (create-robot storage robot))
      (PUT "/turn-face-direction" []
        :body [instruction adapters/Instruction-to-turn-robot-face]
        (turn-robot-face storage instruction)))))

(defn app [storage]
  (all-routes storage))
