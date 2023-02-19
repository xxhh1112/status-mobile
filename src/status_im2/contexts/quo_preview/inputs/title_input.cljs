(ns status-im2.contexts.quo-preview.inputs.title-input
  (:require [quo2.foundations.colors :as colors]
            [react-native.core :as rn]
            [reagent.core :as reagent]
            [quo2.core :as quo]
            [react-native.blur :as blur]
            [status-im2.contexts.quo-preview.preview :as preview]))

(def descriptor
  [{:label "disabled?"
    :key   :disabled?
    :type  :boolean}
   {:label "blur?"
    :key   :blur?
    :type  :boolean}
   {:label   "Cursor Color"
    :key     :color
    :type    :select
    :options (map (fn [color]
                    (let [key (get color :name)]
                      {:key key :value key}))
                  (quo/picker-colors))}])

(defn cool-preview
  []
  (let [state          (reagent/atom {:color     nil
                                      :blur?     false
                                      :disabled? false})
        max-length     24
        on-change-text (fn [_v]
                         (println (str "cool-preview -> on-change-text called " _v)))]
    (fn []
      [rn/touchable-without-feedback {:on-press rn/dismiss-keyboard!}
       [rn/view {:padding-bottom 150}
        [rn/view {:flex 1}]
        [preview/customizer state descriptor]
        (if (:blur? @state)
          [blur/view
           {:background-color  colors/neutral-80-opa-80
            :padding-vertical  60
            :flex-direction    :row
            :margin-horizontal 20
            :justify-content   :center}
           [quo/title-input
            {:max-length          max-length
             :default-value       ""
             :on-change-text      on-change-text
             :disabled?           (:disabled? @state)
             :customization-color (:color @state)
             :blur?               (:blur? @state)
             :placeholder         "type something here"}]]
          [rn/view
           {:padding-vertical  60
            :flex-direction    :row
            :margin-horizontal 20
            :justify-content   :center}
           [quo/title-input
            {:max-length          max-length
             :default-value       ""
             :on-change-text      on-change-text
             :disabled?           (:disabled? @state)
             :customization-color (:color @state)
             :blur?               (:blur? @state)
             :placeholder         "type something here"}]])]])))

(defn preview-title-input
  []
  [rn/view
   {:background-color (colors/theme-colors colors/white
                                           colors/neutral-90)
    :flex             1}
   [rn/flat-list
    {:flex                         1
     :keyboard-should-persist-taps :always
     :header                       [cool-preview]
     :key-fn                       str}]])
