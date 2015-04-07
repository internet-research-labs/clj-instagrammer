(ns agency.irl.instagrammer.subscribe
  (:require [clj-http.client :as client]))

(def ^:dynamic *client-id*     nil)
(def ^:dynamic *client-secret* nil)
(def ^:dynamic *redirect-uri*  nil)
(def ^:dynamic *website-url*   nil)
(def ^:dynamic *callback-url*  nil)
(def ^:dynamic *debug-mode*    nil)

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

(defn geo
  "Returns a future with the result of a request to subscribe to GEO updates."
  [& {:keys [lat lng radius]}]
  (future (sync-geo :lng lng :lat lat :radius radius)))
