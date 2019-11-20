(ns workshop.core)

;; primitive data types

1

"123"

#".*"

:keyword

'symbol

'(+ 1 1)

#_1

[1 2 3]

{:a 1}

(:a {:a 1})

({:a 1} :a)

{{:a 1} 1}

{[1 2 3] 2}

#{1 2 3}

(fn [a] (+ a 1))

true false

(and true false)

(:author (meta ^{:author "timur"} {:a 1}))

;; naming

;; global
(def my-var 1)

my-var

(def my-fn (fn [a] (+ a 1)))

(defn my-fn [a] (+ a 1))

;; local

(def a 1)

a

(type #'a)

(defn my-apply [f]
  (fn [arg]
    (f arg)))

(def apply-inc (my-apply inc))

(apply-inc 1)

(def apply-dec (my-apply #'dec))

(alter-var-root #'dec (fn [_] inc))

(apply-dec 1)

(ns-name *ns*)
*e
*1
*2

;; destructuring
(let [a 1
      {a :a} {:a 2}
      {:keys [a]} {:a 3}]
  a)

(let [{{b :b} :a} {:a {:b 2}}]
  b)

(let [[f _ t] [1 2 3]]
  [f t])

(let [[f & a] [1 2 3]]
  [f a])

;; conditionals

(if (> a 1)
  'bigger
  'not-bigger)

(when (> a 0)
  'a)

;; everything can be boolean
(when {:a 1}
  'a)

(when :a
  'a)

(when nil
  'a)

(boolean :a)
(boolean {:a 1})
(boolean [])
(boolean {})
(boolean nil)

;; other conditionals

(cond
  (< a 0) 'less
  (= a 0) 'zero
  (> a 0) 'more)

(clojure.walk/macroexpand-all
 '(cond
   (< a 0) 'less
   (= a 0) 'zero
   (> a 0) 'more))

;; collections

;; threading macro
(->> [1 2 3 4]
     (map inc)
     (filter even?))

(macroexpand-1
 '(->> [1 2 3 4]
       (map inc)
       (filter even?)))

(-> {:a {:b 1}}
    (get :a)
    (get :b)
    inc)

;; sequences

(seq [1 2 3])

(seq {:a 1 :b 2})

(->> {:a 1 :b 2}
     (map (fn [[k v]]
            [k (+ v 1)]))
     (into {}))

(->> {:a 1 :b 2}
     (map (fn [[k v]]
            [k (+ v 1)])))

;; all is nil-tolerant!

#_(inc nil)

(vec nil)

(into nil [1 2])

(into {:a 1} nil)

(->> [3 5 7 9]
     (remove odd?)
     first
     (into [1 2 3]))

(let [my-vector [1 2 3]

      result
      (->> [3 5 7 9]
           (remove odd?)
           first)]
  (if (nil? result)
    my-vector
    (vec (conj my-vector result))))

;; advanced conditionals

(let [a 1]
  (cond-> a
    (even? a) (- 1)
    (zero? a) identity
    (odd? a) (+ 1)
    (odd? a) (+ 1)))

'cond->>

(let [value 3]
  (condp = value
    1 "one"
    2 "two"
    3 "three"))

(clojure.walk/macroexpand-all
 '(let [value 3]
    (condp = value
      1 "one"
      2 "two"
      3 "three")))

;; multimethods

(ns-unmap *ns* 'my-add)

(defmulti my-add (fn [a b]
                   (cond
                     (and (string? a) (string? b)) :string
                     (and (number? a) (number? b)) :number)))

(defmethod my-add :number
  [a b]
  (+ a b))

(defmethod my-add :string
  [a b]
  (str a b))

(defmethod my-add :default
  [a b]
  a)

(my-add 1 2)

(my-add "1" "2")

(my-add 1 "2")

;; atoms

(def a (atom 1))

(swap! a inc)

@a

(reset! a 3)

(do (future (Thread/sleep 1000)
            (swap! a #(* % 2))
            (Thread/sleep 1000))

    (future (Thread/sleep 1500)
            (swap! a inc)))

@a
