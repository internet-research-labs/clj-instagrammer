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


(def ^:private client-options
  {:client-id     ""
   :client-secret ""
   :callback-url  ""
   :redirect-uri  ""})


(defn handle-websocket
  "Handle requests for WebSockets"
  [request]
  (with-channel request channel
    (if (websocket? channel)
      (on-receive channel (fn [data] (send! channel data)))
      (send! channel {:status 200
                      :headers {"Content-Type" "text/plain"}
                      :body    "Long polling?"}))))


(defn instagram-handler
  "Routes Instagram-like traffic"
  [req]


(defroutes main-routes
    (GET  "/"    req (render-resource "templates/index.html"))
    (GET  "/ws"  []  handle-websocket)
    (GET  "/callback-url" [] handle-subscription)
    (POST "/callback-url" [] handle-new-media)
    (route/not-found "Page not found"))


(def app
  (-> main-routes
      (handler/site)))


(defn -main
  [& args]

      
  (run-server (-> app (instagram/attach geo-sub tag-sub) (handler/site)) {:port 3000})

  (with-instagram client-options
    (let [geo-sub (subscribe/geo :lng 0 :lat 0 :radius 100)
          tag-sub (subscribe/tag :tag "yolo")
          apppppp (-> app (instagram/attach geo-sub tag-sub))]

      ))

  (println "running"))
