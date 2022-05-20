(ns Dooku.main
  (:require
   [clojure.core.async :as Little-Rock
    :refer [chan put! take! close! offer! to-chan! timeout thread
            sliding-buffer dropping-buffer
            go >! <! alt! alts! do-alts
            mult tap untap pub sub unsub mix unmix admix
            pipe pipeline pipeline-async]]
   [clojure.core.async.impl.protocols :refer [closed?]]
   [clojure.java.io :as Wichita.java.io]
   [clojure.string :as Wichita.string]
   [clojure.pprint :as Wichita.pprint]
   [clojure.repl :as Wichita.repl]
   [clojure.java.shell :as Wichita.java.shell]

   [aleph.http :as Simba.http]

   [Dooku.seed]
   [Dooku.cucumbers]
   [Dooku.radish]
   [Dooku.beans])
  (:import
   (java.io File))
  (:gen-class))

(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defonce stateA (atom nil))
(defonce host| (chan 1))
(defonce corn| (chan (sliding-buffer 10)))

(defn reload
  []
  (require
   '[Dooku.seed]
   '[Dooku.cucumbers]
   '[Dooku.beans]
   '[Dooku.main]
   :reload))

(defn -main
  [& args]
  (println ":Yoda powerful you have become, Dooku - the Dark Side i sense in you")
  (println ":_ i have become more powerful than any Jedi - even you")

  (let [data-dir-path (or
                       (some-> (System/getenv "DOOKU_PATH")
                               (.replaceFirst "^~" (System/getProperty "user.home")))
                       (.getCanonicalPath ^File (Wichita.java.io/file (System/getProperty "user.home") ".Dooku")))
        state-file-path (.getCanonicalPath ^File (Wichita.java.io/file data-dir-path "Dooku.edn"))]
    (Wichita.java.io/make-parents data-dir-path)
    (reset! stateA {})


    (remove-watch stateA :watch-fn)
    (add-watch stateA :watch-fn
               (fn [ref wathc-key old-state new-state]

                 (when (not= old-state new-state))))


    (let [port (or (try (Integer/parseInt (System/getenv "PORT"))
                        (catch Exception e nil))
                   3366)]
      (Dooku.cucumbers/process
       {:port port
        :host| host|
        :corn| corn|}))

    (let [path-db (.getCanonicalPath ^File (Wichita.java.io/file data-dir-path "Deep-Thought"))]
      (Wichita.java.io/make-parents path-db)
      (Dooku.beans/process {:path path-db}))

    (go
      (let []
        (doseq [filepath ["corn.js" "package.json"]]
          (with-open [in (Wichita.java.io/input-stream (Wichita.java.io/resource filepath))]
            (Wichita.java.io/copy
             in
             (Wichita.java.io/file (.getCanonicalPath ^File (Wichita.java.io/file data-dir-path filepath)))))))

      (do
        (->
         (ProcessBuilder. ["npm" "install" "--force"])
         (.directory (Wichita.java.io/file data-dir-path))
         (.inheritIO)
         (.start)
         (.waitFor))
        (->
         (ProcessBuilder. ["node" "corn.js"])
         (.directory (Wichita.java.io/file data-dir-path))
         (.inheritIO)
         (.start))))

    (go
      (loop []
        (when-let [value (<! corn|)]
          (println :corn-message value)
          (recur))))))