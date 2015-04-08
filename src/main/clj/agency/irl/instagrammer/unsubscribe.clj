(ns agency.irl.instagrammer.unsubscribe
  (:require [clj-http.client :as client]
            [agency.irl.instagrammer.client :refer :all]))

(defn all-sync
  "Sends a UNSUBSCRIBE ALL HTTP request."
  []
  (client/delete
    (str 
      "https://api.instagram.com/v1/subscriptions"
      "?client_secret=" *client-secret*
      "&object=all"
      "&client_id=" *client-id*)))

(defn all
  "Sends a UNSUBSCRIBE ALL HTTP request asynchronously."
  []
  (future (all-sync)))
