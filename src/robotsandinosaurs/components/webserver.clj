(ns robotsandinosaurs.components.webserver
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer :all]))

(defrecord WebServer [handler storage]
  component/Lifecycle
  (start [this]
    (assoc this :http-server
                    (run-jetty (handler storage) {:port (Integer.
                                                          (or
                                                            (System/getenv "PORT")
                                                            "8080"))
                                                  :join? false})))
  (stop [this]
    (dissoc this :http-server)
    this))

(defn new-webserver [handler]
  (map->WebServer {:handler handler}))
