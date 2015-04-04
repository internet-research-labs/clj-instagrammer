(defproject agency.irl/instagrammer "0.0.1-SNAPSHOT"
  :description "INstagrammer"
  :url "http://irl.agency/"
  :license {:name "UNLICENSE" :url "http://unlicense.org/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.3"]
                 [http-kit "2.1.16"]]
  :main agency.irl.instagrammer.core
  :source-paths ["src/main/clj"]
  :test-paths   ["src/test/clj"]
  :java-source-paths ["src/main/java"]
  :plugins [[lein-ring "0.9.3"]]
  :ring {:handler agency.irl.instagrammer.core/app}
  )
