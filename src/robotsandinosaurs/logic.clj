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

(defn move-robot [where coord face-direction]
  (let [instruction [where face-direction]]
    (cond
      (some #(= % instruction) '([:move-forward :west]
                                 [:move-backwards :east])) (coord-x-1 coord)
      (some #(= % instruction) '([:move-forward :east]
                                 [:move-backwards :west])) (coord-x+1 coord)
      (some #(= % instruction) '([:move-forward :south]
                                 [:move-backwards :north])) (coord-y-1 coord)
      (some #(= % instruction) '([:move-forward :north]
                                 [:move-backwards :south])) (coord-y+1 coord))))

(defn- contains-coord? [coords coord]
  (some #(= coord %) coords))

(defn robot-attack [robot-coord dinosaurs]
  (let [coords-around-robot (-> #{}
                                (conj (coord-x-1 robot-coord))
                                (conj (coord-x+1 robot-coord))
                                (conj (coord-y-1 robot-coord))
                                (conj (coord-y+1 robot-coord)))]
    (loop [all-dinosaurs dinosaurs
           ids-to-remove (map #(:id %)
                              (filter #(contains-coord? coords-around-robot
                                                        (:coord %))
                                      (vals dinosaurs)))]
      (if (empty? ids-to-remove)
        all-dinosaurs
        (recur (dissoc all-dinosaurs (first ids-to-remove))
               (rest ids-to-remove))))))

(defn turn-face-direction [face-direction directions side-to-turn]
  (if (= side-to-turn :right)
    (turn-face-direction face-direction (reverse directions) :left)
    (loop [directions directions left-side (last directions)]
      (if (= (first directions) face-direction)
        left-side
        (recur (rest directions) (first directions))))))
