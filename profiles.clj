{:release
 {:plugins [[lein-set-version "0.3.0"]]
  :set-version {:updates [{:path "README.md" :no-snapshot true}]}}
 :no-checkouts {:checkout-shares ^:replace []} ; disable checkouts
 :clojure-1.4.0 {:dependencies [[org.clojure/clojure "1.4.0"]]}
 :clojure-1.5.0 {:dependencies [[org.clojure/clojure "1.5.0"]]}
 :clojure-1.5.1 {:dependencies [[org.clojure/clojure "1.5.1"]]}
 :doc {:dependencies [[com.palletops/pallet-codox "0.1.0"]]
       :codox {:writer codox-md.writer/write-docs
               :output-dir "doc/0.1.0/api"
               :src-dir-uri "https://github.com/pallet/pallet/blob/develop"
               :src-linenum-anchor-prefix "L"}
       :aliases {"marg" ["marg" "-d" "doc/0.1.0/annotated"]
                 "codox" ["doc"]
                 "doc" ["do" "codox," "marg"]}}}
