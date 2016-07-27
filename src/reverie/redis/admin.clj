(ns reverie.redis.admin
  (:require [clojure.edn :as edn]
            reverie.admin.storage
            [taoensso.carmine :as carmine :refer [wcar]]))

(defrecord CarmineAdminStore [conn-opts prefix])

(extend-type CarmineAdminStore
  reverie.admin.storage/IAdminStorage
  (get [store k]
    (try
      (wcar (:conn-opts store) (carmine/get (str (:prefix store) ":" k)))
      (catch Exception _
        {})))
  (dissoc! [store k] (wcar (:conn-opts store) (carmine/del (str (:prefix store) ":" k))) nil)
  (assoc! [store k v]
    (wcar (:conn-opts store) (carmine/set (str (:prefix store) ":" k) v))
    nil))

(defn store
  "Creates and returns a Carmine-backed reverie/CMS Internal Store."
  [conn-opts & [{:keys [key-prefix]
                 :or   {key-prefix "carmine:reverie-admin"}}]]
  (CarmineAdminStore. conn-opts key-prefix))
