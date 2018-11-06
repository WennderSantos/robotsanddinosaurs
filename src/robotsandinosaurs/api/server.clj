(ns robotsandinosaurs.api.server
  (:require [ring.adapter.jetty :refer :all]
            [robotsandinosaurs.api.controller :refer [app]])
  (:gen-class))

(defn -main []
  (run-jetty app {:port (Integer. (or (System/getenv "PORT") "8080"))
                  :join? false}))
