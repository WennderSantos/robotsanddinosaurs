(ns robotsandinosaurs.server
  (:gen-class)
  (:require [robotsandinosaurs.system :as system]))

(defn -main
  "The entry-point for 'lein run'"
  []
  (system/build-and-start))