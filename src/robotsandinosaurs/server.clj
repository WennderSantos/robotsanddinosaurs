(ns robotsandinosaurs.server
  (:gen-class)
  (:require [robotsandinosaurs.system :as system]))

(defn -main []
  (system/build-and-start))