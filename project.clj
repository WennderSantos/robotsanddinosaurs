(defproject robotsandinosaurs "1.0"
  :description "Robots and dinosaurs"
  :url "https://robotsanddinosaurs.herokuapp.com/"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring "1.7.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [metosin/compojure-api "1.1.11"]]
  :uberjar-name "robots-and-dinosaurs.jar"
  :main ^{:skip-aot true} robotsandinosaurs.server
  :profiles {:dev
             {:dependencies [[midje "1.9.2"]
                             [javax.servlet/javax.servlet-api "3.1.0"]
                             [ring/ring-mock "0.3.2"]
                             [prismatic/schema "1.1.7"]]
              :plugins [[lein-midje "3.2.1"]
                        [lein-cljfmt "0.6.0"]
                        [lein-cloverage "1.0.13"]
                        [lein-ring "0.12.0"]]}
             :uberjar {:aot [robotsandinosaurs.server]}})
