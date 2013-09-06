(defproject nrepl-main "0.1.1-SNAPSHOT"
  :description "Provide a main for running an nrepl server."
  :url "https://github.com/pallet/nrepl-main"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot nrepl.main
  :aot []
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/tools.cli "0.2.2"]
                 [org.clojure/tools.nrepl "0.2.3"]])
