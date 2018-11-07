(ns robotsandinosaurs.server
  (:require [ring.adapter.jetty :refer :all]
            [robotsandinosaurs.controller :refer [app]])
  (:gen-class))

(defn -main []
  (run-jetty app {:port (Integer. (or (System/getenv "PORT") "8080"))
                  :join? false}))
