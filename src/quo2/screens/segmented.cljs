(ns quo2.screens.segmented
  (:require [quo.react-native :as rn]
            [quo.design-system.colors :as colors]
            [quo.previews.preview :as preview]
            [cljs.tools.reader.edn :as edn]
            [quo2.components.segmented-control :as quo2]
            [reagent.core :as reagent])
            (:require-macros  [status-im.utils.slurp :refer [slurp]])
            )

(def descriptor (edn/read-string (slurp "./src/quo2/screens/segmented-options.edn")))

(defn cool-preview []
  (let [state  (reagent/atom {:size  32})]
    (fn []
      [rn/view {:margin-bottom 50
                :padding       16}
       [rn/view {:flex 1}
        [preview/customizer state descriptor]]
       [rn/view {:padding-vertical 60}
        [quo2/segmented-control (merge @state
                                       {:default-active 1
                                        :data [{:id 1 :label "Tab 1"}
                                               {:id 2 :label "Tab 2"}
                                               {:id 3 :label "Tab 3"}
                                               {:id 4 :label "Tab 4"}]
                                        :on-change #(println "Active tab" %)})]]
       [rn/view {:padding-vertical 60}
        [quo2/segmented-control (merge @state
                                       {:default-active 1
                                        :data [{:id 1 :label "Tab 1"}
                                               {:id 2 :label "Tab 2"}]
                                        :on-change #(println "Active tab" %)})]]])))

(defn preview-segmented []
  [rn/view {:background-color (:ui-background @colors/theme)
            :flex             1}
   [rn/flat-list {:flex                      1
                  :keyboardShouldPersistTaps :always
                  :header                    [cool-preview]
                  :key-fn                    str}]])