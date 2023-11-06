(ns status-im2.contexts.wallet.account.tabs.view
  (:require
    [clojure.string :as string]
    [react-native.core :as rn]
    [status-im2.contexts.wallet.account.tabs.about.view :as about]
    [status-im2.contexts.wallet.account.tabs.dapps.view :as dapps]
    [status-im2.contexts.wallet.common.activity-tab.view :as activity]
    [status-im2.contexts.wallet.common.collectibles-tab.view :as collectibles]
    [status-im2.contexts.wallet.common.empty-tab.view :as empty-tab]
    [status-im2.contexts.wallet.common.temp :as temp]
    [status-im2.contexts.wallet.common.token-value.view :as token-value]
    [status-im2.contexts.wallet.common.utils :as utils]
    [utils.i18n :as i18n]
    [utils.re-frame :as rf]))

(defn prepare-token
  [{:keys [symbol] :as item} color]
  {:token               (keyword (string/lower-case symbol))
   :state               :default
   :customization-color color
   :total               (utils/total-per-token item)})

(defn parse-tokens
  [tokens color]
  (vec (map #(prepare-token % color) tokens)))

(defn view
  [{:keys [selected-tab account-address account]}]
  (let [tokens (rf/sub [:wallet/tokens])
        account-tokens (get tokens (keyword (string/lower-case account-address)))
        customization-color (or (:color-id account) :blue)]
    (println "tokens" (parse-tokens account-tokens customization-color))
    (println "accc" account-address)
    (case selected-tab
    :assets       [rn/flat-list
                   {:render-fn               token-value/view
                    :data                    temp/tokens
                    :content-container-style {:padding-horizontal 8}}]
    :collectibles [collectibles/view]
    :activity     [activity/view]
    :permissions  [empty-tab/view
                   {:title        (i18n/label :t/no-permissions)
                    :description  (i18n/label :t/no-collectibles-description)
                    :placeholder? true}]
    :dapps        [dapps/view]
    [about/view])))
