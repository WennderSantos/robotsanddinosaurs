(defproject robotsandinosaurs "1.0"
  :description "Robots and dinosaurs"
  :url "https://robotsanddinosaurs.herokuapp.com/"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring "1.7.0"]
                 [metosin/compojure-api "1.1.11"]]
  :uberjar-name "robots-and-dinosaurs.jar"
  :ring {:handler robotsandinosaurs.api.handler/app}
  :main ^{:skip-aot true} robotsandinosaurs.api.handler
  :profiles {:dev
             {:dependencies [[midje "1.9.2"]
                             [javax.servlet/javax.servlet-api "3.1.0"]
                             [ring/ring-mock "0.3.2"]]
              :plugins [[lein-midje "3.2.1"]
                        [lein-cljfmt "0.6.0"]
                        [lein-cloverage "1.0.13"]
                        [lein-ring "0.12.0"]]}
             :uberjar {:aot [robotsandinosaurs.api.handler]}})
