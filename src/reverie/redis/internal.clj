(ns reverie.redis.internal
  (:require reverie.internal
            [taoensso.carmine :as carmine :refer [wcar]]))

(defrecord CarmineInternalStore [conn-opts prefix]
  reverie.internal/IInternalStorage
  (read-storage [_ k] (wcar conn-opts (carmine/get (str prefix ":" k))))
  (delete-storage [_ k] (wcar conn-opts (carmine/del (str prefix ":" k))) nil)
  (write-storage [_ k v]
    (wcar conn-opts (carmine/set (str prefix ":" k) v))
    k))


(defn store
  "Creates and returns a Carmine-backed reverie/CMS Internal Store."
  [conn-opts & [{:keys [key-prefix]
                 :or   {key-prefix "carmine:reverie-internal"}}]]
  (CarmineInternalStore. conn-opts key-prefix))
