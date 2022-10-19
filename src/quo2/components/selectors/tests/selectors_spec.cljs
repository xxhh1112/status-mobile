(ns quo2.components.selectors.tests.selectors-spec
  (:require ["@testing-library/react-native" :as rtl]
            [quo.react-native :as rn]
            [mocks.js-dependencies]
            [quo2.components.selectors.selectors :as selectors]
            [reagent.core :as reagent]
            [test.jest.matchers]))

(defn render
  ([]
   (render {}))
  ([opts]
   (rtl/render (reagent/as-element [rn/text "hello world"]))))

;; (defn render-toggle
;;   ([]
;;    (render {}))
;;   ([opts]
  

;;      (rtl/render [selectors/toggle {:disabled? (:disabled? false)}])))

(js/global.test "initial screen"
                (fn []
                  (render)
                  (-> (js/expect  (rtl/screen.getByText "hello world"))
                      (.toBeTruthy))))

;; (js/global.test "render selector"
;;                 (fn []
;;                   (render-toggle)
;;                   (-> (js/expect  (rtl/screen.getByText "toggle-component"))
;;                       (.toBeTruthy))))

