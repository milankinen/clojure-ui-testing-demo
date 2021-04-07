(ns tunk.database
  (:require [clojure.tools.logging :refer [info]]
            [clojure.string :as string]
            [clojure.java.jdbc :as sql])
  (:import (java.net URI)
           (com.mchange.v2.c3p0 ComboPooledDataSource)
           (org.flywaydb.core Flyway)))

(defn- uri->db-schema [conn-uri]
  (or (as-> (or (.getQuery (URI. conn-uri)) "") $
            (string/split $ #"&")
            (keep #(second (re-matches #"^currentSchema=(.+)$" %)) $)
            (first $))
      (throw (IllegalStateException. "currentSchema not set to database uri"))))

(defn connect [conn-uri]
  (let [ds (doto (ComboPooledDataSource.)
             (.setUnreturnedConnectionTimeout 1000)
             (.setDriverClass "org.postgresql.Driver")
             (.setJdbcUrl (str "jdbc:" conn-uri))
             (.setMinPoolSize 1)
             (.setMaxPoolSize 5)
             (.setMaxIdleTimeExcessConnections (* 2 30 60))
             (.setMaxIdleTime (* 5 60 60)))]
    (sql/execute! {:datasource ds} (format "CREATE SCHEMA IF NOT EXISTS %s;" (uri->db-schema conn-uri)))
    (info "Connected to database!")
    {:datasource ds
     :conn-uri   conn-uri}))

(defn disconnect [{:keys [datasource]}]
  (.close datasource)
  (info "Disconnected from database!"))

(defn migrate [{:keys [datasource conn-uri]}]
  (let [flyway (-> (Flyway/configure)
                   (.dataSource datasource)
                   (.schemas (into-array String [(uri->db-schema conn-uri)]))
                   (.load))]
    (.migrate flyway)
    (info "Database migrated!")))

(defmacro with-tx [[tx conn] & body]
  `(sql/db-transaction* ~conn (fn [~tx] ~@body)))
