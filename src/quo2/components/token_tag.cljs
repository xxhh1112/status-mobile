(ns quo2.components.token-tag
  (:require [quo2.foundations.colors :as colors]
            [quo.react-native :as rn]
            [quo.theme :as theme]
            [status-im.ui.components.icons.icons :as icons]
            [quo2.components.text :as text]))

(def themes {:light {:background-color colors/neutral-20}
             :dark {:background-color colors/neutral-80}})

(defn get-value-from-size [size big-option small-option]
  (if  (= size :big) big-option small-option))

(defn style-container [size border-color is-required]
  (merge {:height             32
          :min-width         (get-value-from-size size 94 76)
          :align-items        :center
          :flex-direction     :row
          :left             0
          :border-radius      100
          :padding-right      10}
         (when is-required {:border-color     border-color
                            :border-width     1})))

(defn token-tag
  "[token-tag opts \"label\"]
   opts
   {
    :token string
    :value string
    :size :small/:big
    :icon-name :icon-name
    :required-icon :icon-name
    :border-color :color
    :is-required true/false
    :is-purchasable true/false
    }"
  [_ _]
  (fn [{:keys [token value size icon-name border-color is-required is-purchasable required-icon]
        :or {size :small border-color (if (=  (theme/get-theme) :dark) colors/purple-60 colors/purple-50) required-icon (if (=  (theme/get-theme) :dark) :main-icons2/required-checkmark-dark :main-icons2/required-checkmark)}}]
    [rn/view {:style   (merge (style-container size border-color is-required)  (get themes (theme/get-theme)))}
     [rn/view {:style {:margin-left 2
                       :margin-right  (get-value-from-size size 8 6)}}
      [icons/icon icon-name {:no-color true
                             :size    24}]]
     [text/text  {:weight          :medium
                  :number-of-lines 1
                  :size (get-value-from-size size :paragraph-2 :label)
                  :padding 2
                  :padding-top 12
                  :margin-right (get-value-from-size size  12 10)} value " " token]
     (when (or is-required is-purchasable) [rn/view {:style {:display :flex
                                                             :align-items :center
                                                             :justify-content :center
                                                             :position :absolute
                                                             :background-color  (if is-required border-color colors/neutral-50)
                                                             :border-radius      100
                                                             :right     -20
                                                             :bottom      16
                                                             :margin-left 2
                                                             :margin-right (get-value-from-size size 8 6)}}
                                            (when (and is-required required-icon) [icons/icon required-icon {:no-color true
                                                                                                             :size    4}])
                                            (when is-purchasable [icons/icon (if (=  (theme/get-theme) :dark) :main-icons2/purchasable-dark :main-icons2/purchasable) {:no-color true
                                                                                                                                                                       :size    4}])])]))
