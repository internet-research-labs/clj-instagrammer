(ns agency.irl.instagrammer.times
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]))

(defn format-time [timestamp]
  (f/unparse (f/formatters :time) (c/from-long (* 1000 timestamp))))
