{:release
 {:plugins [[lein-set-version "0.3.0"]]
  :set-version {:updates [{:path "README.md" :no-snapshot true}]}}
 :no-checkouts {:checkout-shares ^:replace []} ; disable checkouts
 :clojure-1.4.0 {:dependencies [[org.clojure/clojure "1.4.0"]]}
 :clojure-1.5.0 {:dependencies [[org.clojure/clojure "1.5.0"]]}
 :clojure-1.5.1 {:dependencies [[org.clojure/clojure "1.5.1"]]}}
