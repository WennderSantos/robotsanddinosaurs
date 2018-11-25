(ns robotsandinosaurs.components.storage
  (:require [com.stuartsierra.component :as component]
            [robotsandinosaurs.protocols.storage-client :as storage-client]))

(defonce empty-space {:robots {} :dinosaurs {}})

(defrecord InMemory[storage]
  component/Lifecycle
  (start [this] this)
  (stop [this]
     (reset! storage empty-space)
    this)

storage-client/StorageClient
  (read-all [_this] @storage)
  (put! [_this update-fn] (swap! storage update-fn))
  (clear-all! [_this] (reset! storage empty-space)))

(defn new-in-memory []
  (->InMemory (atom empty-space)))