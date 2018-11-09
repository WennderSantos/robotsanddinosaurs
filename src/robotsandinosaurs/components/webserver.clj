(ns robotsandinosaurs.components.webserver
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer :all]))

(defrecord WebServer [config]
  component/Lifecycle
  (start [this]
    (assoc this :http-server
                    (run-jetty ((:app config)) {:port (Integer. (or (System/getenv "PORT") "8080"))
                                    :join? false})))
  (stop [this]
    (dissoc this :http-server)
    this))

(defn new-webserver [config]
  (->WebServer config))