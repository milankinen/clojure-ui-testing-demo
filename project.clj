(defproject clojure-ui-testing-demo "0.0.1"
  :description "CUIC demo project"
  :url "https://github.com/milankinen/clojure-ui-testing-demo"
  :dependencies
  [[org.clojure/clojure "1.10.0"]
   [org.clojure/tools.logging "0.4.1"]
   [org.clojure/java.jdbc "0.7.8"]
   [ch.qos.logback/logback-classic "1.2.3"]
   [commons-io/commons-io "2.6"]
   [metosin/compojure-api "2.0.0-alpha27"
    :exclusions [org.clojure/spec.alpha]]
   [http-kit "2.3.0"]
   [javax.servlet/servlet-api "2.5"]
   [org.flywaydb/flyway-core "5.2.4"]
   [org.postgresql/postgresql "42.2.5"]
   [net.postgis/postgis-jdbc "2.3.0"
    :exclusions [org.postgresql/postgresql]]
   [com.mchange/c3p0 "0.9.5.2"]
   [cheshire "5.8.1"]]
  :plugins
  [[lein-ancient "0.6.15"]]
  :main ^:skip-aot tunk.main
  :repl-options {:init-ns dev-repl}
  :source-paths ["src"]
  :resource-paths ["resources" "ui/dist"]
  :target-path "target/%s"
  :profiles {:dev     {:jvm-opts ["-Ddev=1"]
                       :source-paths ["dev/src"]}
             :uberjar {:aot :all}}
  :aliases {"t" ["test"]})
