(ns nrepl.main
  "Provide a main function for running nrepl"
  (:require
   [clojure.java.io :refer [file]]
   [clojure.stacktrace :refer [print-cause-trace]]
   [clojure.string :refer [split]]
   [clojure.tools.cli :refer [cli]]
   [clojure.tools.nrepl.server :refer [default-handler start-server]]))

(defn report-unexpected-exception
  "Report an exception to stderr."
  [^Throwable e]
  (binding [*out* *err*]
    (print-cause-trace e)
    (flush)))

(defn report-error
  "Report a message to *err*."
  [msg]
  (binding [*out* *err*]
    (println msg)
    (flush)))

(def ^:dynamic *exit-process?*
  "Bind to false to suppress process termination." true)

(defn exit [exit-code]
  (if *exit-process?*
    (do
      (shutdown-agents)
      (System/exit exit-code))
    (throw (ex-info "suppressed exit" {:exit-code exit-code}))))

(def nrepl-switches
  [["-p" "--port" "Port to run the nREPL server on"
    :default 0 :parse-fn #(Integer. %)]
   ["-a" "--ack-port" "Port to acknowledge the running port on"
    :parse-fn #(Integer. %)]
   ["-b" "--bind" "Bind address for the server"
    :default "127.0.0.1"]
   ["-f" "--port-file" "File to write with the running port on"]
   ["-m" "--middleware" "Middleware to add to the project (comma separated)"]
   ["-d" "--dispatch" "Symbol of handler function to use"]
   ["-h" "--help" "Show this help message" :flag true]
   ["-v" "--verbose" "Show server startup message"
    :flag true :default false]])

(def help
  (str "A command line for nREPL."
       \newline \newline
       (last (apply cli nil nrepl-switches))))

(defn resolve-sym-name
  [s]
  (let [sym (apply symbol (split s #"/"))
        sym-ns (symbol (namespace sym))]
    (try (require sym-ns)
         (catch Exception e
           (report-unexpected-exception e)
           (throw e)))
    (let [v (resolve sym)]
      (when-not v
        (report-error (str "Can not resolve " (pr-str sym)))
        (exit 1))
      (println "resolving" (pr-str sym) v)
      v)))

(defn handler [dispatch middleware]
  (when (and dispatch middleware)
    (report-error "Can only use one of --dispatch or --middleware")
    (exit 1))
  (if dispatch
    (resolve-sym-name dispatch)
    (let [mw (map resolve-sym-name (when middleware (split middleware #",")))]
      (println "mw" (pr-str (vec mw)))
      (apply default-handler
             (map resolve-sym-name (when middleware (split middleware #",")))))))

(defn server
  [{:keys [port bind ack-port verbose dispatch middleware port-file]
    :as options}]
  (let [options (select-keys options [:bind :port :ack-port])
        _ (println "options" (pr-str options))
        server (apply start-server
                      :handler (handler dispatch middleware)
                      (apply concat options))
        port (-> server deref :ss .getLocalPort)]
    (when port-file
      (doto (file port-file)
        .deleteOnExit
        (spit port)))
    (when verbose
      (println "nREPL server started on port" port "on" bind))
    @(promise)))

(defn ^{:doc help} nrepl-task
  [args]
  (try
    (let [[{:keys [help] :as options} args extras]
          (apply cli args nrepl-switches)]
      (when help
        (println nrepl.main/help)
        (exit 0))
      (server options))
    (catch Exception e
      ;; suppress exception traces for errors with :exit-code
      (if-let [exit-code (:exit-code (ex-data e))]
        (do
          (report-error (.getMessage e))
          (exit exit-code))
        (do
          (report-unexpected-exception e)
          (exit 1))))))

(defn -main
  ([& args] (nrepl-task args))
  ([] (nrepl-task *command-line-args*)))
