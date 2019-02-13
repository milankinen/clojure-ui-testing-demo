(ns tunk.api
  (:require [clojure.java.jdbc :as sql]
            [schema.core :as s]
            [ring.util.http-response :refer [ok]]
            [compojure.api.sweet :refer [routes GET POST]]
            [tunk.database :as db]))

(def SavedTodo
  {:id        s/Uuid
   :completed s/Bool
   :title     s/Str})

(def TodoSaveRequest
  {:todos [SavedTodo]})

(defn load-todos [ctx]
  (let [db (:db ctx)]
    (sql/query db ["SELECT * FROM todo"])))

(defn save-todos! [ctx todos]
  (db/with-tx [tx (:db ctx)]
    (sql/execute! tx ["DELETE FROM todo"])
    (sql/insert-multi! tx :todo todos)))

(defn api-routes [ctx]
  (routes
    (GET "/todos" []
      (ok (load-todos ctx)))
    (POST "/todos" []
      :body [body TodoSaveRequest]
      (save-todos! ctx (:todos body))
      (ok {:success true}))))
