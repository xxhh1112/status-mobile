(ns status-im.ui.screens.wallet.wallet-connect.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.react :as react]
            [status-im.i18n.i18n :as i18n]
            [status-im.utils.security]
            [quo.design-system.colors :as colors]
            [quo.core :as quo]
            [status-im.ui.components.icons.icons :as icons]
            [status-im.ui.screens.browser.accounts :as accounts]
            [status-im.ui.components.bottom-panel.views :as bottom-panel]
            [status-im.ui.components.chat-icon.screen :as chat-icon]))

(defn acc-sheet []
  {:background-color        colors/white
   :border-top-right-radius 16
   :border-top-left-radius  16
   :padding-bottom          1})

(defn circle [{:keys [color size style icon icon-size icon-color]}]
  [react/view
   {:style (merge
            style
            {:width            size
             :height           size
             :background-color color
             :border-radius    (/ size 2)
             :align-items      :center
             :justify-content  :center})}
   (when icon
     [icons/icon icon
      (merge
       {:color    (if icon-color icon-color colors/blue)}
       icon-size)])])

(defn toolbar-selection [{:keys [text background-color on-press]}]
  [react/touchable-opacity {:on-press on-press}
   [react/view {:style {:height 36
                        :min-width 189
                        :border-radius 18
                        :background-color background-color
                        :align-items :center
                        :flex-direction :row
                        :padding-left 12
                        :padding-right 8}}
    [icons/icon
     :main-icons/billfold
     {:color  (:text-05 @colors/theme)
      :width  18
      :height 17.8}]
    [quo/text {:color :inverse
               :style {:margin-left 8
                       :flex-grow 1}}
     text]
    [icons/icon
     :main-icons/chevron-down
     {:color  (:text-05 @colors/theme)
      :width  24
      :height 24}]]])

(defview success-sheet-view []
  (letsubs [{:keys [name icons]} [:wallet-connect/proposal-metadata]
            dapps-account [:dapps-account]]
    (let [icon-uri (when (and icons (> (count icons) 0)) (first icons))]
      [react/view {:flex 1 :align-items :center}
       [react/view {:flex 1 :align-items :center}
        [react/view {:flex-direction :row
                     :align-items :center
                     :margin-top 10
                     :margin-bottom 16}
         [react/image {:style  {:width            40
                                :height           40
                                :background-color (:interactive-02 @colors/theme)
                                :resize-mode      :cover
                                :border-radius    20}
                       :source {:uri icon-uri}}]
         [circle {:color (:interactive-02 @colors/theme)
                  :size  4
                  :style {:margin-right 4}}]
         [circle {:color (:interactive-02 @colors/theme)
                  :size  4
                  :style {:margin-right 4}}]
         [circle {:color (:interactive-02 @colors/theme)
                  :size  4
                  :style {:margin-right 8}}]
         [circle {:color (:positive-03 @colors/theme)
                  :size  24
                  :style {:margin-right 8}
                  :icon :main-icons/checkmark
                  :icon-size {:width 16 :height 16}
                  :icon-color colors/white-persist}]
         [circle {:color (:interactive-02 @colors/theme)
                  :size  4
                  :style {:margin-right 4}}]
         [circle {:color (:interactive-02 @colors/theme)
                  :size  4
                  :style {:margin-right 4}}]
         [circle {:color (:interactive-02 @colors/theme)
                  :size  4
                  :style {:margin-right 8}}]
         [chat-icon/custom-icon-view-list (:name dapps-account) (:color dapps-account)]]]
       [react/view {:flex 1
                    :align-items :center}
        [react/view {:style {:flex-direction :row}}
         [quo/text {:weight :bold
                    :size   :large}
          (str name " ")]
         [quo/text {:weight :regular
                    :size   :large
                    :color  :secondary
                    :style  {:margin-bottom 16}}
          (i18n/label :t/wallet-connect-app-connected)]]
        [react/view {:padding-horizontal 16 :width "100%" :flex-direction :row}
         [react/view {:flex 1}
          [quo/button
           {:theme :monocromatic
            :on-press #(re-frame/dispatch [:bottom-sheet/hide])}
           (i18n/label :t/wallet-connect-go-back)]]]]])))

(defn session-proposal-sheet [{:keys [name icons]}]
  (let [visible-accounts @(re-frame/subscribe [:visible-accounts-without-watch-only])
        dapps-account @(re-frame/subscribe [:dapps-account])
        icon-uri (when (and icons (> (count icons) 0)) (first icons))
        accounts  @(re-frame/subscribe [:multiaccount/visible-accounts])]
    [react/view {:style acc-sheet}
     [react/view {:background-color :white
                  :width "100%"
                  :align-items :center
                  :padding-top 30
                  :padding-bottom 80
                  :border-top-right-radius 16
                  :border-top-left-radius  16}
      [react/view {:flex-direction :row
                   :align-items :center
                   :margin-top 10
                   :margin-bottom 16}
       [react/image {:style  (merge {:width            40
                                     :height           40
                                     :background-color (:interactive-02 @colors/theme)
                                     :resize-mode      :cover
                                     :border-radius    20})
                     :source {:uri icon-uri}}]
       [circle {:color (:interactive-02 @colors/theme)
                :size  4
                :style {:margin-right 4}}]
       [circle {:color (:interactive-02 @colors/theme)
                :size  4
                :style {:margin-right 4}}]
       [circle {:color (:interactive-02 @colors/theme)
                :size  4
                :style {:margin-right 8}}]
       [circle {:color (:interactive-02 @colors/theme)
                :size  24
                :style {:margin-right 8}
                :icon :main-icons/checkmark
                :icon-size {:width 16 :height 16}}]
       [circle {:color (:interactive-02 @colors/theme)
                :size  4
                :style {:margin-right 4}}]
       [circle {:color (:interactive-02 @colors/theme)
                :size  4
                :style {:margin-right 4}}]
       [circle {:color (:interactive-02 @colors/theme)
                :size  4
                :style {:margin-right 8}}]
       [chat-icon/custom-icon-view-list (:name dapps-account) (:color dapps-account)]]
      [react/view {:flex 1
                   :align-items :center}
       [react/view {:style {:flex-direction :row}}
        [quo/text {:weight :bold
                   :size   :large}
         (str name " ")]
        [quo/text {:weight :regular
                   :size   :large
                   :color  :secondary
                   :style  {:margin-bottom 16}}
         (i18n/label :t/wallet-connect-proposal-title)]]
       [toolbar-selection {:icon :main-icons/billfold
                           :background-color (:color dapps-account)
                           :text (:name dapps-account)
                           :multiaccounts accounts
                           :on-press #(re-frame/dispatch [:bottom-sheet/show-sheet
                                                          {:content (accounts/accounts-list visible-accounts dapps-account)}])}]
       [quo/text {:align :center
                  :color :secondary
                  :style {:margin-vertical 16}}
        (i18n/label :t/wallet-connect-proposal-description {:name name})]
       [react/view {:padding-horizontal 16 :width "100%" :align-items :stretch :justify-content :space-between :flex-direction :row :margin-top 6}
        [react/view {:flex 1
                     :margin-right 4}
         [quo/button
          {:on-press #(re-frame/dispatch [:wallet-connect/reject-proposal])}
          (i18n/label :t/cancel)]]
        [react/view {:flex 1
                     :margin-left 4}
         [quo/button
          {:theme     :accent
           :on-press  #(re-frame/dispatch [:wallet-connect/approve-proposal dapps-account])}
          (i18n/label :t/connect)]]]]]]))

(def success-sheet
  {:content success-sheet-view})

(def bottom-sheet
  {:content success-sheet-view})

(defview wallet-connect-proposal-sheet []
  (letsubs [proposal-metadata [:wallet-connect/proposal-metadata]]
    [bottom-panel/animated-bottom-panel
     proposal-metadata
     session-proposal-sheet
     #(re-frame/dispatch [:hide-wallet-connect-sheet])]))
