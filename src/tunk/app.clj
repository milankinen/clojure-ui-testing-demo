(ns tunk.app
  (:require [clojure.java.jdbc :as sql]
            [compojure.api.sweet :refer [routes GET POST]]
            [ring.util.http-response :refer [ok]]
            [schema.core :as s]
            [tunk.database :as db]))

(defonce ^:dynamic *ctx* nil)

(def SavedTodo
  {:id        s/Uuid
   :completed s/Bool
   :title     s/Str})

(def TodoSaveRequest
  {:todos [SavedTodo]})

(defn get-todos []
  (let [db (:db *ctx*)]
    (sql/query db ["SELECT id, completed, title FROM todo ORDER BY idx ASC"])))

(defn clear-todos []
  (db/with-tx [tx (:db *ctx*)]
    (first (sql/execute! tx ["DELETE FROM todo"]))))

(defn save-todos [todos]
  (db/with-tx [tx (:db *ctx*)]
    (sql/execute! tx ["DELETE FROM todo"])
    (sql/insert-multi! tx :todo todos)))

(defn api-routes [ctx]
  (routes
    (GET "/todos" []
      (binding [*ctx* ctx]
        (ok (get-todos))))
    (POST "/todos" []
      :body [body TodoSaveRequest]
      (binding [*ctx* ctx]
        (save-todos (:todos body))
        (ok {:success true})))))
