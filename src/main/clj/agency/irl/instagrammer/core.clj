(ns agency.irl.instagrammer.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response])
  (:gen-class))


(defroutes main-routes
    (GET "/" [] "Hellow")
    (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)))


(defn -main
  "I don't do a whole lot."
  [& args]
  (println args "Hello, World!"))
