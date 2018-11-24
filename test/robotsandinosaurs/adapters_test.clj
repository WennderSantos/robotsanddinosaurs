(ns robotsandinosaurs.adapters-test
  (:require [robotsandinosaurs.logic :as logic]
						[midje.sweet :refer :all]
						[robotsandinosaurs.adapters :as adapters])
	(:import [java.util UUID]))

(fact "Convert uuid into string"
  (let [uuid (UUID/randomUUID)]
    (adapters/uuid->string uuid) => (str uuid)))
