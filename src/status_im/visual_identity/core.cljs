(ns status-im.visual-identity.core
  (:require [taoensso.timbre :as log]
            [status-im.ethereum.json-rpc :as json-rpc]))

(defn color-hash
  [public-key on-success]
  (json-rpc/call
       {:method     "visualidentity_colorHashOf"
        :params     [public-key]
        :on-success #(on-success %)
        :on-error   (fn [err]
                      (log/error {:error err})
                      (log/warn "The logged events will be rejected"))}))
