(ns status-im.ui.screens.communities.community-overview-redesign
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [status-im.i18n.i18n :as i18n]
            [status-im.ui.components.list.views :as list]
            [status-im.ui.components.react :as react]
            [quo2.components.communities.community-card-view :as community-card-view]
            [quo2.components.separator :as separator]
            [quo2.components.text :as quo2.text]
            [quo2.components.icon :as quo2.icon]
            [quo2.components.tag :as quo2.tag]
            [quo2.components.button :as quo2.button]
            [quo2.components.counter :as quo2.counter]
            [quo2.components.filter-tags :as filter-tags]
            [quo2.components.filter-tag  :as filter-tag]
            [quo2.foundations.colors :as quo2.colors]
            [quo.components.safe-area :as safe-area]
            [quo2.components.tabs :as quo2.tabs]
            [status-im.ui.screens.chat.photos :as photos]
            [status-im.react-native.resources :as resources]
            [status-im.multiaccounts.core :as multiaccounts]
            [status-im.ui.components.topbar :as topbar]
            [status-im.ui.components.plus-button :as plus-button]
            [status-im.utils.handlers :refer [<sub]]
            [status-im.ui.components.topnav :as topnav]

      [quo2.components.communities.community-view :as community-view]
   [status-im.communities.core :as communities]
   [status-im.utils.handlers :refer [>evt <sub]]

   [status-im.ui.screens.communities.styles :as styles]
   [status-im.ui.screens.communities.community :as community]
   [status-im.ui.screens.communities.icon :as communities.icon]

    [status-im.react-native.resources :as resources]
))

(def coinbase-mock-data 
{
           :id "1"
           :name "Coinbase"
           :description "Jump start your crypto portfolio with the easiest place to buy and sell crypto"
           :locked false
          :cover (js/require "../resources/images/ui/coinbase-community-background.png")
          :tags  [{:id 1 :tag-label (i18n/label :t/music) :resource (resources/get-image :music)}
                    {:id 2 :tag-label (i18n/label :t/lifestyle) :resource (resources/get-image :lifestyle)}
                    {:id 3 :tag-label (i18n/label :t/podcasts) :resource (resources/get-image :podcasts)}
            ]
         }
)

(defn preview-list-component []
 [react/view {:style {:height 20 :margin-top 20}}
 [quo2.text/text {:accessibility-label :communities-screen-title
                     :weight              :semi-bold
                     :size                :label}
     "placeholder for preview list component"]]
)

(defn channel-list-component []
 [react/view {:style {:height 200 :margin-top 20}}
 [quo2.text/text {:accessibility-label :communities-screen-title
                     :weight              :semi-bold
                     :size                :label}
     "placeholder for channel list component"]]
)

(defn community-card-page-view [{:keys [id name description locked
                                        status tokens cover tags featured] :as community}]
  (let [width (* (<sub [:dimensions/window-width]) 0.90)]
   
      [react/view 
         {:style
           {:height "100%"
            :border-radius   20}
      }
       [react/view (styles/community-page-cover-container)
        [react/image {:source      cover
                      :style  {
                              :height 180
                              :flex 1
                               }}]]
       [react/view (styles/card-view-content-container 148)
        [react/view (styles/card-view-chat-icon 80)
         [communities.icon/community-icon-redesign community 80]]
        (when (= status :gated)
          [react/view (styles/permission-tag-styles)
           [community-view/permission-tag-container {:locked       locked
                                      :status       status
                                      :tokens       tokens}]])
        [community-view/community-title 
                          {:title name
                          :size :large
                          :description description}]
        [community-view/community-stats-column :card-view]
        [community-view/community-tags tags]
        [preview-list-component]
           [quo2.button/button 
      {:style 
        { :width "100%"
          :margin-top 20
          :margin-left :auto 
        :margin-right :auto}
       :before :main-icons/redesign-communities}
      "Join community"]
          [channel-list-component]

        ]]))


(defn community-user-info [community-info]
  [react/view 
       {
         :flex-direction :row
       }
       
       [quo2.icon/icon :total-members {
         :size 12
       }]
       [quo2.text/text 
       {
         :accessibility-label :communities-community-total-users
        :size                :label}
         (:number-of-users community-info)]
      
     
       [quo2.icon/icon :lightning {
       }]
       [quo2.text/text 
       {:accessibility-label :communities-community-active-users
        :size :label}
         (:active-users community-info)]
     ]
)

(defn community-tags [community-info]
  [react/view 
       {
         :flex-direction :row
       }
       [ quo2.tag/tag {
         :size :small
         :token-img-src (js/require "../resources/images/tokens/mainnet/ETH.png")
         } "crypto" ]
         [ quo2.tag/tag {
         :size :small
         :token-img-src (js/require "../resources/images/tokens/mainnet/ETH.png")
         } "Markets" ]
         [ quo2.tag/tag {
         :size :small
         :token-img-src (js/require "../resources/images/tokens/mainnet/ETH.png")
         } "Organisation" ]
     ]
)



(defn title-column [community-info]
  [react/view
   {:flex-direction     :column
    :align-items        :flex-start
    :height             56
    :margin-top   12
    :margin-bottom   12

    :padding-horizontal 16}

    [quo2.text/text {:accessibility-label :communities-screen-title
                     :weight              :semi-bold
                     :size                :heading-1}
     (:name community-info)]
     [quo2.text/text {:accessibility-label :communities-screen-sub-title
                     :size                :label}
     (:overview community-info)]
     [community-user-info community-info]
    [community-tags community-info ]
    [preview-list-component]
   ])

(defn views []
  (let [multiaccount (<sub [:multiaccount])
        communities  (<sub [:communities/communities])]
    (fn []
    [react/view {:style {
    }}
      [community-card-page-view 
         coinbase-mock-data
         ]
          ])))



