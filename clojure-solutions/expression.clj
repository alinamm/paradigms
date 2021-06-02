(load-file "proto.clj")

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

(defn Myconstructor
  ([ctor, proto, f, s, d] (fn [& args] (apply ctor {:prototype proto, :f f :s s :d d} args))))

(def a (field :a))
(def b (field :b))
(def f (field :f))
(def s (field :s))
(def d (field :d))
(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))

(defn BinaryOperation [t a b] (assoc t :a a :b b))

(def BinaryOperationPrototype
  {:evaluate (fn [t h] ((f t) (evaluate (a t) h) (evaluate (b t) h)))
   :toString (fn [t] (str "(" (s t) " " (toString (a t)) " " (toString (b t)) ")"))
   :diff     (fn [t v] ((d t) (a t) (b t) v))
   })

(defn BinFabric [f s d] (Myconstructor BinaryOperation BinaryOperationPrototype f s d))

(defn UnaryOperation [this a] (assoc this :a a))

(def UnaryOperationPrototype
  {:evaluate (fn [t h] ((f t) (evaluate (a t) h)))
   :toString (fn [t] (str "(" (s t) " " (toString (a t)) ")"))
   :diff     (fn [t v] ((d t) (a t) v))
   })

(defn UnFabric [f s d] (Myconstructor UnaryOperation UnaryOperationPrototype f s d))

(def Add (BinFabric +
                    "+"
                    (fn [a b v] (Add (diff a v) (diff b v)))))
(def Subtract (BinFabric -
                         "-"
                         (fn [a b v] (Subtract (diff a v) (diff b v)))))
(def Multiply (BinFabric *
                         "*"
                         (fn [a b v] (Add (Multiply (diff a v) b) (Multiply a (diff b v))))))

(def Divide (BinFabric (fn [x, y] (/ (double x) y))
                       "/"
                       (fn [a b v] (Divide (Subtract (Multiply (diff a v) b) (Multiply a (diff b v))) (Multiply b b)))))
(def Negate (UnFabric -
                      "negate"
                      (fn [a v] (Negate (diff a v)))))

(declare Cosh)

(def Sinh (UnFabric (fn [a] (Math/sinh a))
                    "sinh"
                    (fn [a v] (Multiply (Cosh a) (diff a v)))))

(def Cosh (UnFabric (fn [a] (Math/cosh a))
                    "cosh"
                    (fn [a v] (Multiply (Sinh a) (diff a v)))))

(declare Constant)

(declare ZERO)
(declare ONE)
(def ConstantPrototype
  {:evaluate (fn [t h] (a t))
   :toString (fn [t] (str (a t)))
   :diff     (fn [t v] ZERO)
   })

(defn ConstantCons [t s] (assoc t :a (double s)))


(def Constant (constructor ConstantCons ConstantPrototype))

(defn VariableCons [t s] (assoc t :a s))

(def VariablePrototype
  {:evaluate (fn [t h] (h (a t)))
   :toString (fn [t] (a t))
   :diff     (fn [t v] (if (= (str (a t)) v) ONE ZERO))})

(def Variable (constructor VariableCons VariablePrototype))

(def mp {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sinh Sinh 'cosh Cosh})

(defn parseArro [arr] (cond
                        (number? arr) (Constant arr)
                        (symbol? arr) (Variable (str arr))
                        (== (count arr) 3) ((get mp (nth arr 0)) (parseArro (nth arr 1)) (parseArro (nth arr 2)))
                        :else ((get mp (nth arr 0)) (parseArro (nth arr 1)))
                        ))
(defn parseObject [s] (parseArro (read-string s)))

(def ZERO (Constant 0))
(def ONE (Constant 1))