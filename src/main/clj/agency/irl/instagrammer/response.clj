(ns agency.irl.instagrammer.response
  (:require [ring.util.codec :as codec]
            [ring.middleware.params]))


(defn- parse-params [params encoding]
  (let [params (codec/form-decode params encoding)]
    (if (and params (map? params)) params {})))


(defn echo-hub-challenge
  "Returns a Ring Response Object that Echoes:
    * hub.challenge
    * hub.verify_token
    * hub.calback_url"
  [req]
  (let [query-string  (:query-string req)
        encoding      (or (:encoding req) "UTF-8")
        params        (if (not (nil? query-string)) (parse-params query-string encoding))]
    {:status  200
     :body    (get params "hub.challenge" "👽")
     :header  {"Content-Type" "text/plain; charset=utf-8"}}))


;; @TODO: Look into add-watch/remove-watch to trigger atom events
(defn handle-new-media
  "Returns an immediate response, and fires off events."
  [req]
  (println (:body req))
  {:status 200
   :body   "🍕 "})
