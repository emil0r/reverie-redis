(ns reverie.test.redis
  (:require [clj-time.core :as t]
            [midje.sweet :refer :all]
            [reverie.admin.storage :as admin.storage]
            [reverie.cache :as cache]
            [reverie.internal :as internal]
            [reverie.page :as page]
            [reverie.redis.admin :as redis.admin]
            [reverie.redis.cache :as redis.cache]
            [reverie.redis.internal :as redis.internal]))



(fact "Admin Store"
      (let [store (redis.admin/store {})
            data {1 (t/now)}]
        (admin.storage/assoc! store :foobar data)
        [(admin.storage/get store :foobar)
         (admin.storage/dissoc! store :foobar)
         (admin.storage/get store :foobar)]
        => [data nil nil]))


(fact "Internal Store"
      (let [store (redis.internal/store {})
            data {"/" {:title "home"}
                  "/foo" {:title "foo"
                          :created (t/now)}}]
        (internal/write-storage store :page/all data)
        (internal/read-storage store :page/all)
        => data))

(fact "Cache Store"
      (let [store (redis.cache/store {})
            data (page/map->Page {:title "my page"
                                  :name "Home"
                                  :id 1
                                  :slug "/"
                                  :serial 1
                                  :template :main
                                  :published? true
                                  :created (t/now)})

            options nil]
        (cache/clear-cache store)
        (cache/write-cache store options "/" data)
        (cache/write-cache store options "/foo" {:foo true})
        (cache/write-cache store options "/bar" {:bar true})
        (cache/write-cache store options "/baz" {:baz true})
        (let [read-cache-1 (cache/read-cache store options "/")
              _ (cache/delete-cache store options "/")
              read-cache-2 (cache/read-cache store options "/")]
          (cache/clear-cache store)
         {"read-cache-1" read-cache-1
          "read-cache-2" read-cache-2
          "read-cache-/foo" (cache/read-cache store options "/foo")})
        => {"read-cache-1" data
            "read-cache-2" nil
            "read-cache-/foo" nil}))
