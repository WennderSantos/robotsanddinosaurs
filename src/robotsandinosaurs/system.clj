(ns robotsandinosaurs.system
  (:require [com.stuartsierra.component :as component]
            [robotsandinosaurs.components.webserver :as webserver]
            [robotsandinosaurs.service :as service]
            [robotsandinosaurs.components.storage :as storage]))

(defn- build []
  (component/system-map
      :server  (component/using (webserver/new-webserver #'service/all-routes) [:storage])
      :storage (storage/new-in-memory)))

(defn build-and-start []
  "Configure components dependencies and start the system"
  (-> (build)
      (component/start)))