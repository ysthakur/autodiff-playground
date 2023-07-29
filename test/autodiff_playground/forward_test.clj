(ns autodiff-playground.forward-test
  (:require [clojure.test :refer :all]
            [autodiff-playground.forward :refer :all]))

(deftest all-tests
  (is (= (eval-expr (->Constant 45.3) {} "x") [45.3 0]))
  (is (= (eval-expr (->Var "x") {"x" 2} "x") [2 1]))
  (is (= (eval-expr (->Var "x") {"x" 2} "y") [2 0]))
  (is (= (eval-expr (->Plus (->Times (->Var "x") (->Constant 5)) (->Var "y")) {"x" 4 "y" -1} "x") [19 5])))
