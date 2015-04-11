(ns agency.irl.instagrammer.respond
  (:require [clostache.parser :refer [render-resource]]))


(defn whatever
  "Returns a string blah"
  [req]
  (render-resource "templates/index.html"))
