(ns agency.irl.instagrammer.unsubscribe
  (:require [clj-http.client :as client]
            [agency.irl.instagrammer.client :refer :all]))

(defn all-sync
  "Sends a UNSUBSCRIBE ALL HTTP request."
  []
  (client/delete
    "https://api.instagram.com/v1/subscriptions/"
    {:form-params {:client_id     *client-id*
                   :client_secret *client-secret*
                   :object        "geography"
                   :aspect        "media"
                   :lat           lat
                   :lng           lng
                   :radius        radius
                   :callback_url  *callback-url*}}))

(defn all
  "Sends a UNSUBSCRIBE ALL HTTP request asynchronously."
  []
  (future (all-sync)))
