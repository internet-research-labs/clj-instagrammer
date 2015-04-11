(ns agency.irl.instagrammer.request
  (:require [clj-http.client :as client]
            [agency.irl.instagrammer.times :as times]
            [agency.irl.instagrammer.client :refer :all]))


(def ^:private lat-lng-search-url "https://api.instagram.com/v1/media/search?lat=%s&lng=%s&distance=%s&min_timestamp=%s&client_id=%s&client_secret=%s")


(defn poll-http-sync
  [lat lng radius]
  (let [min-time (- (quot (System/currentTimeMillis) 1000) +40)]
    (client/get (format lat-lng-search-url
                        lat lng radius min-time *client-id* *client-secret*))))

(defn poll-http
  "Send an HTTP request for new media at longitude/latitude, then recurses.
  - lat:    decimal, latitude coordinate
  - lng:    decimal, longitude coordinate
  - radius: distance in meters
  - every-ms: "
  [& {:keys [lat lng radius every-ms callback error]}]
  @(future
     (Thread/sleep every-ms)
     (callback (poll-http-sync lat lng radius))
     (try
       (poll-http :lat lat
                  :lng lng
                  :radius radius
                  :every-ms every-ms
                  :callback callback
                  :error error)
       (catch Exception e (error (.getMessage e))))))
