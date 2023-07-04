(ns status-im2.contexts.syncing.events
  (:require [native-module.core :as native-module]
            [re-frame.core :as re-frame]
            [status-im.data-store.settings :as data-store.settings]
            [status-im.node.core :as node]
            [status-im.utils.platform :as utils.platform]
            [status-im2.config :as config]
            [status-im2.constants :as constants]
            [taoensso.timbre :as log]
            [utils.re-frame :as rf]
            [utils.security.core :as security]
            [utils.i18n :as i18n]
            [utils.transforms :as transforms]
            [quo2.foundations.colors :as colors]))

(rf/defn local-pairing-update-role
  {:events [:syncing/update-role]}
  [{:keys [db]} role]
  {:db (assoc-in db [:syncing :role] role)})

(rf/defn local-pairing-clear-states
  {:events [:syncing/clear-states]}
  [{:keys [db]} role]
  {:db (dissoc db :syncing)})

(defn- get-default-node-config
  [installation-id]
  (let [db {:networks/current-network config/default-network
            :networks/networks        (data-store.settings/rpc->networks config/default-networks)
            :profile/profile          {:installation-id           installation-id
                                       :device-name               (native-module/get-installation-name)
                                       :log-level                 config/log-level
                                       :waku-bloom-filter-mode    false
                                       :custom-bootnodes          nil
                                       :custom-bootnodes-enabled? false}}]
    (node/get-multiaccount-node-config db)))

(rf/defn set-preflight-check-status
{:events [:camera/set-preflight-check-status]}
 [{:keys [db]} raw-response]
  (log/info "Local pairing preflight check"
            {:response raw-response
             :event    :camera/set-preflight-check-status})
  (let [^js response-js (transforms/json->js raw-response)
        error           (transforms/js->clj (.-error response-js))]
    {:db (assoc db :camera/preflight-check-passed? (empty? error))}))

(rf/defn preflight-outbound-check-for-local-pairing
  "Performing the check for the first time
   will trigger local network access permission in iOS.
   This permission is required for local pairing
   https://github.com/status-im/status-mobile/issues/16135"
  {:events [:syncing/preflight-outbound-check]}
  [_]
  (native-module/local-pairing-preflight-outbound-check (fn [raw-response]
                                                          (rf/dispatch [:camera/set-preflight-check-status raw-response]))))

(rf/defn initiate-local-pairing-with-connection-string
  {:events       [:syncing/input-connection-string-for-bootstrapping]
   :interceptors [(re-frame/inject-cofx :random-guid-generator)]}
  [{:keys [random-guid-generator db]} connection-string]
  (let [installation-id (random-guid-generator)
        default-node-config (get-default-node-config installation-id)
        default-node-config-string (.stringify js/JSON (clj->js default-node-config))
        callback
        (fn [final-node-config]
          (let [config-map (.stringify js/JSON
                                       (clj->js
                                        {:receiverConfig {:kdfIterations config/default-kdf-iterations
                                                          :nodeConfig final-node-config
                                                          :settingCurrentNetwork config/default-network
                                                          :deviceType utils.platform/os
                                                          :deviceName
                                                          (native-module/get-installation-name)}}))]
            (rf/dispatch [:syncing/update-role constants/local-pairing-role-receiver])
            (native-module/input-connection-string-for-bootstrapping
             connection-string
             config-map
             #(log/info "Initiated local pairing"
                        {:response %
                         :event    :syncing/input-connection-string-for-bootstrapping}))))]
    (native-module/prepare-dir-and-update-config "" default-node-config-string callback)))

(rf/defn preparations-for-connection-string
  {:events [:syncing/get-connection-string-for-bootstrapping-another-device]}
  [{:keys [db]} entered-password set-code]
  (let [valid-password? (>= (count entered-password) constants/min-password-length)
        show-sheet      (fn [connection-string]
                          (set-code connection-string)
                          (rf/dispatch [:syncing/update-role constants/local-pairing-role-sender])
                          (rf/dispatch [:bottom-sheet/hide]))]
    (if valid-password?
      (let [sha3-pwd   (native-module/sha3 (str (security/safe-unmask-data entered-password)))
            key-uid    (get-in db [:profile/profile :key-uid])
            config-map (.stringify js/JSON
                                   (clj->js {:senderConfig {:keyUID       key-uid
                                                            :keystorePath ""
                                                            :password     sha3-pwd
                                                            :deviceType   utils.platform/os}
                                             :serverConfig {:timeout 0}}))]
        (native-module/get-connection-string-for-bootstrapping-another-device
         config-map
         #(show-sheet %)))
      (show-sheet ""))))

(rf/defn update-camera-permission-status
  {:events [:update-camera-permission-status]}
  [{:keys [db]} value]
  {:db (assoc db :camera/permission-granted? value)})

(rf/defn request-permission
 {:events [:camera/request-permission]}
 [{:keys [db]}]
   (rf/dispatch
    [:request-permissions
     {:permissions [:camera]
      :on-allowed  #(rf/dispatch [:update-camera-permission-status true])
      :on-denied   #(rf/dispatch
                     [:toasts/upsert
                      {:icon           :i/info
                       :id             :camera-permission-denied-toast
                       :icon-color     colors/danger-50
                       :override-theme :light
                       :text           (i18n/label :t/camera-permission-denied)}])}]))
