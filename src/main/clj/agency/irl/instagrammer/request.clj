(ns agency.irl.instagrammer.request
  (:require [clj-http.client :as client]
            [agency.irl.instagrammer.client :refer :all]))


;; (def lat-lng-search-url "client id = %s")
(def lat-lng-search-url "https://api.instagram.com/v1/media/search?lat=%s&lng=%s&distance=%s&min_timestamp=%s&client_id=%s&client_secret=%s")

(defn poll-http-sync
  [lat lng radius]
  (let [min-time (- (quot (System/currentTimeMillis) 1000) -4)]
    (client/get (format lat-lng-search-url
                        lat lng radius min-time *client-id* *client-secret*))))

(defn poll-http
  [& {:keys [lat lng radius callback]}]
  (future
      (Thread/sleep 1000)
      (poll-http :lat lat :lng lng :radius radius :callback callback)
      (callback (poll-http-sync lat lng radius))))
