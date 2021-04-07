(ns tunk.main
  (:require [clojure.tools.logging :refer [info error]]
            [tunk.config :as c]
            [tunk.http-server :as server]
            [tunk.database :as db])
  (:import (java.io Closeable))
  (:gen-class))

(defrecord Context [db]
  Closeable
  (close [_]
    (db/disconnect db)))

(defrecord App [ctx server url]
  Closeable
  (close [_]
    (.close server)
    (.close ctx)))

(defn start-app [{:keys [db-uri port]}]
  {:pre [(integer? port)
         (string? db-uri)]}
  (let [db (doto (db/connect db-uri)
             (db/migrate))
        ctx (->Context db)
        server (server/start ctx port)
        url (str "http://" (c/as-str "APP_DOMAIN" "localhost") ":" port)]
    (->App ctx server url)))

(defn -main [& _]
  (try
    (let [conf {:db-uri      (c/as-str "DATABASE_URI" nil)
                :server-port (c/as-int "HTTP_PORT" 5000)}]
      (start-app conf))
    (catch Throwable e
      (error e "Startup error")
      (System/exit 1))))
