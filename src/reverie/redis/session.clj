(ns reverie.redis.session
  (:require [taoensso.carmine.ring :as carmine.ring]))

(defn store [{:keys [conn-opts options] :as data}]
  (if data
    (carmine.ring/carmine-store conn-opts options)))
