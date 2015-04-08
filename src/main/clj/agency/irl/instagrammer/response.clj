(ns agency.irl.instagrammer.response
  (:require [ring.util.codec :as codec]
            [ring.util.request :refer [body-string]]
            [ring.middleware.params :refer [wrap-json-body params-request]]))

(def ^:dynamic *subscription* nil)

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
  (println *subscription*)
  {:status 200
   :body   "🍕 "
   :header  {"Content-Type" "text/plain; charset=utf-8"}})


(defn make-new-handler
  "Returns a handler that fires a response to the callback."
  [callback]
  (fn [req]
    (future (callback
              (body-string req)
              (:params (params-request req))))
    {:status 200
     :body   "🍕 "
     :header  {"Content-Type" "text/plain; charset=utf-8"}}))

