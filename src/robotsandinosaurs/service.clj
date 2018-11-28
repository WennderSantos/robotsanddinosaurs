(ns robotsandinosaurs.service
  (:require [compojure.api.sweet :refer :all]
            [ring.util.response :as ring-resp]
            [com.stuartsierra.component :as component]
            [robotsandinosaurs.adapters :as adapters]
            [robotsandinosaurs.schemas :as schemas]
            [robotsandinosaurs.controllers.space-ctrl :as ctrl.space]
            [robotsandinosaurs.controllers.dinosaur-ctrl :as ctrl.dinosaur]
            [robotsandinosaurs.controllers.robot-ctrl :as ctrl.robot]
            [compojure.route :as route]))

(defn- coord-exist-in-space? [coord storage]
  (->> (ctrl.space/get-space storage)
       (adapters/space->list-creatures)
       (mapcat #(rest %))
       (flatten)
       (some #(= coord (:coord %)))))

(defn get-space [storage]
  (-> (ctrl.space/get-space storage)
      (adapters/space->list-creatures)
      (ring-resp/response)))

(defn restart-space [storage]
  (ctrl.space/restart! storage)
  (-> (ring-resp/response {})
      (ring-resp/status 204)))

(defn get-dinosaurs [storage]
  (-> (ctrl.dinosaur/get-dinosaurs storage)
      (adapters/creatures->list)
      (ring-resp/response)))

(defn get-dinosaur [id storage]
  (if-let [dinosaur (ctrl.dinosaur/get-dinosaur id storage)]
    (ring-resp/response dinosaur)
    (ring-resp/not-found {})))

(defn create-dinosaur [dinosaur storage]
  (if (coord-exist-in-space? (:coord dinosaur) storage)
    (ring-resp/bad-request {:error "coord already in use"})
    (let [dinosaur-id (ctrl.dinosaur/create-dinosaur! dinosaur storage)]
      (ring-resp/created
        (str "/dinosaurs/" dinosaur-id)
        {:id dinosaur-id}))))

(defn get-robots [storage]
  (-> (ctrl.robot/get-robots storage)
      (adapters/creatures->list)
      (ring-resp/response)))

(defn get-robot [id storage]
  (if-let [robot (ctrl.robot/get-robot id storage)]
    (ring-resp/response robot)
    (ring-resp/not-found {})))

(defn create-robot [robot storage]
  (if (coord-exist-in-space? (:coord robot) storage)
    (ring-resp/bad-request {:error "coord already in use"})
    (let [robot-id (ctrl.robot/create-robot! robot storage)]
      (ring-resp/created
        (str "/robots/" robot-id)
        {:id robot-id}))))

(defn turn-robot-face [id side-to-turn storage]
  (if-let [robot (ctrl.robot/get-robot id storage)]
    (-> (ctrl.robot/turn-robot-face! robot side-to-turn storage)
        (ring-resp/response))
    (ring-resp/not-found {})))

(defn robot-attack [id storage]
  (if-let [robot (ctrl.robot/get-robot id storage)]
    (-> (ctrl.robot/robot-attack! robot storage)
        (ring-resp/response))
    (ring-resp/not-found {})))

(defn robot-move [id instruction storage]
  (if-let [robot (ctrl.robot/get-robot id storage)]
    (-> (ctrl.robot/robot-move! robot instruction storage)
        (ring-resp/response))
    (ring-resp/not-found {})))

(defn all-routes [storage]
  (api
    (context "/space" []
      (GET "/" []
        (get-space storage))
      (PUT "/restart" []
        (restart-space storage)))
    (context "/dinosaurs" []
      (GET "/" []
        (get-dinosaurs storage))
      (GET "/:id" [id]
        (get-dinosaur id storage))
      (POST "/" []
        :body [dinosaur schemas/Dinosaur]
        (create-dinosaur dinosaur storage)))
    (context "/robots" []
      (GET "/" []
        (get-robots storage))
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
        (POST "/move" [id]
          :body [instruction schemas/Move-instruction]
          (robot-move id instruction storage))))
      (route/not-found (ring-resp/not-found {}))))

(defn app [storage]
  (all-routes storage))
