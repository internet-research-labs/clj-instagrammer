(ns agency.irl.instagrammer.respond
  (:require [clostache.parser :refer [render-resource]]))


(defn whatever
  "Returns a string blah"
  [req]
  (println (render-resource 
             "templates/index.html"))
  (render-resource "templates/index.html"))
