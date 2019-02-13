(ns tunk.test.ui-tests
  (:require [clojure.test :refer :all]
            [cuic.core :as c]
            [cuic.test :refer [is*]]
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
  (with-open [app     (start-app! test-conf)
              browser (c/launch! {:headless true})]
    (binding [*app*       app
              c/*browser* browser
              c/*config*  (assoc c/*config* :timeout 1000)]
      (api/save-todos! (:ctx *app*) [example-db-todo])
      (c/goto! (:url *app*))
      (t))))

(use-fixtures :each ui-test-fixture)

(defn todo-list-items []
  (c/q ".todo-list li"))

(defn todo-title [todo]
  (c/text-content todo))

(defn todo-item-by-title [title]
  (->> (todo-list-items)
       (filter #(= title (todo-title %)))
       (first)))

(defn add-todo! [text]
  (doto (c/q ".new-todo")
    (c/click!)
    (c/clear-text!)
    (c/type! text :enter)))

(defn remove-todo! [todo]
  (c/hover! todo)
  (c/click! (c/q todo "button.destroy")))

(deftest todomvc-app-works-ok
  (testing "stored todo items are displayed at page startup"
    (is* (= ["tsers"] (map todo-title (todo-list-items)))))
  (testing "added todo items are displayed in the list and stored immediately to the database"
    (add-todo! "lolbal")
    (add-todo! "mäijjäi")
    (is* (= ["tsers" "lolbal" "mäijjäi"] (map todo-title (todo-list-items))))
    (is* (= ["tsers" "lolbal" "mäijjäi"] (map :title (api/load-todos (:ctx *app*))))))
  (testing "removed todo items are removed from the list and database immediately"
    (remove-todo! (todo-item-by-title "tsers"))
    (remove-todo! (todo-item-by-title "mäijjäi"))
    (is* (= ["lolbal"] (map todo-title (todo-list-items))))
    (is* (= ["lolbal"] (map :title (api/load-todos (:ctx *app*)))))))
