(ns autodiff-playground.symbolic-test
  (:require [clojure.test :refer :all]
            [autodiff-playground.symbolic :refer :all]))

(deftest eval-const-test
  (is (= (eval-expr (->Constant 45.3) {}) 45.3)))

(deftest eval-ind-var-test
  (is (= (eval-expr (->IndVar "x") {"x" 2}) 2)))

(deftest eval-complex-test
  (testing "evaluating more complex expressions")
  (is (= (eval-expr (->Times (->Plus (->IndVar "x") (->Constant 5)) (->IndVar "y")) {"x" 4 "y" -1}) -9)))

(deftest differentiate-tests
  ;; Technically these all need to have values for x but I'm lazy so I'm gonna just use {} (no variables)
  (testing "basic derivatives"
    (is (= (symbolic-diff (->Constant 5) {} "x") 0))
    (is (= (symbolic-diff (->IndVar "x") {} "x") 1))
    (is (= (symbolic-diff (->IndVar "y") {} "x") 0)))
  (testing "functions"
    (is (= (symbolic-diff (->Plus (->IndVar "x") (->IndVar 5)) {} "x")
           1))
    (is (= (symbolic-diff (->Times (->Constant 2) (->IndVar "x")) {"x" 12345} "x") ; The value of x doesn't matter here
           2)))
  (testing "dependent variables"
    (is (= (symbolic-diff (->DepVar "y" (->Times (->Constant 3) (->IndVar "x"))) {"x" 12345} "x") ; The value of x doesn't matter here
           3))
    (is (= (symbolic-diff (->Times (->IndVar "x") (->DepVar "y" (->Times (->Constant 3) (->IndVar "x")))) {"x" 2} "x")
           12))))
