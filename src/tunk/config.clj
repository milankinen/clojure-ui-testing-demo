(ns tunk.config
  (:require [clojure.edn :as edn]))

(defn- validate [key predicate value]
  (when-not (predicate value)
    (throw (IllegalArgumentException.
             (str "Invalid config value " key ": " value))))
  value)

(defn dev? []
  (or (= "1" (System/getProperty "dev"))
      (= "1" (System/getProperty "test"))))

(defn as-str [key defaults]
  (get (System/getenv) key (some-> defaults (str))))

(defn as-int [key defaults]
  (some->> (as-str key defaults)
           (edn/read-string)
           (validate key integer?)))

(defn as-float [key defaults]
  (some->> (as-str key defaults)
           (edn/read-string)
           (validate key float?)))

(defn as-bool [key defaults]
  (some->> (as-str key defaults)
           (edn/read-string)
           (validate key #(contains? #{true false} %))))

(defn dev-default [value]
  (when (dev?) value))
