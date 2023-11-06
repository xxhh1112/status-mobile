(ns status-im2.contexts.add-new-contact.scan-profile-qr
  (:require [status-im2.common.scan-qr-code.view :as scan-qr-code]
            [utils.debounce :as debounce]
            [utils.i18n :as i18n]))

(defn view
  []
  [scan-qr-code/view
   {:title           (i18n/label :t/scan-qr)
    :bottom-padding? false
    :on-success-scan #(debounce/debounce-and-dispatch [:contacts/qr-code-scanned %] 300)}])
