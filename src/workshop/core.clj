(ns workshop.core
  (:require
   [workshop.reference :as *ref]))

;; eval buffer

;; eval expression

;; # типы данных

1

"123"

#".*"

:keyword

:slava/keyword

::keyword

::*ref/my-symbol

'symbol

;; персистентные структуры данных

;; lists

'(+ 1 1)

[1 2 3]

{:a 1}

;; keyword is a function

(:a {:a 1})

;; hash map is a function
({:a 1} :a)

{{:a 1} 1}

{[1 2 3] 2}

#{1 2 3}

true false

(and true false)

(or true false)

;; метаданные

{:author "timur"}
{:patient {:id "123"}}

(:author
 (meta ^{:author "timur"}
       {:a 1}))

;; макросы чтения

#_1

;; # операции на структурах данных

;; 10 типов данных + 10^100 функций (стандартной библиотеки)
(assoc {} :a 1)

(dissoc {:a 1} :a)

(select-keys {:a 1 :b 2 :c 3} [:a :b])

(get {:a 1} :a)

(conj [1] 2) 

(get [1 2 3] 1)

(conj '(1) 2)

;; # именование

;; глобальное
(def a 1)

1

a

(def my-fn (fn [a] (+ a 1)))

(defn my-fn
  [a]
  (+ a 1))

;; локальное

(let [a 2] a)

;; варки это не значения 
(type #'a)

(defn my-apply [f]
  (fn [arg]
    (f arg)))

;; inc => var => fn

(def apply-inc (my-apply inc))

(apply-inc 1)

(def apply-dec (my-apply #'dec))

(alter-var-root #'dec (fn [_] inc))

(apply-dec 1)

(= (apply-inc 1) (apply-dec 1))

;; некоторые динамические переменные
(ns-name *ns*)

*e
*1
*2

;; # деструктуризация
(let [a 1
      {a :a b :b d :d} {:a 2 :b 3 :d 4}
      {:keys [a b d]} {:a 2 :b 3 :d 4}]
  [a b d])

(let [{{b :b} :a} {:a {:b 2}}]
  b)

(let [[f _ t] [1 2 3]]
  [f t])

(let [[f & a] [1 2 3]]
  [f a])

;; ! упражнение 1

;; # условные выражения

(if (> a 1)
  'bigger
  'not-bigger)

(when (> a 0)
  'a)

;; все значения приводимы к булеву типу
(when {:a 1}
  'a)

(when :a
  'a)

(when nil
  'a)

(boolean :a)
(boolean {:a 1})
(boolean nil)

;; другие условные выражения

(cond
  (< a 0) 'less
  (= a 0) 'zero
  (> a 0) 'more)

(clojure.walk/macroexpand-all
 '(cond
   (< a 0) 'less
   (= a 0) 'zero
   (> a 0) 'more))

;; # сочетание вычислений

;; с макросом для коллекций
(->> [1 2 3 4]
     (map inc)
     (filter even?))

(macroexpand-1
 '(->> [1 2 3 4]
       (map inc)
       (filter even?)))

;; с макросом для значений
(-> {:a {:b 1}}
    (get :a)
    (get :b)
    inc)

;; абстрактный тип данных последовательности

(seq [1 2 3])

(seq {:a 1 :b 2})

(->> {:a 1 :b 2}
     (map (fn [[k v]]
            [k (+ v 1)]))
     (into {}))

(->> {:a 1 :b 2}
     (map (fn [[k v]]
            [k (+ v 1)])))

;; (почти) все работает с nil

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

#_(inc nil)

;; # операции на коллекциях ленивые 

(->> [1 2 3 4]
     (map (fn [v] (prn v) (* v v))))

(def my-list
  (->> [1 2 3 4]
       (map (fn [v] (prn v) (* v v)))))

(def v (take 2 my-list))

;; if you do sideffects - use doall
(def my-list
  (->> [1 2 3 4]
       (map (fn [v] (prn v) (* v v)))
       doall))

;; ! упражнение

;; # более сложные условные выражения

(let [a 1]
  (cond-> a
    (even? a) (- 1)
    (zero? a) identity
    (odd? a) (+ 1)
    (odd? a) (+ 1)))

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

;; # мультиметоды

(ns-unmap *ns* 'my-add)

(defmulti my-add
  (fn [a b]
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

;; # атомы

(def a (atom 1))

(swap! a inc)

@a

(reset! a 3)

(do (future (Thread/sleep 1000)
            (swap! a #(* % 2)))

    (future (Thread/sleep 1500)
            (swap! a inc)))

@a

;; ! упражнение 2

;; ИТОГИ

;; Кложурка - динамический язык
;; вычисления с использованием встроенных структур данных описываются лаконично
;; их следует представлять как обработку последовательностей
;; Разрабатывать надо интерактивно
;; эффективная разработка невозможна без правильно настроенного окружения
