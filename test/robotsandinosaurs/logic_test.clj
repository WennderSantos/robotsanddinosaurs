(ns robotsandinosaurs.logic-test
  (:use midje.sweet)
  (:require [robotsandinosaurs.logic :refer :all]))

(fact "start-the-game should start the game with an empty space"
      (start-the-game!) => empty-space)

(against-background
 [(before :facts (start-the-game!))]

 (fact "Should be able to get the current state"
       (fact "when space is empty"
             (get-current-space) => empty-space)
       (fact "when space is not empty"
             (let [new-space (create-a-creature {:creature :dinosaur :current-space empty-space :coord "5:5"})]
               (update-space! new-space) => new-space
               (get-current-space) => new-space))))

(fact "Should be able to create"
      (fact "a robot in an empty space and face its direction"
            (create-a-creature {:creature :robot :current-space empty-space :coord "1:2" :face-direction "upwards"}) => {:dinosaur #{} :robot {"1:2" "upwards"}})
      (fact "a robot in an already populated space and face its direction"
            (let [current-space {:dinosaur #{} :robot {"1:2" "upwards"}}]
              (create-a-creature {:creature :robot :current-space current-space :coord "5:5" :face-direction "left"}) => {:dinosaur #{} :robot {"1:2" "upwards" "5:5" "left"}}))
      (fact "a dinosaur in an empty space"
            (create-a-creature {:creature :dinosaur :current-space empty-space :coord "3:2"}) => {:dinosaur #{"3:2"} :robot {}})
      (fact "a dinosaur in an already populated space"
            (let [current-space {:dinosaur #{} :robot {"3:2" "left"}}]
              (create-a-creature {:creature :dinosaur :current-space current-space :coord "10:20"}) => {:dinosaur #{"10:20"} :robot {"3:2" "left"}})))

(fact "Should be able to remove"
      (let [current-space {:dinosaur #{"6:5"} :robot {"2:2" "upwards"}}]
        (fact "a robot at 2:2"
              (remove-creature {:creature :robot :current-space current-space :coord "2:2"}) => {:dinosaur #{"6:5"} :robot {}})
        (fact "a dinosaur at 6:5"
              (remove-creature {:creature :dinosaur :current-space current-space :coord "6:5"}) => {:dinosaur #{} :robot {"2:2" "upwards"}})))

(fact "Should be able to convert a string cord in a map"
      (coord-into-map "11:11") => {:x 11 :y 11}
      (coord-into-map "2:3") => {:x 2 :y 3})

(fact "Should return a new coord based on a robot instruction"
      (fact "turn left"
            (fact "facing upwards"
                  (robot-instruction {:move :turn-left :face-direction "upwards" :current-coord {:x 4 :y 5}}) => "3:5")
            (fact "facing backwards"
                  (robot-instruction {:move :turn-left :face-direction "backwards" :current-coord {:x 4 :y 5}}) => "5:5")
            (fact "facing left"
                  (robot-instruction {:move :turn-left :face-direction "left" :current-coord {:x 4 :y 5}}) => "4:4")
            (fact "facing right"
                  (robot-instruction {:move :turn-left :face-direction "right" :current-coord {:x 4 :y 5}}) => "4:6"))
      (fact "turn right"
            (fact "facing upwards"
                  (robot-instruction {:move :turn-right :face-direction "upwards" :current-coord {:x 10 :y 15}}) => "11:15")
            (fact "facing backwards"
                  (robot-instruction {:move :turn-right :face-direction "backwards" :current-coord {:x 10 :y 15}}) => "9:15")
            (fact "facing left"
                  (robot-instruction {:move :turn-right :face-direction "left" :current-coord {:x 10 :y 15}}) => "10:16")
            (fact "facing right"
                  (robot-instruction {:move :turn-right :face-direction "right" :current-coord {:x 10 :y 15}}) => "10:14"))
      (fact "move back"
            (fact "facing upwards"
                  (robot-instruction {:move :move-back :face-direction "upwards" :current-coord {:x 8 :y 23}}) => "8:22")
            (fact "facing backwards"
                  (robot-instruction {:move :move-back :face-direction "backwards" :current-coord {:x 8 :y 23}}) => "8:24")
            (fact "facing left"
                  (robot-instruction {:move :move-back :face-direction "left" :current-coord {:x 8 :y 23}}) => "9:23")
            (fact "facing right"
                  (robot-instruction {:move :move-back :face-direction "right" :current-coord {:x 8 :y 23}}) => "7:23"))
      (fact "move forward"
            (fact "facing upwards"
                  (robot-instruction {:move :move-forward :face-direction "upwards" :current-coord {:x 31 :y 40}}) => "31:41")
            (fact "facing backwards"
                  (robot-instruction {:move :move-forward :face-direction "backwards" :current-coord {:x 31 :y 40}}) => "31:39")
            (fact "facing left"
                  (robot-instruction {:move :move-forward :face-direction "left" :current-coord {:x 31 :y 40}}) => "30:40")
            (fact "facing right"
                  (robot-instruction {:move :move-forward :face-direction "right" :current-coord {:x 31 :y 40}}) => "32:40")))

(fact "Should be able to move a robot"
      (fact "to the left"
            (fact "when its current coord is 2:2 and its face is upwards"
                  (move-a-robot {:direction :turn-left :current-coord "2:2" :face-direction "left" :current-space {:robot {"2:2" "upwards"} :dinosaur #{}}}) => {:robot {"1:2" "left"} :dinosaur #{}})
            (fact "when its current coord is 2:2 and its face is backwards"
                  (move-a-robot {:direction :turn-left :current-coord "2:2" :face-direction "backwards" :current-space {:robot {"2:2" "backwards"} :dinosaur #{}}}) => {:robot {"3:2" "backwards"} :dinosaur #{}})
            (fact "when its current coord is 2:2 and its face is left"
                  (move-a-robot {:direction :turn-left :current-coord "2:2" :face-direction "left" :current-space {:robot {"2:2" "left"} :dinosaur #{}}}) => {:robot {"2:1" "left"} :dinosaur #{}})
            (fact "when its current coord is 2:2 and its face is right"
                  (move-a-robot {:direction :turn-left :current-coord "2:2" :face-direction "right" :current-space {:robot {"2:2" "right"} :dinosaur #{}}}) => {:robot {"2:3" "right"} :dinosaur #{}}))
      (fact "to the right"
            (fact "when its current coord is 4:9 and its face is upwards"
                  (move-a-robot {:direction :turn-right :current-coord "4:9" :face-direction "upwards" :current-space {:robot {"4:9" "upwards"} :dinosaur #{}}}) => {:robot {"5:9" "upwards"} :dinosaur #{}})
            (fact "when its current coord is 4:9 and its face is backwards"
                  (move-a-robot {:direction :turn-right :current-coord "4:9" :face-direction "right" :current-space {:robot {"4:9" "backwards"} :dinosaur #{}}}) => {:robot {"3:9" "right"} :dinosaur #{}})
            (fact "when its current coord is 4:9 and its face is left"
                  (move-a-robot {:direction :turn-right :current-coord "4:9" :face-direction "left" :current-space {:robot {"4:9" "left"} :dinosaur #{}}}) => {:robot {"4:10" "left"} :dinosaur #{}})
            (fact "when its current coord is 4:9 and its face is right"
                  (move-a-robot {:direction :turn-right :current-coord "4:9" :face-direction "right" :current-space {:robot {"4:9" "right"} :dinosaur #{}}}) => {:robot {"4:8" "right"} :dinosaur #{}}))
      (fact "forward"
            (fact "when its current coord is 15:21 and its face is upwards"
                  (move-a-robot {:direction :move-forward :current-coord "15:21" :face-direction "upwards" :current-space {:robot {"15:21" "upwards"} :dinosaur #{}}}) => {:robot {"15:22" "upwards"} :dinosaur #{}})
            (fact "when its current coord is 15:21 and its face is backwards"
                  (move-a-robot {:direction :move-forward :current-coord "15:21" :face-direction "backwards" :current-space {:robot {"15:21" "backwards"} :dinosaur #{}}}) => {:robot {"15:20" "backwards"} :dinosaur #{}})
            (fact "when its current coord is 15:21 and its face is left"
                  (move-a-robot {:direction :move-forward :current-coord "15:21" :face-direction "left" :current-space {:robot {"15:21" "left"} :dinosaur #{}}}) => {:robot {"14:21" "left"} :dinosaur #{}})
            (fact "when its current coord is 15:21 and its face is right"
                  (move-a-robot {:direction :move-forward :current-coord "15:21" :face-direction "right" :current-space {:robot {"15:21" "right"} :dinosaur #{}}}) => {:robot {"16:21" "right"} :dinosaur #{}}))
      (fact "back"
            (fact "when its current coord is 39:40 and its face is upwards"
                  (move-a-robot {:direction :move-back :current-coord "39:40" :face-direction "upwards" :current-space {:robot {"39:40" "upwards"} :dinosaur #{}}}) => {:robot {"39:39" "upwards"} :dinosaur #{}})
            (fact "when its current coord is 39:40 and its face is backwards"
                  (move-a-robot {:direction :move-back :current-coord "39:40" :face-direction "backwards" :current-space {:robot {"39:40" "backwards"} :dinosaur #{}}}) => {:robot {"39:41" "backwards"} :dinosaur #{}})
            (fact "when its current coord is 39:40 and its face is left"
                  (move-a-robot {:direction :move-back :current-coord "39:40" :face-direction "left" :current-space {:robot {"39:40" "left"} :dinosaur #{}}}) => {:robot {"40:40" "left"} :dinosaur #{}})
            (fact "when its current coord is 39:40 and its face is right"
                  (move-a-robot {:direction :move-back :current-coord "39:40" :face-direction "right" :current-space {:robot {"39:40" "right"} :dinosaur #{}}}) => {:robot {"38:40" "right"} :dinosaur #{}})))

(fact "Should be able to know all the coords around a coord"
      (#'robotsandinosaurs.logic/coords-around-this {:x 5 :y 4}) => #{"6:4" "5:5" "4:4" "5:3"})

(fact "In a robot attack, dinosaurs around it should be destroyed"
      (let [current-space {:dinosaur #{"20:30" "5:2" "4:3"} :robot {"4:2" "right"}}]
        (robot-attack "4:2" current-space) => {:dinosaur #{"20:30"} :robot {"4:2" "right"}}))

(fact "Creatures should not occupy the same coord"
      (fact "If a coord is being used by a robot, it should not be available"
            (is-this-coord-available-in-space?  {:coord "2:2" :current-space {:robot {"2:2" "left"} :dinosaur #{"3:4"}}}) => false)
      (fact "If a coord is being used by a dinosaur, it should not be available"
            (is-this-coord-available-in-space?  {:coord "3:4" :current-space {:robot {"2:2" "left"} :dinosaur #{"3:4"}}}) => false)
      (fact "Coord should be available"
            (is-this-coord-available-in-space?  {:coord "10:20" :current-space {:robot {"2:2" "left"} :dinosaur #{"3:4"}}}) => true))

(fact "Should be able to validate if a robot exist in space"
      (fact "when exists"
            (is-this-robot-exist-in-space? {:coord "2:2" :current-space {:robot {"2:2" "left"} :dinosaur #{}}}) => true)
      (fact "when doesn't exist"
            (is-this-robot-exist-in-space? {:coord "2:2" :current-space {:robot {"3:3" "left"} :dinosaur #{}}}) => false))

(fact "Parse a coord-map into a string"
      (coord-into-string {:x 4 :y 3}) => "4:3")