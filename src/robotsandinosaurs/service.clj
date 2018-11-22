(ns robotsandinosaurs.service
  (:require [compojure.api.sweet :refer :all]
            [ring.util.response :as ring-resp]
            [com.stuartsierra.component :as component]
            [robotsandinosaurs.adapters :as adapters]
            [robotsandinosaurs.schemas :as schema]
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

(defn create-dinosaur [storage dinosaur]
  (let [dinosaur-id (ctrl.dinosaur/create-dinosaur! storage dinosaur)]
    (ring-resp/created
      (str "/dinosaur/" dinosaur-id)
      dinosaur-id)))

(defn get-dinosaur [id storage]
  (let [dinosaur (ctrl.dinosaur/get-dinosaur id storage)]
    (if dinosaur
      (ring-resp/response dinosaur)
      (ring-resp/not-found {}))))

;;(defn create-robot [storage robot]
;;  (let [new-robot (ctrl.robot/create-robot! storage robot)]
;;    (ring-resp/created
;;      "/space"
;;      new-robot)))
;;
;;(defn turn-robot-face [storage {:keys [coord direction-to-turn]}]
;;  (-> (ctrl.robot/turn-robot-face! storage coord direction-to-turn)
;;      (ring-resp/response)))
;;
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
      (POST "/" []
        :body [dinosaur schema/Dinosaur]
        (create-dinosaur storage dinosaur))
      (GET "/:id" [id]
        (get-dinosaur id storage)))))
;;    (context "/robot" []
;;      (POST "/" []
;;        :body [robot adapters/Robot]
;;        (create-robot storage robot))
;;      (PUT "/turn-face-direction" []
;;        :body [instruction adapters/Instruction-to-turn-robot-face]
;;        (turn-robot-face storage instruction))
;;      (PUT "/attack" []
;;        :body [instruction adapters/Instruction-robot-attack]
;;        (robot-attack storage instruction))
;;      (PUT "/move" []
;;        :body [instruction adapters/Instruction-robot-move]
;;        (robot-move storage instruction)))))
;;
(defn app [storage]
  (all-routes storage))
