(ns robotsandinosaurs.components.webserver
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer :all]))

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