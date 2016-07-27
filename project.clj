(defproject reverie-redis "0.1.0-SNAPSHOT"
  :description "Redis adapters for reverie/CMS"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [reverie-core "0.8.0-SNAPSHOT"]
                 [com.taoensso/carmine "2.14.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]}})
