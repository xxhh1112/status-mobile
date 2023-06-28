(ns quo2.components.buttons.button.view
  (:require [quo2.components.icon :as quo2.icons]
            [quo2.components.markdown.text :as text]
            [quo2.theme :as theme]
            [react-native.core :as rn]
            [react-native.blur :as blur]
            [reagent.core :as reagent]
            [quo2.components.buttons.button.style :as style]
            [quo2.components.buttons.button.type-values :as type-values]
            [quo2.foundations.customization-colors :as customization-colors]))

(defn- button-internal
  "with label
   [button opts \"label\"]
   opts
   {:type   :primary/:positive/:grey/:dark-grey/:outline/:ghost/
            :danger/:black  
    :background nil/:blur/:photo
    :size   40 [default] /32/24/56
    :icon   true/false
    :disabled true/false
    :above :icon-keyword 
    :before :icon-keyword
    :after  :icon-keyword
    :on-press callback
    :on-long-press callback}
   
   only icon
   [button {} :i/close-circle]"
  [_ _]
  (let [pressed-in (reagent/atom false)]
    (fn
      [{:keys [on-press disabled type size before after above icon-secondary-no-color
               width customization-color theme pressed background on-long-press accessibility-label
               icon-no-color style inner-style
               override-before-margins override-after-margins icon-size icon-container-size
               icon-container-rounded?]
        :or   {size 40}}
       children]
      (let [pressed? (or @pressed-in pressed)
            {:keys [icon-color icon-secondary-color background-color label-color border-color
                    blur-type blur-overlay-color icon-background-color]}
            (type-values/get-values {:customization-color customization-color
                                     :theme               theme
                                     :type                type
                                     :background          background
                                     :pressed?            pressed?})
            icon-only? (not (or (number? children) (string? children)))
            blur? (= :blur background)
            blur-state (if blur? :blurred :default)
            icon-size (or icon-size (when (= 24 size) 12))
            icon-secondary-color (or icon-secondary-color icon-color)]
        [rn/touchable-without-feedback
         {:disabled            disabled
          :accessibility-label accessibility-label
          :on-press-in         #(reset! pressed-in true)
          :on-press-out        #(reset! pressed-in nil)
          :on-press            on-press
          :on-long-press       on-long-press}
         [rn/view
          {:style (merge
                   (style/shape-style-container type icon-only? size)
                   {:width width}
                   style)}
          [rn/view
           {:style (merge
                    (style/style-container {:type             type
                                            :size             size
                                            :disabled         disabled
                                            :background-color background-color
                                            :border-color     border-color
                                            :icon-only?       icon-only?
                                            :above            above
                                            :width            width
                                            :before           before
                                            :after            after
                                            :blur             blur?})
                    inner-style)}

           (when (keyword? customization-color)
             [customization-colors/overlay
              {:theme    theme
               :pressed? pressed?}])
           (when (= background :blurred)
             [blur/view
              {:blur-radius   20
               :blur-type     blur-type
               :overlay-color blur-overlay-color
               :style         style/blur-view}])
           (when above
             [rn/view
              [quo2.icons/icon above
               {:container-style {:margin-bottom 2}
                :color           icon-secondary-color
                :size            icon-size}]])
           (when before
             [rn/view
              {:style (style/before-icon-style
                       {:override-margins        override-before-margins
                        :size                    size
                        :icon-container-size     icon-container-size
                        :icon-background-color   (get icon-background-color blur-state)
                        :icon-container-rounded? icon-container-rounded?
                        :icon-size               icon-size})}
              [quo2.icons/icon before
               {:color icon-secondary-color
                :size  icon-size}]])
           [rn/view
            (cond
              (or icon-only? icon-no-color)
              [quo2.icons/icon children
               {:color    icon-color
                :no-color icon-no-color
                :size     icon-size}]

              (string? children)
              [text/text
               {:size            (when (#{56 24} size) :paragraph-2)
                :weight          :medium
                :number-of-lines 1
                :style           {:z-index 2
                                  :color   label-color}}

               children]

              (vector? children)
              children)]
           (when after
             [rn/view
              {:style (style/after-icon-style
                       {:override-margins        override-after-margins
                        :size                    size
                        :icon-container-size     icon-container-size
                        :icon-background-color   (get icon-background-color blur-state)
                        :icon-container-rounded? icon-container-rounded?
                        :icon-size               icon-size})}
              [quo2.icons/icon after
               {:no-color icon-secondary-no-color
                :color    icon-secondary-color
                :size     icon-size}]])]]]))))

(defn button-wrapper
  [{:keys [type customization-color] :or {type :primary} :as props} label]
  (let [color (case type
                :primary  (or customization-color :primary)
                :positive :positive
                :danger   :danger
                nil)]
    [button-internal (assoc props :customization-color :blue :type type) label]))

(def button (theme/with-theme button-wrapper))
