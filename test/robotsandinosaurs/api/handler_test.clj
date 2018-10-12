(ns robotsandinosaurs.api.handler-test
  (:use midje.sweet)
  (:require [cheshire.core :as cheshire]
            [robotsandinosaurs.api.handler :refer :all]
            [ring.mock.request :as mock]
            [robotsandinosaurs.core :refer :all]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(fact "Parse space into objects"
      (let [space {:dinosaur #{"6:5" "7:8"} :robot {"2:2" "upwards" "10:10" "left"}}
            dinosaurs (map #(into {} {:coord (coord-into-map %)}) (:dinosaur space))
            robots (map #(into {} {:coord (coord-into-map (first %)) :face-direction (last %)}) (:robot space))]
        (-> {}
            (assoc :robots robots)
            (assoc :dinosaurs dinosaurs)) => {:dinosaurs '({:coord {:x 7 :y 8}} {:coord {:x 6 :y 5}})
                                              :robots '({:coord {:x 2 :y 2} :face-direction "upwards"} {:coord {:x 10 :y 10} :face-direction "left"})}))

(against-background
 [(before :facts (start-the-game!))]

 (facts "/space"
        (fact "GET /space"
              (let [response (app (mock/request :get "/space"))
                    body     (parse-body (:body response))]
                (:status response) => 200))

        (fact "POST /space/restart"
              (let [response (app (mock/request :post "/space/restart"))
                    body     (parse-body (:body response))]
                (:status response) => 200)))

 (facts "/dinosaur"
        (fact "POST /dinosaur should return 200"
              (let [dinosaur {:x 1 :y 1}
                    response (app (-> (mock/request :post "/dinosaur")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string dinosaur))))]
                (:status response) => 200))

        (fact "POST /dinosaur with an invalid coordination should return 400"
              (let [dinosaur {:x 50 :y 1}
                    response (app (-> (mock/request :post "/dinosaur")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string dinosaur))))]
                (:status response) => 400)))

 (facts "/robot"
        (fact "POST /robot should return 200"
              (let [robot {:coord {:x 2 :y 1}
                           :face-direction "left"}
                    response (app (-> (mock/request :post "/robot")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string robot))))]
                (:status response) => 200))

        (fact "POST /robot with an invalid coordination should return 400"
              (let [robot {:coord {:x "1" :y 1}
                           :face-direction "left"}
                    response (app (-> (mock/request :post "/robot")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string robot))))]
                (:status response) => 400))

        (fact "POST /robot/move should reutrn 200"
              (update-space! (create-a-creature {:creature :robot :current-space empty-space :coord "0:1" :face-direction "left"}))
              (let [robot {:coord {:x 0 :y 1}
                           :new-face-direction "upwards"
                           :movement-instruction :turn-right}
                    response (app (-> (mock/request :post "/robot/move")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string robot))))]
                (:status response) => 200))

        (fact "POST /robot/move with a robot that does not exist should return 404"
              (let [robot {:coord {:x 1 :y 1}
                           :new-face-direction "upwards"
                           :movement-instruction :turn-right}
                    response (app (-> (mock/request :post "/robot/move")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string robot))))]
                (:status response) => 404))

        (fact "POST /robot/attack should return 200"
              (update-space! (create-a-creature {:creature :robot :current-space empty-space :coord "0:1" :face-direction "left"}))
              (let [coord {:x 0 :y 1}
                    response (app (-> (mock/request :post "/robot/attack")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string coord))))]
                (:status response) => 200))

        (fact "POST /robot/attack with a robot that does not exist should return 404"
              (let [coord {:x 0 :y 1}
                    response (app (-> (mock/request :post "/robot/attack")
                                      (mock/content-type "application/json")
                                      (mock/body (cheshire/generate-string coord))))]
                (:status response) => 404))))