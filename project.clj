(defproject agency.irl/instagrammer "0.0.1-SNAPSHOT"
  :description "INstagrammer"
  :url "http://irl.agency/"
  :license {:name "UNLICENSE" :url "http://unlicense.org/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clansi "1.0.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [org.clojure/tools.cli "0.3.1"]

                 [compojure "1.3.3"]
                 [http-kit "2.1.16"]
                 [clj-http "1.1.0"]
                 [instagram-api "0.1.8"]
                 [cheshire "5.4.0"]
                 [environ "1.0.0"]]
  :min-lein-version "2.0.0"
  :uberjar-name "instagrammer-standalone.jar"
  :main agency.irl.instagrammer.core
  :source-paths   ["src/main/clj"]
  :test-paths     ["src/test/clj"]
  :resource-paths ["src/main/resources"]
  :java-source-paths ["src/main/java"]
  :plugins [[lein-ring "0.9.3"]]
  :ring {:handler agency.irl.instagrammer.core/app}
  :profiles {:production {:env {:production true}}})
