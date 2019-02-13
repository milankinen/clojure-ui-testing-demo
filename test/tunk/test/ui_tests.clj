(ns tunk.test.ui-tests
  (:require [clojure.test :refer :all]
            [tunk.main :refer [start-app!]]
            [tunk.api :as api]))

(def test-conf
  {:db-uri      "postgresql://localhost:45432/demo?currentSchema=tests&user=dev&password=ts3rs"
   :server-port 5002})

(def example-db-todo
  {:id        #uuid"e512a01e-d73d-4858-88ad-3a6962e11ec7"
   :completed false
   :title     "tsers"})

(defonce ^:dynamic *app* nil)

(defn ui-test-fixture [t]
  (with-open [app (start-app! test-conf)]
    (binding [*app* app]
      (api/save-todos! (:ctx *app*) [example-db-todo])
      (t))))

(use-fixtures :each ui-test-fixture)

(deftest todomvc-app-works-ok
  (testing "stored todo items are displayed at page startup"
    '...)
  (testing "added todo items are displayed in the list and stored immediately to the database"
    '...)
  (testing "removed todo items are removed from the list and database immediately"
    '...))

