(ns tunk.repl
  (:require [tunk.main :refer [start-app]]
            [tunk.app :as app]))

(defonce app
  (start-app {:db-uri "postgresql://localhost:45432/demo?currentSchema=repl&user=dev&password=ts3rs"
              :port   5000}))

(defonce ctx
  (alter-var-root #'app/*ctx* (constantly (:ctx app))))
