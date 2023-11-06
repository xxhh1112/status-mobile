(ns status-im2.contexts.wallet.select-address-to-watch.view
  (:require
    clojure.string
    [quo.core :as quo]
    [re-frame.core :as re-frame]
    [react-native.core :as rn]
    [react-native.safe-area :as safe-area]
    [reagent.core :as reagent]
    [status-im2.constants :as constants]
    [status-im2.contexts.wallet.select-address-to-watch.style :as style]
    [utils.i18n :as i18n]
    [utils.re-frame :as rf]))

(defn- address-input
  []
  (let [timer                    (atom nil)
        valid-ens-or-address?    (reagent/atom false)
        on-detect-address-or-ens (fn [_]
                                   (reset! valid-ens-or-address? false)
                                   (when @timer (js/clearTimeout @timer))
                                   (reset! timer (js/setTimeout #(reset! valid-ens-or-address? true)
                                                                2000)))]
    (fn [input-value]
      (let [scanned-address (rf/sub [:wallet/scanned-address])]
        [quo/address-input
         {:on-scan               #(rf/dispatch [:open-modal :scan-address])
          :ens-regex             constants/regx-ens
          :address-regex         constants/regx-address
          :scanned-value         scanned-address
          :on-detect-ens         on-detect-address-or-ens
          :on-detect-address     on-detect-address-or-ens
          :on-change-text        (fn [text]
                                   (when-not (= scanned-address text)
                                     (rf/dispatch [:wallet/clean-scanned-address]))
                                   (reset! input-value text))
          :on-clear              #(rf/dispatch [:wallet/clean-scanned-address])
          :valid-ens-or-address? @valid-ens-or-address?}]))))

(defn f-view
  []
  (let [top                 (safe-area/get-top)
        bottom              (safe-area/get-bottom)
        customization-color (rf/sub [:profile/customization-color])
        input-value         (atom "")]
    (fn []
      (rn/use-effect (fn [] #(rf/dispatch [:wallet/clean-scanned-address])))
      [rn/view
       {:style {:flex       1
                :margin-top top}}
       [quo/page-nav
        {:type      :no-title
         :icon-name :i/close
         :on-press  #(rf/dispatch [:navigate-back])}]
       [quo/text-combinations
        {:container-style style/header-container
         :title           (i18n/label :t/add-address)
         :description     (i18n/label :t/enter-eth)}]
       [address-input input-value]
       [quo/button
        {:customization-color customization-color
         :disabled?           (clojure.string/blank? @input-value)
         :on-press            #(re-frame/dispatch [:navigate-to
                                                   :address-to-watch-edit
                                                   {:address @input-value}])
         :container-style     (style/button-container bottom)}
        (i18n/label :t/continue)]])))

(defn view
  []
  [:f> f-view])
