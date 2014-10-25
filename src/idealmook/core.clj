(ns idealmook.core (:gen-class))

(import '(existence
          Existence
          Existence010
          Existence020
          Existence030
          Existence031
          Existence032
          Existence040
          Existence050))

(defn -main
  "Prints the log from executing assignment 1."
  [& args]
  (let [existence (new Existence010)]
    (loop [i 0]
      (when (< i 20)
        (println (format "%d: %s" i (.step existence)))
        (recur (+ 1 i))))))
