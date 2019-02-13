(ns dev-repl
  (:require [tunk.main :refer [start-app!]]))

(def repl-conf
  {:db-uri      "postgresql://localhost:45432/demo?currentSchema=repl&user=dev&password=ts3rs"
   :server-port 5001})

(defonce app (start-app! repl-conf))

(def ctx (:ctx app))
