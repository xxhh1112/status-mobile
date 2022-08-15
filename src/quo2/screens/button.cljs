(ns quo2.screens.button
  (:require [reagent.core :as reagent]
            [quo.react-native :as rn]
            [quo.previews.preview :as preview]
            [quo.design-system.colors :as colors]
            [cljs.tools.reader.edn :as edn]
            [quo2.components.button :as quo2])
            (:require-macros  [status-im.utils.slurp :refer [slurp]])
            )

(def descriptor (edn/read-string (slurp "./src/quo2/screens/button-options.edn")))

(defn cool-preview []
  (let [state  (reagent/atom {:label "Press Me"
                              :size  40})
        label  (reagent/cursor state [:label])
        before (reagent/cursor state [:before])
        after  (reagent/cursor state [:after])
        above  (reagent/cursor state [:above])
        icon  (reagent/cursor state [:icon])]
    (fn []
      [rn/view {:margin-bottom 50
                :padding       16}
       [rn/view {:flex 1}
        [preview/customizer state descriptor]]
       [rn/view {:padding-vertical 60
                 :flex-direction   :row
                 :justify-content  :center}
        [quo2/button (merge (dissoc @state
                                    :theme :before :after)
                            {:on-press #(println "Hello world!")}
                            (when @above
                              {:above :main-icons2/placeholder})
                            (when @before
                              {:before :main-icons2/placeholder})
                            (when @after
                              {:after :main-icons2/placeholder}))
         (if @icon :main-icons2/placeholder @label)]]])))

(defn preview-button []
  [rn/view {:background-color (:ui-background @colors/theme)
            :flex             1}
   [rn/flat-list {:flex                      1
                  :keyboardShouldPersistTaps :always
                  :header                    [cool-preview]
                  :key-fn                    str}]])