# reverie-redis

redis adapters for reverie/CMS using Carmine.

## Usage

```clojure

[reverie-redis "0.1.0"]

(requrie '[reverie.redis.core :as redis])

(let [settings (init-settings "settings.edn") ;; <- pseudo code
      {:keys [session-store cache-store internal-store]} (redis/get-stores settings)]
      ;; all of these are to be in init.clj in the system-map function

      ;; send to reverie.admin.AdminInitializer 
      (admin/get-admin-intiailizer {:storage internal-store})

      ;; send to reverie.server.Server
      (server/get-server {:store session-store})
      
      ;; send to reverie.cache.CacheManager
      (cache/cachemanager {:store cache-store})
  )

```

## License

Copyright © 2016 Emil Bengtsson

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

--

Coram Deo
