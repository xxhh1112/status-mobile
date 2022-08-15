(ns quo2.screens.text
  (:require [quo.react-native :as rn]
            [quo.previews.preview :as preview]
            [reagent.core :as reagent]
            [quo2.components.text :as quo2]
            [cljs.tools.reader.edn :as edn]
            [quo.design-system.colors :as colors])
            (:require-macros  [status-im.utils.slurp :refer [slurp]])
            )

(def descriptor (edn/read-string (slurp "./src/quo2/screens/text-options.edn")))

(defn cool-preview []
  (let [state     (reagent/atom {})]
    (fn []
    ;;  (js/console.log descriptor)
     (js/console.log descriptor )

      [rn/view {:margin-bottom 50
                :padding       16}
       [preview/customizer state descriptor]
       [rn/view {:padding-vertical 60}
        [quo2/text @state
         "The quick brown fox jumped over the lazy dog."]]])))

(defn preview-text []
  [rn/view {:background-color (:ui-background @colors/theme)
            :flex             1}
   [rn/flat-list {:flex                      1
                  :keyboardShouldPersistTaps :always
                  :header                    [cool-preview]
                  :key-fn                    str}]])