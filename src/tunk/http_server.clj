(ns tunk.http-server
  (:require [clojure.tools.logging :refer [info]]
            [clojure.java.io :as io]
            [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [GET routes]]
            [compojure.route :refer [not-found resources]]
            [compojure.api.sweet :refer [api context]]
            [tunk.app :as api]
            [tunk.config :as c])
  (:import (java.io Closeable)))

(defn- app-api [path handler]
  (api {:coercion :schema
        :swagger  {:ui   "/api-docs"
                   :spec "/swagger.json"
                   :data {:info     {:title "API-docs"}
                          :tags     []
                          :consumes ["application/json"]
                          :produces ["application/json"]}}}
       (context path [] handler)))

(defn- handler [ctx]
  (letfn [(build []
            (routes
              (GET "/" []
                {:body    (slurp (io/resource "index.html"))
                 :status  200
                 :headers {"Content-Type" "text/html"}})
              (app-api "/api" (api/api-routes ctx))
              (resources "/static")
              (not-found "...")))]
    (if (c/dev?)
      (fn [req] ((build) req))
      (build))))

(defrecord Server [port shutdown!]
  Closeable
  (close [_]
    (shutdown!)))

(defn start [ctx port]
  (let [stop-server (run-server (handler ctx) {:port port :worker-name-prefix "http-"})
        shutdown!   (fn stop-server! []
                      (stop-server :timeout 5000)
                      (info "HTTP server stopped"))]
    (info "HTTP server started at port" port)
    (->Server port shutdown!)))
