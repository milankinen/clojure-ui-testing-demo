(defproject clojure-ui-testing-demo "0.0.1"
  :description "CUIC demo project"
  :url "https://github.com/milankinen/clojure-ui-testing-demo"
  :dependencies
  [[org.clojure/clojure "1.10.3"]
   [org.clojure/tools.logging "1.1.0"]
   [org.clojure/java.jdbc "0.7.12"]
   [ch.qos.logback/logback-classic "1.2.3"]
   [commons-io/commons-io "2.8.0"]
   [metosin/compojure-api "2.0.0-alpha27" :exclusions [org.clojure/spec.alpha]]
   [http-kit "2.5.3"]
   [javax.servlet/servlet-api "2.5"]
   [org.flywaydb/flyway-core "7.7.2"]
   [org.postgresql/postgresql "42.2.19"]
   [net.postgis/postgis-jdbc "2.5.0" :exclusions [org.postgresql/postgresql]]
   [com.mchange/c3p0 "0.9.5.5"]
   [cheshire "5.10.0"]
   [cuic "1.0.0-RC1"]]
  :plugins
  [[lein-ancient "0.7.0"]]
  :main ^:skip-aot tunk.main
  :repl-options {:init-ns dev-repl}
  :source-paths ["src"]
  :resource-paths ["resources" "ui/dist"]
  :target-path "target/%s"
  :profiles {:dev     {:jvm-opts     ["-Ddev=1"]
                       :source-paths ["dev/src"]}
             :repl    {:repl-options {:init-ns tunk.repl}
                       :jvm-opts     ["-Dcuic.headless=false"]}
             :test    {:jvm-opts ["-Dtest=1" "-Dlogback.configurationFile=resources/logback-error-only.xml"]}
             :uberjar {:aot :all}}
  :aliases {"t" ["test"]})
