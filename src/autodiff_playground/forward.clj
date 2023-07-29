(ns autodiff-playground.forward)

(defprotocol Expr
  (eval-expr [expr ctx])
  (differentiate [expr ctx var]))

(defn autodiff [expr ctx var]
  (let [derivative (differentiate expr ctx var)]
    (eval-expr derivative ctx)))

(defrecord Constant [value] Expr
  (eval-expr [expr ctx] value)
  (differentiate [expr ctx var] (->Constant 0)))

;; An independent variable
(defrecord IndVar [name] Expr
  (eval-expr [expr ctx] (get ctx name))
  (differentiate [expr ctx var]
    (if (= var name) (->Constant 1) (->Constant 0))))

;; A dependent variable
(defrecord DepVar [name value] Expr
  (eval-expr [expr ctx] (eval-expr value ctx))
  (differentiate [expr ctx var] (differentiate value ctx var)))

(defrecord Plus [lhs rhs] Expr
  (eval-expr [expr ctx]
    (+ (eval-expr lhs ctx) (eval-expr rhs ctx)))
  (differentiate [expr ctx var]
    (Plus. (differentiate lhs ctx var) (differentiate rhs ctx var))))

(defrecord Times [lhs rhs] Expr
  (eval-expr [expr ctx]
    (* (eval-expr lhs ctx) (eval-expr rhs ctx)))
  (differentiate [expr ctx var]
    (Plus.
      (Times. lhs (differentiate rhs ctx var))
      (Times. (differentiate lhs ctx var) rhs))))
