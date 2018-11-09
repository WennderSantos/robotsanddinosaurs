(ns robotsandinosaurs.service
  (:require [compojure.core :refer :all]
            [ring.util.response :as ring-resp]
            [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [robotsandinosaurs.adapters :as adapters]
            [robotsandinosaurs.controllers.space-ctrl :as space-controller]))

(defn get-current-space [storage]
  (-> (space-controller/get-current-space storage)
      (adapters/space->list-objects)
      (ring-resp/response)))

(defn restart-space [storage]
  (-> (space-controller/restart! storage)
      (adapters/space->list-objects)
      (ring-resp/response)))

(defn all-routes [storage]
  (routes
    (context "/space" []
      (GET "/" [] (get-current-space storage))
      (POST "/restart" [] (restart-space storage)))))

(defn app [storage]
  (wrap-json-response (all-routes storage)))

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