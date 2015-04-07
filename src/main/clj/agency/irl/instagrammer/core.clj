(ns agency.irl.instagrammer.core
  (:require [environ.core :refer [env]]
            [agency.irl.instagrammer.response :as insta-response]
            [agency.irl.instagrammer.subscribe :as subscribe]
            [clostache.parser :refer [render-resource]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [org.httpkit.server :refer :all]
            [org.httpkit.timer :refer :all]
            [ring.util.response :refer :all]
            [clansi :refer [style]]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))


(def ^:private client-options
  {:client-id     (env :ig-client-id)
   :client-secret (env :ig-client-secret)
   :redirect-uri  (env :ig-redirect-uri)
   :website-url   (env :ig-website-url)
   :callback-url  "http://fierce-retreat-1705.herokuapp.com/callback-url"})


(defn handle-websocket
  "Handle requests for WebSockets"
  [request]
  (with-channel request channel
    (if (websocket? channel)
      (on-receive channel (fn [data] (send! channel data)))
      (send! channel {:status 200
                      :headers {"Content-Type" "text/plain"}
                      :body    "Long polling?"}))))


(defroutes main-routes
    (GET  "/"    req (render-resource "templates/index.html"))
    (GET  "/ws"  []  handle-websocket)
    (GET  "/callback-url" [] insta-response/echo-hub-challenge)
    (POST "/callback-url" [] insta-response/handle-new-media)
    (route/not-found "Page not found"))


(def app
  (-> main-routes
      (handler/site)))


(defn -main
  [& [port]]

  (println)
  (println "***")
  (println)
  (println "PORT: " (env :port))
  (println)
  (println (style "client-id ....... " :yellow) (style (:client-id client-options)     :red))
  (println (style "client-secret ... " :yellow) (style (:client-secret client-options) :red))
  (println (style "redirect-uri .... " :yellow) (style (:redirect-uri client-options)  :red))
  (println (style "website-url ..... " :yellow) (style (:website-url client-options)   :red))
  (println)
  (println "***")
  (println)


  (run-server app {:port (Integer. (or port (env :port) 5000))})

  ;; Bing all that jazz
  (binding [subscribe/*client-id*     (:client-id client-options)
            subscribe/*client-secret* (:client-secret client-options)
            subscribe/*callback-url*  (:callback-url client-options)]

    (let [sub-geo (subscribe/geo :lat 40.7903 :lng 73.9597 :radius 25)]
      (println "<<<<<")
      (println @sub-geo)
      (println ">>>>>")
      ))

; (with-instagram client-options
;   (let [geo-sub (subscribe/geo :lng 0 :lat 0 :radius 100)
;         tag-sub (subscribe/tag :tag "yolo")
;         apppppp (-> app (instagram/attach geo-sub tag-sub))]
;     ))

  (println "running"))
