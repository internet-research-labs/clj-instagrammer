(ns agency.irl.instagrammer.core
  (:require [environ.core :refer [env]]
            [agency.irl.instagrammer.response :refer [*subscription*] :as insta-response]
            [agency.irl.instagrammer.subscribe :as subscribe]
            [agency.irl.instagrammer.unsubscribe :as unsubscribe]
            [agency.irl.instagrammer.client :refer :all]
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
   :callback-url  "https://fierce-retreat-1705.herokuapp.com/callback-url"})


;; <<< socket manager
;;
;; Collection of sockets mapped to a value and a set of functions that add and
;; remove values from the collection. This is hack to make things work smoothly
;; in clojure land. Is there a better way to share variables across several
;; scopes? Without rewriting the overall flow too much?
;; Notably, an event emitter needs to be handed a list of the current sockets
;; everytime it is executed.

(def clients (atom {}))

(defn add-client! [client value]
  (println "++" value)
  (swap! clients assoc client value))

(defn rm-client! [client]
  (println "--" (get @clients client))
  (swap! clients dissoc client))

(defn update-clients! [message]
  (doseq [client (keys @clients)]
    (send! client message)))


;; >>> socket manager


;; <<< websocket handler
;;
;; A handler that attaches

(defn handle-websocket
  "Handle requests for WebSockets"
  [request]
  (with-channel request channel
    (let [id (rand-int 1000000)]
      (if (websocket? channel)
        (do
          (add-client! channel id)
          (on-close channel (fn [status] (rm-client! channel))))
        (send! channel {:status 200
                        :headers {"Content-Type" "text/plain"}
                        :body    "Long polling?"})))))

;; >>> websocket handler

(defn got-new-media
  "Does something with new media from instagram"
  [body params]
  (println body)
  ; (println params)
  (update-clients! "got update"))


(defroutes main-routes
    (GET  "/"    req (render-resource "templates/index.html" {:websocket-url (env :websocket-url) :version "x_x"}))
    (GET  "/ws"  []  handle-websocket)
    (GET  "/callback-url" [] insta-response/echo-hub-challenge)
    (POST "/callback-url" [] (insta-response/make-new-handler got-new-media))
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


  (let [server-port (Integer. (or port (env :port) 5000))]
    (run-server app {:port server-port}))

  (binding [*client-id*     (:client-id client-options)
            *client-secret* (:client-secret client-options)
            *callback-url*  (:callback-url client-options)]

    (unsubscribe/all-sync)

    (let [geo-sub (subscribe/geo :lng 0 :lat 0 :radius 100)
          tag-sub (subscribe/tag :tag "yolo")]))


; ; Bind client info
; (binding [*client-id*     (:client-id client-options)
;           *client-secret* (:client-secret client-options)
;           *callback-url*  (:callback-url client-options)]

;   (let [
;         ;sub-geo (subscribe/geo :lat 40.7903 :lng 73.9597 :radius 5000)
;         ;sub-tag (subscribe/tag :tag "yolo")
;         ]
;     ;(println @sub-geo @sub-tag)
;     ))

; (with-instagram client-options
;   (let [geo-sub (subscribe/geo :lng 0 :lat 0 :radius 100)
;         tag-sub (subscribe/tag :tag "yolo")
;         apppppp (-> app (instagram/attach geo-sub tag-sub))]
;     ))

  (println "running"))
