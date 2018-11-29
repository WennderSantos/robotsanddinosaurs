(ns robotsandinosaurs.logic
  (:require [clojure.string :as str]))

(defn new-robot [x y face-direction id]
  {id {:id id
       :coord {:x x :y y}
       :face-direction face-direction}})

(defn new-dinosaur [x y id]
  {id {:id id
       :coord {:x x :y y}}})

(defn- coord-x-1 [coord] (update coord :x dec))

(defn- coord-x+1 [coord] (update coord :x inc))

(defn- coord-y-1 [coord] (update coord :y dec))

(defn- coord-y+1 [coord] (update coord :y inc))

(defn move [where coord face-direction]
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

(defn contains-coord? [coord coords]
  (some #(= coord %) coords))

(defn robot-attack [robot-coord dinosaurs]
  (->> (conj #{}
             (coord-x-1 robot-coord)
             (coord-x+1 robot-coord)
             (coord-y-1 robot-coord)
             (coord-y+1 robot-coord))
       ((fn [coords-around-robot]
         (filter #(contains-coord? (:coord %) coords-around-robot)
                 (vals dinosaurs))))
       (map #(:id %))))

(defn turn-face-direction [side-to-turn directions face-direction]
  (if (= side-to-turn :right)
    (turn-face-direction :left (reverse directions) face-direction)
    (->> (conj directions (last directions))
         (partition 2 1)
         (filter (fn [[current next]]
                  (= next face-direction)))
         (flatten)
         (first))))
