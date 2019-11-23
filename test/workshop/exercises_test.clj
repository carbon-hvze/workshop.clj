(ns workshop.exercises-test
  (:require
   [clojure.test :refer :all]))

(deftest destructuring
  ;;; destructure
  ;;     |
  ;;     |
  ;;     V
  (let [value [[1 {:a {:b {:c 3}}}]]]

    (is (= value 3)))

  )

(defn even-sum [hm])

(deftest mc-test
  ;; вычислить сумму четных значений листьев 

  (is (= (even-sum {:a {:d 3} :b {:c 1}}) 0))

  (is (= (even-sum {:a {:d 2} :b {:c 1}}) 2))

  (is (= (even-sum {:a {:d 2} :b {:c [1 2 3]}}) 2))

  (is (= (even-sum {:a {:d 2} :b 2.0}) 4))

  (is (= (even-sum {:a {:d identity} :b {:e 11}}) 0)))

(defn *memoize [f])

(deftest memoization

  (def my-fn (*memoize (fn [& args] (Thread/sleep 1000) (apply + args))))

  (is (= (my-fn 1 2 3 4) 10))

  (is (= (my-fn 1 2) 3))

  (is (= (my-fn 1 3 2 4) 10))

  (is (= (my-fn 0 0 0) 0)))
