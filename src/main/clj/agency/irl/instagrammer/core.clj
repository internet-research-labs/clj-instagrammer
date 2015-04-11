(ns agency.irl.instagrammer.core
  (:require [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [agency.irl.instagrammer.response :refer [*subscription*] :as insta-response]
            [agency.irl.instagrammer.subscribe :as subscribe]
            [agency.irl.instagrammer.unsubscribe :as unsubscribe]
            [agency.irl.instagrammer.client :refer :all]
            [agency.irl.instagrammer.request :as requester]
            [agency.irl.instagrammer.times :as times]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [clostache.parser :refer [render-resource]]
            [org.httpkit.server :refer :all]
            [org.httpkit.timer :refer :all]
            [ring.util.response :refer :all]
            [clansi :refer [style]]
            [cheshire.core :as cheshire]
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
  (swap! clients assoc client value))

(defn rm-client! [client]
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
  (log/debug "received update")
  (update-clients! "got update"))


(defn ping-clients!
  "Sends a Websocket message to our clients"
  [res]
  (let [body (->> (:body res))
        ping (cheshire/parse-string body)]
    (update-clients! body)))

(defn show-error
  [err]
  (log/error "error"))


(defroutes main-routes
    (GET  "/"    req (render-resource "templates/index.html" {:websocket-url (env :websocket-url) :version "x_x"}))
    (GET  "/ws"  []  handle-websocket)
    (GET  "/callback-url" [] insta-response/echo-hub-challenge)
    (POST "/callback-url" [] (insta-response/make-new-handler got-new-media))
    (route/resources "/")
    (route/not-found "Page not found"))


(def app
  (-> main-routes
      (handler/site)))


(def ^:private nyc-regions
  [[40.765206 -73.977544 3500]
   [40.765206 -73.977544 3500]])

(defn parse-int [s]
  (Integer/parseInt (re-find #"\A-?\d+" s)))


(defn get-locations
  [result]
  (let [body      (:body result)
        body-json (cheshire/parse-string body true)
        data      (:data body-json)
        timestamps (map #(parse-int (:created_time %1)) data)]

    (let [min-stamp (apply max timestamps)
               max-stamp (apply min timestamps) 
               min-time  (times/format-time min-stamp)
               max-time  (times/format-time max-stamp)]
      (try 
        (log/debug "Errors comes in with:")
        (log/debug "  max = " min-stamp " (" min-time ")")
        (log/debug "  min = " max-stamp  " (" max-time ")")
        (log/debug "  count = " (count timestamps))
        (catch Exception e (log/error (.getMessage e)))))))


(defn -main
  [& [port]]

  (log/debug "Starting up new webserver with:")
  (log/debug "  client-id ....... " (:client-id client-options))
  (log/debug "  client-secret ... " (:client-secret client-options))
  (log/debug "  redirect-uri .... " (:redirect-uri client-options))
  (log/debug "  website-url ..... " (:website-url client-options))


  (let [server-port (Integer. (or port (env :port) 5000))]
    (run-server app {:port server-port}))

  (binding [*client-id*     (:client-id client-options)
            *client-secret* (:client-secret client-options)
            *callback-url*  (:callback-url client-options)]

    (unsubscribe/all-sync)

    (requester/poll-http :lat     40.765206
                         :lng     -73.977544
                         :radius   3500
                         :every-ms 2000
                         :callback ping-clients!
                         :error    show-error)))
