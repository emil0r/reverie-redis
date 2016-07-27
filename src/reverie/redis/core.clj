(ns reverie.redis.core
  (:require [reverie.redis.admin :as redis.admin]
            [reverie.redis.cache :as redis.cache]
            [reverie.redis.internal :as redis.internal]
            [reverie.redis.session :as redis.session]
            [reverie.settings :as settings]))

(defn get-stores
  ([settings] (get-stores settings [:storage :redis]))
  ([settings path]
   (let [{:keys [cache internal admin session]} (settings/get settings path)
         session-store   (if session  (redis.session/store session))
         admin-store     (if admin    (redis.admin/store admin))
         cache-store     (if cache    (redis.cache/store cache))
         internal-store  (if internal (redis.internal/store internal))]
     {:session-store     session-store
      :admin-store       admin-store
      :cache-store       cache-store
      :internal-store    internal-store})))
