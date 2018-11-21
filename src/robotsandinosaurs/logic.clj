(ns robotsandinosaurs.logic
  (:require [clojure.string :as str]))

(defmulti create-a-creature (fn [args] (args :creature)))

(defmethod create-a-creature :robot [{:keys [creature current-space coord face-direction]}]
  (assoc-in current-space [creature coord] face-direction))

(defmethod create-a-creature :dinosaur [{:keys [creature current-space coord]}]
  (update-in current-space [creature] conj coord))

(defn coord-into-map [coord]
  {:x (Integer. (first (str/split coord #":"))) :y (Integer. (last (str/split coord #":")))})

(defn coord-into-string [{:keys [x y]}]
  (str x ":" y))

(defmulti remove-creature (fn [at] (at :creature)))

(defmethod remove-creature :robot [{:keys [creature current-space coord]}]
  (update current-space creature dissoc coord))

(defmethod remove-creature :dinosaur [{:keys [creature current-space coord]}]
  (update current-space creature disj coord))

(defn- coord-x-1 [coord]
  (coord-into-string (update coord :x dec)))

(defn- coord-x+1 [coord]
  (coord-into-string (update coord :x inc)))

(defn- coord-y-1 [coord]
  (coord-into-string (update coord :y dec)))

(defn- coord-y+1 [coord]
  (coord-into-string (update coord :y inc)))

(defmulti robot-instruction
  (fn [instruction]
    (let [movement [(:move instruction) (:face-direction instruction)]]
      (cond
        (some #(= % movement) '([:turn-left "upwards"] [:turn-right "backwards"] [:move-back "right"] [:move-forward "left"])) :x-1
        (some #(= % movement) '([:turn-right "upwards"] [:turn-left "backwards"] [:move-back "left"] [:move-forward "right"])) :x+1
        (some #(= % movement) '([:turn-left "left"] [:turn-right "right"] [:move-back "upwards"] [:move-forward "backwards"])) :y-1
        (some #(= % movement) '([:turn-right "left"] [:turn-left "right"] [:move-back "backwards"] [:move-forward "upwards"])) :y+1))))

(defmethod robot-instruction :x-1 [{:keys [current-coord]}]
  (coord-x-1 current-coord))

(defmethod robot-instruction :x+1 [{:keys [current-coord]}]
  (coord-x+1 current-coord))

(defmethod robot-instruction :y-1 [{:keys [current-coord]}]
  (coord-y-1 current-coord))

(defmethod robot-instruction :y+1 [{:keys [current-coord]}]
  (coord-y+1 current-coord))

(defn move-a-robot [{:keys [direction current-coord face-direction current-space]}]
  (let [current-face-direction (get-in current-space [:robot current-coord])
        new-coord (robot-instruction {:move direction :face-direction current-face-direction :current-coord (coord-into-map current-coord)})
        space-without-the-robot (remove-creature {:creature :robot :current-space current-space :coord current-coord})
        new-space (create-a-creature {:creature :robot :current-space space-without-the-robot :coord new-coord :face-direction face-direction})]
    new-space))

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
