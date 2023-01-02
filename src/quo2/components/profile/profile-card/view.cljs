(ns quo2.components.profile.profile-card.view 
    (:require [quo2.components.buttons.button :as button]
            [quo2.components.avatars.user-avatar :as user-avatar]
            [quo2.core :as quo]
            [quo2.foundations.colors :as colors]
            [status-im2.contexts.quo-preview.profile.profile-card.style :as style]
            [react-native.core :as rn]))

(defn profile-card
  [{:keys [show-sign-profile? key-card? profile-picture name hash
           emoji-hash on-press-dots on-press-sign show-emoji-hash?]
    :or   {show-sign-profile? false
           show-emoji-hash?   false
           key-card?          false}}] 
  [rn/view
   {:style style/card-container}
   [rn/view
    {:style style/card-header}
    [user-avatar/user-avatar
     {:full-name         name
      :profile-picture   profile-picture
      :size              :medium
      :status-indicator? false
      :ring?             true}]
    [quo/button
     {:size     32
      :type     :blur-bg
      :icon     true
      :on-press on-press-dots}
     :i/options]]
   [rn/view
    {:style style/name-container}
    [quo/text
     {:size            :heading-2
      :weight          :semi-bold
      :number-of-lines 1
      :style           style/user-name} name]
    (when key-card?
      (quo/icon
       :i/keycard))]
   [quo/text
    {:weight          :monospace
     :number-of-lines 1
     :style           style/user-hash} hash]
   (when (and show-emoji-hash? emoji-hash)
     [quo/text
      {:weight          :monospace
       :number-of-lines 1
       :style           style/emoji-hash} emoji-hash])
   (when show-sign-profile?
     [button/button
      {:on-press             on-press-sign
       :type                 :community
       :community-color      (colors/custom-color :turquoise 60)
       :community-text-color colors/white
       :style                style/sign-button} "Sign in to this profile"])]) 

