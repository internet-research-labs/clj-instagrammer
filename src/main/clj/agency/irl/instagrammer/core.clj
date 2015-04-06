(ns agency.irl.instagrammer.core
  (:require [agency.irl.instagrammer.respond :as respond]
            [clostache.parser :refer [render-resource]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [org.httpkit.server :refer :all]
            [org.httpkit.timer :refer :all]
            [ring.util.response :refer :all]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))


(defn handle-websocket
  [request]
  (with-channel request channel
    (println (websocket? channel))
    (if (websocket? channel)
      (on-receive channel (fn [data]
                            (send! channel data)))
      (send! channel {:status 200
                      :headers {"Content-Type" "text/plain"}
                      :body    "Long polling?"}))))


(defroutes main-routes
    (GET "/" req (render-resource "templates/index.html"))
    (GET "/ws" req (handle-websocket req))
    (route/not-found "Page not found"))


(def app
  (-> main-routes
      (handler/site)))


(defn -main
  "I don't do a whole lot."
  [& args]
  (run-server app {:port 3000})
  (println "running 💆a "))
