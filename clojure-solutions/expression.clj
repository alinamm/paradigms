(defn constant [h] (fn [a] h))
(defn variable [h] (fn [a] (get a h)))
(defn func [s] (fn [a b] (fn [h] (s (a h) (b h)))))
(def add (func +))
(def subtract (func -))
(def multiply (func *))
(def divide (func (fn [a b] (/ (double a) b))))
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




(defn proto-get
      "Returns object property respecting the prototype chain"
      ([obj key] (proto-get obj key nil))
      ([obj key default]
       (cond
         (contains? obj key) (obj key)
         (contains? obj :prototype) (proto-get (obj :prototype) key default)
         :else default)))

(defn proto-call
      "Calls object method respecting the prototype chain"
      [this key & args]
      (apply (proto-get this key) this args))
(defn method
      "Creates method"
      [key] (fn [this & args] (apply proto-call this key args)))

(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))

(defn Add [a b] {
                 :evaluate (fn [this h] (+ (evaluate a h) (evaluate b h)))
                 :toString (fn [this] (str "(" "+" " " (toString a) " " (toString b) ")"))
                 :diff     (fn [this v] (Add (diff a v) (diff b v)))
                 })

(defn Subtract [a b] {
                      :evaluate (fn [this h] (- (evaluate a h) (evaluate b h)))
                      :toString (fn [this] (str "(" "-" " " (toString a) " " (toString b) ")"))
                      :diff     (fn [this v] (Subtract (diff a v) (diff b v)))
                      })
(defn Multiply [a b] {
                      :evaluate (fn [this h] (* (evaluate a h) (evaluate b h)))
                      :toString (fn [this] (str "(" "*" " " (toString a) " " (toString b) ")"))
                      :diff     (fn [this v] (Add (Multiply (diff a v) b) (Multiply a (diff b v))))
                      })
(defn Divide [a b] {
                    :evaluate (fn [this h] (/ (double (evaluate a h)) (evaluate b h)))
                    :toString (fn [this] (str "(" "/" " " (toString a) " " (toString b) ")"))
                    :diff     (fn [this v] (Divide (Subtract (Multiply (diff a v) b) (Multiply a (diff b v))) (Multiply b b)))
                    })
(defn Constant [a] {
                    :evaluate (fn [this h] a)
                    :toString (fn [this] (str a))
                    :diff     (fn [this v] (Constant 0))
                    })
(defn Negate [a] {
                  :evaluate (fn [this h] (- (evaluate a h)))
                  :toString (fn [this] (str "(" "negate" " " (toString a) ")"))
                  :diff     (fn [this v] (Negate (diff a v)))
                  })
(defn Variable [a] {
                    :evaluate (fn [this h] (get h a))
                    :toString (fn [this] (str a))
                    :diff     (fn [this v] (if (= (str a) v) (Constant 1) (Constant 0)))
                    })
(declare Cosh)
(defn Sinh [a] {
                :evaluate (fn [this h] (Math/sinh (evaluate a h)))
                :toString (fn [this] (str "(" "sinh" " " (toString a) ")"))
                :diff     (fn [this v] (Multiply (Cosh a) (diff a v)))
                })
(defn Cosh [a] {
                :evaluate (fn [this h] (Math/cosh (evaluate a h)))
                :toString (fn [this] (str "(" "cosh" " " (toString a) ")"))
                :diff     (fn [this v] (Multiply (Sinh a) (diff a v)))
                })
(def mp {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sinh Sinh 'cosh Cosh})
(defn parseArro [arr] (cond
                        (number? arr) (Constant arr)
                        (symbol? arr) (Variable (str arr))
                        (== (count arr) 3) ((get mp (nth arr 0)) (parseArro (nth arr 1)) (parseArro (nth arr 2)))
                        :else ((get mp (nth arr 0)) (parseArro (nth arr 1)))
                        ))
(defn parseObject [s] (parseArro (read-string s)))
