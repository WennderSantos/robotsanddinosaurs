(ns robotsandinosaurs.api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [robotsandinosaurs.core :as core]
            [ring.adapter.jetty :refer :all])
  (:gen-class))

(def X (s/constrained s/Int #(and (>= % 0) (< % (get-in core/grid [:lenght :x])))))
(def Y (s/constrained s/Int #(and (>= % 0) (< % (get-in core/grid [:lenght :x])))))
(def Coord {:x X :y Y})
(def Robot-face-direction (s/enum "upwards" "backwards" "left" "right"))
(def Robot-movement-instruction (s/enum :turn-left :turn-right :move-forward :move-back))

(s/defschema Space {:robots [{:coord Coord :face-direction Robot-face-direction}]
                    :dinosaurs [{:coord Coord}]})

(s/defschema New-robot {:coord Coord :face-direction Robot-face-direction})
(s/defschema Robot-move {:coord Coord
                         :movement-instruction Robot-movement-instruction
                         :new-face-direction Robot-face-direction})

(defn parse-space-into-objects [current-space]
  (let [dinosaurs (map #(into {} {:coord (core/coord-into-map %)}) (:dinosaur current-space))
        robots (map #(into {} {:coord (core/coord-into-map (first %)) :face-direction (last %)}) (:robot current-space))]
    (-> {}
        (assoc :robots robots)
        (assoc :dinosaurs dinosaurs))))

(def app
  (api
   {:swagger
    {:ui "/api-docs"
     :spec "/swagger.json"
     :data {:info {:title "Robots and dinosaurs"
                   :description ""}
            :tags [{:name "space", :description "Everything about /space"}
                   {:name "dinosaur", :description "Everything about /dinosaur"}
                   {:name "robot", :description "Everything about /robot"}]}}}

   (GET "/" []
     (ok "<div style='text-align: center; padding: 1%;'>
            <h1>Hello World!</h1>
            </br>
            <a href='/api-docs'>Go to the api docs</a>
          </div>"))

   (context "/space" []
     :tags ["space"]

     (POST "/restart" []
       :return Space
       :summary "Restart the simulation space to its initial state. Current simulation space will be lost."
       (ok
        (-> (core/start-the-game!)
            (parse-space-into-objects)))) (GET "/" []
                                            :return Space
                                            :summary "Get the current state of the simulation space"
                                            (ok
                                             (-> (core/get-current-space)
                                                 (parse-space-into-objects)))))

   (context "/dinosaur" []
     :tags ["dinosaur"]

     (POST "/" []
       :return Space
       :body [dinosaur Coord]
       :summary "Creates a dinosaur by informing its coordination in the simulation space"
       (let [coord (core/coord-into-string dinosaur)
             current-space (core/get-current-space)]
         (if (core/is-this-coord-available-in-space? {:coord coord :current-space current-space})
           (ok
            (-> (core/update-space! (core/create-a-creature {:creature :dinosaur :current-space current-space :coord coord}))
                (parse-space-into-objects)))
           (bad-request (str "Trying to create a dinosaur in a coordination already occupied " coord))))))

   (context "/robot" []
     :tags ["robot"]

     (POST "/" []
       :return Space
       :body [robot New-robot]
       :summary "Creates a robot by informing a coordination in space and where its face should be directed"
       (let [coord (core/coord-into-string (:coord robot))
             current-space (core/get-current-space)]
         (if (core/is-this-coord-available-in-space? {:coord coord :current-space current-space})
           (ok
            (-> (core/update-space! (core/create-a-creature {:creature :robot :current-space current-space :coord coord :face-direction (:face-direction robot)}))
                (parse-space-into-objects)))
           (bad-request (str "Trying to create a robot in a coordination already occupied " coord)))))

     (POST "/attack" []
       :return Space
       :body [attack Coord]
       :summary "Attack by informing the coordination of the robot to use in this attack"
       (let [coord (core/coord-into-string attack)
             current-space (core/get-current-space)]
         (if (core/is-this-robot-exist-in-space? {:coord coord :current-space current-space})
           (ok
            (-> (core/update-space! (core/robot-attack coord current-space))
                (parse-space-into-objects)))
           (not-found (str "There is no robot at " coord)))))

     (POST "/move" []
       :return Space
       :body [movement Robot-move]
       :summary "Move a robot by informing its coordination, instruction for movement and a face direction"
       (let [current-coord-in-string-format (core/coord-into-string (:coord movement))
             current-coord-in-map-format (:coord movement)
             current-space (core/get-current-space)
             movement-instruction (:movement-instruction movement)]
         (if (core/is-this-robot-exist-in-space? {:coord current-coord-in-string-format :current-space current-space})
           (let [current-face-direction (get-in current-space [:robot current-coord-in-string-format])
                 coord-to-move (core/robot-instruction {:move movement-instruction :face-direction current-face-direction :current-coord current-coord-in-map-format})]
             (if (core/is-this-coord-available-in-space? {:coord coord-to-move :current-space current-space})
               (ok
                (-> (core/update-space! (core/move-a-robot {:direction movement-instruction :current-coord current-coord-in-string-format :face-direction (:new-face-direction movement) :current-space current-space}))
                    (parse-space-into-objects)))
               (bad-request (str "Trying to move to a coordination already occupied " (core/coord-into-map coord-to-move)))))
           (not-found (str "There is no robot at " current-coord-in-string-format))))))))

(defn -main []
  (run-jetty app {:port (Integer. (or (System/getenv "PORT") "8080"))
                  :join? false}))