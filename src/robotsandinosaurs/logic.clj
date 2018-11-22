(ns robotsandinosaurs.logic
  (:require [clojure.string :as str]))

(defn new-robot [x y face-direction id]
  {id {:id id :coord {:x x :y y} :face-direction face-direction}})

(defn new-dinosaur [x y id]
  {id {:id id :coord {:x x :y y}}})

(defn coord-into-map [coord]
  {:x (Integer. (first (str/split coord #":"))) :y (Integer. (last (str/split coord #":")))})

(defn coord-into-string [{:keys [x y]}]
  (str x ":" y))

(defn- coord-x-1 [coord]
  (coord-into-string (update coord :x dec)))

(defn- coord-x+1 [coord]
  (coord-into-string (update coord :x inc)))

(defn- coord-y-1 [coord]
  (coord-into-string (update coord :y dec)))

(defn- coord-y+1 [coord]
  (coord-into-string (update coord :y inc)))

(defn move-robot [where coord face-direction]
  (let [instruction [where face-direction]]
    (cond
      (some #(= % instruction) '([:move-forward "west"] [:move-backwards "east"])) (coord-x-1 coord)
      (some #(= % instruction) '([:move-forward "east"] [:move-backwards "west"])) (coord-x+1 coord)
      (some #(= % instruction) '([:move-forward "south"] [:move-backwards "north"])) (coord-y-1 coord)
      (some #(= % instruction) '([:move-forward "north"] [:move-backwards "south"])) (coord-y+1 coord))))

(defn- coords-around-this [coord]
  (-> #{}
      (conj (coord-x-1 coord))
      (conj (coord-x+1 coord))
      (conj (coord-y-1 coord))
      (conj (coord-y+1 coord))))

(defn robot-attack [map-robot-coord dinosaurs]
  (loop [dinosaurs dinosaurs coords-around (coords-around-this map-robot-coord)]
    (if (empty? coords-around)
      dinosaurs
      (recur (disj dinosaurs (first coords-around))
             (rest coords-around)))))

(defn is-this-robot-exist-in-space? [{:keys [coord current-space]}]
  (true? (some #(= coord %) (keys (:robot current-space)))))

(defn is-this-coord-available-in-space? [{:keys [coord current-space]}]
  (let [coords-being-used-by-robots (keys (:robot current-space))
        coords-being-used-by-dinosaurs (:dinosaur current-space)
        coords-being-used (concat coords-being-used-by-robots coords-being-used-by-dinosaurs)]
    (not (some #(= coord %) coords-being-used))))

(defn turn-robot-face [face-direction directions direction-to-turn]
  (if (= direction-to-turn :right)
    (turn-robot-face face-direction (reverse directions) :left)
    (loop [directions directions left-side (last directions)]
      (if (= (first directions) face-direction)
        left-side
        (recur (rest directions) (first directions))))))
