(ns robotsandinosaurs.system
  (:require [com.stuartsierra.component :as component]
            [robotsandinosaurs.service :as service]
            [robotsandinosaurs.components.storage :as storage]))

(defn build []
  (component/system-map
      :server  (component/using (service/new-webserver) [:storage])
      :storage (storage/new-in-memory)))

(defn build-and-start []
  (-> (build)
      (component/start)))