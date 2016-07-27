(ns reverie.redis.cache
  (:require reverie.cache
            [taoensso.carmine :as carmine :refer [wcar]]))

(defrecord CarmineCacheStore [conn-opts prefix breakpoint]
  reverie.cache/ICacheStore
  (read-cache [_ _ key] (wcar conn-opts (carmine/get (str prefix ":" key))))
  (write-cache [_ _ key data] (wcar conn-opts (carmine/set (str prefix ":" key) data)) nil)
  (delete-cache [_ _ key] (wcar conn-opts (carmine/del (str prefix ":" key))) nil)
  (clear-cache [store]
    (loop [index 0
           cursor 0]
      (let [[cursor data] (wcar conn-opts (carmine/scan cursor :match (str prefix ":*") :count 1000))]
        (if (or (empty? data)
                (> index breakpoint))
          nil
          (do
            (doseq [k data]
              (when (string? k)
                (reverie.cache/delete-cache store nil (.replace k (str prefix ":") ""))))
            (recur (inc index) cursor)))))
    nil))

(defn store
  "Creates and returns a Carmine-backed reverie/CMS Cache Store."
  [conn-opts & [{:keys [key-prefix breakpoint]
                 :or   {key-prefix "carmine:reverie-cache"
                        breakpoint 10000}}]]
  (CarmineCacheStore. conn-opts key-prefix breakpoint))
