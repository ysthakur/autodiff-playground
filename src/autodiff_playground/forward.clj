(ns autodiff-playground.forward)

(defprotocol Expr
  (eval-expr [expr ctx var]))

(defrecord Constant [value] Expr
  (eval-expr [expr ctx var] [value 0]))

(defrecord Var [name] Expr
  (eval-expr [expr ctx var]
    [(get ctx name)
     (if (= var name) 1 0)]))

(defrecord Plus [lhs rhs] Expr
  (eval-expr [expr ctx var]
    (let [[lv, ld] (eval-expr lhs ctx var)
          [rv, rd] (eval-expr rhs ctx var)]
      [(+ lv rv) (+ ld rd)])))

(defrecord Times [lhs rhs] Expr
  (eval-expr [expr ctx var]
    (let [[lv, ld] (eval-expr lhs ctx var)
          [rv, rd] (eval-expr rhs ctx var)]
      [(* lv rv)
       (+ (* lv rd) (* ld rv))])))
