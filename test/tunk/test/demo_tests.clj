(ns tunk.test.demo-tests
  (:require [clojure.test :refer [testing use-fixtures]]
            [cuic.core :as c]
            [cuic.test :refer [deftest* is* browser-test-fixture]]
            [tunk.app :refer [*ctx*] :as app]
            [tunk.main :refer [start-app]]))

(defn- app-fixture [test]
  (with-open [app (start-app {:db-uri "postgresql://localhost:45432/demo?currentSchema=tests&user=dev&password=ts3rs"
                              :port   5002})]
    (binding [*ctx* (:ctx app)
              c/*base-url* "http://localhost:5002"]
      (app/clear-todos)
      (test))))

(use-fixtures
  :each
  (browser-test-fixture)
  app-fixture)

(deftest* todo-list-tests
  (c/goto "/")
  (c/sleep 3000)
  (testing "added todos are stored to the database"
    (is* '???))
  (testing "removed todos are removed from the database as well"
    (is* '???)))
