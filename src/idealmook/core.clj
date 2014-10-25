(ns idealmook.core (:gen-class))

(import '(existence Existence Existence010 Existence020 Existence030
          Existence031 Existence032 Existence040 Existence050))
(import '(coupling.interaction Interaction Interaction010))
(import coupling.Experiment)
(import coupling.Result)

(defprotocol Time (step [this]))

(defprotocol Labeled (get-label [this]))

(defprotocol World
  (get-other-experience [this experience])
  (get-result [this experience])
  (predict [this experience])
  (add-or-get-primitive-interaction [this experience result])
  (add-or-get-interaction [this label])
  (increase-satisfaction-counter [this]))

(deftype SimpleWorld
    [experiments rewards mood interactions 
     boredom-level satisfaction-counter previous-experience]
  Time
  (step [this]
    (let [experience
          (if (= (:mood this) :bored)
            (do (assoc this :satisfaction-counter 0)
                (get-other-experience this (:previous-experience this)))
            (:previous-experience this))]
      (let [anticipated (predict this experience)
            result (get-result this experience)]
        (add-or-get-primitive-interaction this experience result)
        (if (= result anticipated)
          (do 
            (assoc this :mood :self-satisfied)
            (increase-satisfaction-counter this))
          (do
            (assoc this :mood :frustrated)
            (assoc this :satisfaction-counter 0)))
        (when (>= (:satisfaction-counter this) (:boredom-level this))
          (assoc this :mood :bored))
        (assoc this :previous-experience experience)
        (format "%s%s %s" (get-label experience)
                (get-label result) (:mood this))))))

;; TODO: Rewrite `Interaction', `Experiment', `Result'

(defn create-interaction [label] (new Interaction010 label))
(defn create-experience [label] (new Experiment label))

;; methods

(defn add-or-get-primitive-interaction [this experience result]
  (let [interaction
        (add-or-get-interaction 
         this 
         (str (get-label experience) (get-label result)))]
    (assoc interaction :experience experience :result result)
    interaction))

(defn add-or-get-interaction [this label]
  (let [interactions (:interactions this)]
    (if-not (contains? interactions label)
      (assoc interactions label (create-interaction label)))
    (get interactions label)))

(defn add-or-get-experience [this label]
  (let [experiences (:experiences this)]
    (if-not (contains? experiences label)
      (assoc experiences label (create-experience label)))
    (get experiences label)))

(defn predict [this experience]
  (let [interaction
        (some (fn [elt] (= (:experiences elt) experience))
              (vals (:interactions this)))]
    (when interaction (:result interaction))))

(defn get-other-experience [this experience]
  (some (fn [elt] (not= elt experience)) (vals (:experiences this))))

(defn create-or-get-result [this label]
  (let [results (:results this)]
    (if-not (contains? results label)
      (assoc results label (new Result label)))
    (get results label)))

(defn get-result [this experience]
  (create-or-get-result 
   this
   (if (= experience (add-or-get-experience this :experience1))
     :result1 :result2)))

(defn increase-satisfaction-counter [this]
  (assoc this :satisfaction-counter (+ 1 (:satisfaction-counter this))))

(extend SimpleWorld
  World
  {
   :get-other-experience get-other-experience
   :predict predict
   :add-or-get-primitive-interaction add-or-get-primitive-interaction
   :add-or-get-interaction add-or-get-interaction
   :increase-satisfaction-counter increase-satisfaction-counter
   :get-result get-result })

(defn -main
  "Prints the log from executing assignment 1."
  [& args]
  (println "Assignment 1")
  (let [existence (new Existence010)]
    (loop [i 0]
      (when (< i 20)
        (println (format "%d: %s" i (.step existence)))
        (recur (+ 1 i)))))
  (println "")
  (println "Assignment 2")
  (let [existence (new Existence020)]
    (loop [i 0]
      (when (< i 20)
        (println (format "%d: %s" i (.step existence)))
        (recur (+ 1 i))))))
