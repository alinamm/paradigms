(defn constant [h] (fn [a] h))
(defn variable [h] (fn [a] (get a h)))
(defn add [a b] (fn [h] (+ (a h) (b h))))
(defn subtract [a b] (fn [h] (- (a h) (b h))))
(defn multiply [a b] (fn [h] (* (a h) (b h))))
(defn divide [a b] (fn [h] (/ (double (a h)) (b h))))
(defn negate [a] (fn [h] (- (a h))))
(defn sinh [a] (fn [h] (Math/sinh (a h))))
(defn cosh [a] (fn [h] (Math/cosh (a h))))
(def m {'+ add '- subtract '* multiply '/ divide 'negate negate 'sinh sinh 'cosh cosh})
(defn parseArr [arr] (cond
                       (number? arr) (constant arr)
                       (symbol? arr) (variable (str arr))
                       (== (count arr) 3) ((get m (nth arr 0)) (parseArr (nth arr 1)) (parseArr (nth arr 2)))
                       :else ((get m (nth arr 0)) (parseArr (nth arr 1)))
                       ))
(defn parseFunction [s] (parseArr (read-string s)))

;(def expr
;  (subtract
;    (multiply
;      (constant 2)
;      (variable "x"))
;    (constant 3)))
;
;(println (expr {"x" 2}))
(def expr (parseFunction "(+ 1 1)"))
(println (expr {"x" 2}))