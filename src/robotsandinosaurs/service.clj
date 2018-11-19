(ns robotsandinosaurs.service
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer :all]
            [robotsandinosaurs.adapters :as adapters]
            [robotsandinosaurs.controllers.space-ctrl :as space-ctrl]
            [robotsandinosaurs.controllers.dinosaur-ctrl :as dinosaur-ctrl]
            [robotsandinosaurs.controllers.robot-ctrl :as robot-ctrl]))

(defn get-space [storage]
  (-> (space-ctrl/get-space storage)
      (adapters/space->list-objects)))

(defn restart-space [storage]
  (-> (space-ctrl/restart! storage)
      (adapters/space->list-objects)))

(defn create-dinosaur [storage coord]
  (let [dinosaur (dinosaur-ctrl/create-dinosaur! storage coord)]
    {:dinosaur dinosaur}))

(defn all-routes [storage]
  (api
    (context "/space" []
      (GET "/" [] (ok (get-space storage)))
      (PUT "/restart" [] (ok (restart-space storage))))
    (context "/dinosaur" []
      (POST "/" []
        :body [coord adapters/Coord]
        (ok (create-dinosaur storage coord))))))

(defn app [storage]
  (all-routes storage))

(defrecord WebServer [storage]
  component/Lifecycle
  (start [this]
    (assoc this :http-server
                    (run-jetty (app storage) {:port (Integer. (or (System/getenv "PORT") "8080"))
                                    :join? false})))
  (stop [this]
    (dissoc this :http-server)
    this))

(defn new-webserver []
  (->WebServer {}))