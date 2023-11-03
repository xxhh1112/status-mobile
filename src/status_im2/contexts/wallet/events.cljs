(ns status-im2.contexts.wallet.events
  (:require
    [native-module.core :as native-module]
    [quo.foundations.colors :as colors]
    [status-im2.common.data-store.wallet :as data-store]
    [taoensso.timbre :as log]
    [utils.i18n :as i18n]
    [utils.re-frame :as rf]
    [utils.security.core :as security]))

(rf/reg-event-fx :wallet/show-account-created-toast
 (fn [{:keys [db]} [address]]
   (let [{:keys [name]} (get-in db [:wallet :accounts address])]
     {:db (update db :wallet dissoc :navigate-to-account :new-account?)
      :fx [[:dispatch
            [:toasts/upsert
             {:id         :new-wallet-account-created
              :icon       :i/correct
              :icon-color colors/success-50
              :text       (i18n/label :t/account-created {:name name})}]]]})))

(rf/reg-event-fx :wallet/navigate-to-account
 (fn [{:keys [db]} [address]]
   (let [new-account? (get-in db [:wallet :new-account?])]
     (cond-> {:db (assoc-in db [:wallet :current-viewing-account-address] address)
              :fx [[:dispatch [:navigate-to :wallet-accounts address]]]}

       new-account?
       (update :fx conj [:dispatch [:wallet/show-account-created-toast address]])))))

(rf/reg-event-fx :wallet/close-account-page
 (fn [{:keys [db]}]
   {:db (update db :wallet dissoc :current-viewing-account-address)
    :fx [[:dispatch [:navigate-back]]]}))

(rf/reg-event-fx :wallet/get-accounts-success
 (fn [{:keys [db]} [accounts]]
   (let [wallet-db           (get db :wallet)
         new-account?        (:new-account? wallet-db)
         navigate-to-account (:navigate-to-account wallet-db)]
     (cond-> {:db (reduce (fn [db {:keys [address] :as account}]
                            (assoc-in db [:wallet :accounts address] account))
                          db
                          (data-store/rpc->accounts accounts))
              :fx [[:dispatch [:wallet/get-wallet-token]]]}

       new-account?
       (update :fx
               conj
               [:dispatch [:hide-bottom-sheet]]
               [:dispatch-later
                [{:dispatch [:navigate-back]
                  :ms       100}
                 {:dispatch [:wallet/navigate-to-account navigate-to-account]
                  :ms       300}]])))))

(rf/reg-event-fx :wallet/get-accounts
 (fn [_]
   {:fx [[:json-rpc/call
          [{:method     "accounts_getAccounts"
            :on-success [:wallet/get-accounts-success]
            :on-error   #(log/info "failed to get accounts "
                                   {:error %
                                    :event :wallet/get-accounts})}]]]}))

(rf/reg-event-fx :wallet/save-account
 (fn [{:keys [db]} [{:keys [address edited-data]} callback]]
   (let [account        (get-in db [:wallet :accounts address])
         edited-account (-> (merge account edited-data)
                            data-store/<-account)]
     {:fx [[:json-rpc/call
            [{:method     "accounts_saveAccount"
              :params     [edited-account]
              :on-success (fn []
                            (rf/dispatch [:wallet/get-accounts])
                            (when (fn? callback)
                              (callback)))
              :on-error   #(log/info "failed to save account "
                                     {:error %
                                      :event :wallet/save-account})}]]]})))

(rf/reg-event-fx :wallet/get-wallet-token
 (fn [{:keys [db]}]
   (let [addresses (map :address (vals (get-in db [:wallet :accounts])))]
     {:fx [[:json-rpc/call
            [{:method     "wallet_getWalletToken"
              :params     [addresses]
              :on-success [:wallet/get-wallet-token-success]
              :on-error   #(log/info "failed to get wallet token "
                                     {:error  %
                                      :event  :wallet/get-wallet-token
                                      :params addresses})}]]]})))

(rf/reg-event-fx :wallet/get-wallet-token-success
 (fn [{:keys [db]} [tokens]]
   {:db (assoc db
               :wallet/tokens          tokens
               :wallet/tokens-loading? false)}))

(rf/defn scan-address-success
  {:events [:wallet/scan-address-success]}
  [{:keys [db]} address]
  {:db (assoc db :wallet/scanned-address address)})

(rf/defn clean-scanned-address
  {:events [:wallet/clean-scanned-address]}
  [{:keys [db]}]
  {:db (dissoc db :wallet/scanned-address)})

(rf/reg-event-fx :wallet/create-derived-addresses
 (fn [{:keys [db]} [password {:keys [path]} on-success]]
   (let [{:keys [wallet-root-address]} (:profile/profile db)
         sha3-pwd                      (native-module/sha3 (str (security/safe-unmask-data password)))]
     {:fx [[:json-rpc/call
            [{:method     "wallet_getDerivedAddresses"
              :params     [sha3-pwd wallet-root-address [path]]
              :on-success on-success
              :on-error   #(log/info "failed to derive address " %)}]]]})))

(rf/reg-event-fx :wallet/add-account
 (fn [{:keys [db]} [password {:keys [emoji account-name color]} {:keys [public-key address path]}]]
   (let [key-uid        (get-in db [:profile/profile :key-uid])
         sha3-pwd       (native-module/sha3 (security/safe-unmask-data password))
         account-config {:key-uid    key-uid
                         :wallet     false
                         :chat       false
                         :type       :generated
                         :name       account-name
                         :emoji      emoji
                         :path       path
                         :address    address
                         :public-key public-key
                         :colorID    color}]
     {:db (update db
                  :wallet              assoc
                  :navigate-to-account address
                  :new-account?        true)
      :fx [[:json-rpc/call
            [{:method     "accounts_addAccount"
              :params     [sha3-pwd account-config]
              :on-success [:wallet/get-accounts]
              :on-error   #(log/info "failed to create account " %)}]]]})))

(rf/reg-event-fx :wallet/derive-address-and-add-account
 (fn [_ [password account-details]]
   (let [on-success (fn [derived-adress-details]
                      (rf/dispatch [:wallet/add-account password account-details
                                    (first derived-adress-details)]))]
     {:fx [[:dispatch [:wallet/create-derived-addresses password account-details on-success]]]})))

(rf/defn get-ethereum-chains
  {:events [:wallet/get-ethereum-chains]}
  [{:keys [db]}]
  {:fx [[:json-rpc/call
         [{:method     "wallet_getEthereumChains"
           :params     []
           :on-success [:wallet/get-ethereum-chains-success]
           :on-error   #(log/info "failed to get networks " %)}]]]})

(rf/reg-event-fx
 :wallet/get-ethereum-chains-success
 (fn [{:keys [db]} [data]]
   (let [network-data
         {:test (map #(->> %
                           :Test
                           data-store/<-rpc)
                     data)
          :prod (map #(->> %
                           :Prod
                           data-store/<-rpc)
                     data)}]
     {:db (assoc db :wallet/networks network-data)})))
