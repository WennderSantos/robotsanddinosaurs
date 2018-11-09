(ns robotsandinosaurs.service
  (:require [compojure.core :refer :all]
            [ring.util.response :as ring-resp]
            [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [robotsandinosaurs.protocols.storage-client :as db]))

(defn home [state]
    (ring-resp/response (db/read-all state)))

(defn all-routes [state]
  (routes
    (GET "/foo" [] (home state))
    (GET "/bar" [] (home state))))

(defn app [state]
  (wrap-json-response (all-routes state)))

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