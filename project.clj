(defproject idealmook "0.1.0-SNAPSHOT"
  :description "Programming exercises for IDEALMOOC course in AI.
For information on the course see: http://liris.cnrs.fr/ideal/mooc/"
  :url "https://github.com/wvxvw/idealmooc-clojure-assignments"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot idealmook.core
  :target-path "target/%s"
  :java-source-paths ["java/trunk/TD/src"]
  :profiles {:uberjar {:aot :all}})
