(ns status-im2.contexts.syncing.enter-qr-code-manually.view
  (:require [react-native.core :as rn]
            [taoensso.timbre :as log]
            [quo2.core :as quo]
            [utils.re-frame :as rf]
            [reagent.core :as reagent]
            [clojure.string :as string]
            [status-im2.contexts.syncing.utils :as sync-utils]
            [utils.i18n :as i18n]
            [quo2.foundations.colors :as colors]))

(defn view
  []
  (let [opts     (rf/sub [:get-screen-params])
        qr-input (reagent/atom "")]
    (fn []
      [rn/view
       {:style {:justify-content :center
                :align-items     :center
                :padding         10
                :margin-top      50}}

       [quo/button
        {:type                :positive
         :accessibility-label :manual-qr-clear-button
         :size                24
         :on-press            #(rf/dispatch [:navigate-back])
         :style               {:margin-top    10
                               :margin-bottom 10
                               :margin-left   10
                               :align-items   :flex-start
                               :width         "100%"}}
        "Go Back"]

       [rn/text
        {:style {:font-size     22
                 :color         :white
                 :margin-bottom 10}}
        "Manual input screen for QR code"]
       [rn/text
        {:style {:font-size     12
                 :color         :white
                 :margin-bottom 20
                 :font-style    :italic}}
        "Note: This is a UI built specifically for E2E automation and won't be present in the PR, nightly and release builds.If you want to disable this screen go to .env file and set QR_READ_TEST_MENU=0 and rebuild the app!"]
       [quo/input
        {:type           :text
         :multiline      true
         :height         80
         :placeholder    "paste a valid QR link over here"
         :value          @qr-input
         :on-change-text #(reset! qr-input %)}]

       [rn/view {:style {:flex-direction :row}}

        [quo/button
         {:type                :primary
          :accessibility-label :manual-qr-submit-button
          :style               {:margin-top 10}
          :size                24
          :on-press            #(if (sync-utils/valid-connection-string? @qr-input)
                                  (rf/dispatch [:syncing/input-connection-string-for-bootstrapping
                                                @qr-input])
                                  (rf/dispatch [:toasts/upsert
                                                {:icon :i/info
                                                 :icon-color colors/danger-50
                                                 :override-theme :light
                                                 :text (i18n/label
                                                        :t/error-this-is-not-a-sync-qr-code)}]))}
         "Submit"]

        [quo/button
         {:type                :danger
          :accessibility-label :manual-qr-clear-button
          :size                24
          :on-press            #(reset! qr-input "")
          :style               {:margin-top  10
                                :margin-left 10}}
         "Clear"]]])))
