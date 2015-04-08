(ns agency.irl.instagrammer.subscribe
  (:require [clj-http.client :as client]
            [agency.irl.instagrammer.client :refer :all]))


(defn sync-geo
  "Subscribe to Instagram GEO updates. Returns a "
  [& {:keys [lng lat radius]}]
  [lat lng radius]
  (client/post
    "https://api.instagram.com/v1/subscriptions/"
    {:form-params {:client_id     *client-id*
                   :client_secret *client-secret*
                   :object        "geography"
                   :aspect        "media"
                   :lat           lat
                   :lng           lng
                   :radius        radius
                   :callback_url  *callback-url*}}))


(defn sync-tag
  "Subscribe to Instagram GEO updates. Returns a "
  [& {:keys [tag]}]
  (client/post
    "https://api.instagram.com/v1/subscriptions/"
    {:form-params {:client_id     *client-id*
                   :client_secret *client-secret*
                   :object        "tag"
                   :aspect        "media"
                   :object_id     tag
                   :callback_url  *callback-url*}}))


(defn geo
  "Returns a future with the result of a request to subscribe to GEO updates."
  [& {:keys [lat lng radius]}]
  (println "(" lat " " lng " " radius ")")
  (future (sync-geo :lng lng :lat lat :radius radius)))


(defn tag
  "Returns a future with the result of a request to subscribe to GEO updates."
  [& {:keys [tag]}]
  (future (sync-tag :tag tag)))
