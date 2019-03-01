(ns robotsandinosaurs.logic
  "Business logic"
  (:require [clojure.string :as str]
            [robotsandinosaurs.schemas :as schemas]))

(defn new-robot
  "Returns a map which the `id` is its key.
  This makes easy to retrive a robot by its id
  using the `get` function"
  [x y face-direction id]
  {id {:id id
       :coord {:x x :y y}
       :face-direction face-direction}})

(defn new-dinosaur
  "Returns a map which the `id` is its key.
  This makes easy to retrive a dinosaur by its id
  using the `get` function"
  [x y id]
  {id {:id id
       :coord {:x x :y y}}})

(defn- coord-x-1 [coord] (update coord :x dec))

(defn- coord-x+1 [coord] (update coord :x inc))

(defn- coord-y-1 [coord] (update coord :y dec))

(defn- coord-y+1 [coord] (update coord :y inc))

(defn move
  "Returns a coord.
  Move a robot based on its coord and face direction.
  The combination of a coord and a face direction will
  result in a instruction like: move forward when facing north.
  each of these possible combinations is a key to apply a function f to
  the robot coord which will return its new coord"
  [where coord face-direction]
  (-> {:move-forward {:west #(coord-x-1 coord)
                      :east #(coord-x+1 coord)
                      :south #(coord-y-1 coord)
                      :north #(coord-y+1 coord)}
       :move-backwards {:west #(coord-x+1 coord)
                       :east #(coord-x-1 coord)
                       :south #(coord-y+1 coord)
                       :north #(coord-y-1 coord)}}
      ((fn [instructions]
        ((get-in instructions [where face-direction]))))))

(defn contains-coord?
  "Returns true or false
  Validate if a coord is inside a sequence of coords"
  [coord coords]
  (some #(= coord %) coords))

(defn robot-attack
  "Returns a sequence containing the ids of the
  killed dinosaurs.
  Given a robot coord, will calculate the coords around it
  and filter dinosaurs coord in space which matches these
  coords around the robot."
  [robot-coord dinosaurs]
  (->> (conj #{}
             (coord-x-1 robot-coord)
             (coord-x+1 robot-coord)
             (coord-y-1 robot-coord)
             (coord-y+1 robot-coord))
       ((fn [coords-around-robot]
         (filter #(contains-coord? (:coord %) coords-around-robot)
                 (vals dinosaurs))))
       (map #(:id %))))

(defn turn-face-direction
  "Returns a face direction.
  Robots can turn-left or turn-right and these turns will
  result in a new face direction based on its current
  face direction."
  [side-to-turn current-face-direction]
  (-> schemas/turns
      (get side-to-turn)
      (get current-face-direction)))
