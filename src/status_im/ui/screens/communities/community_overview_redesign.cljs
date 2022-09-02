(ns status-im.ui.screens.communities.community-overview-redesign
  (:require  [status-im.i18n.i18n :as i18n]
             [status-im.ui.components.react :as react]
             [quo2.components.text :as quo2.text]
             [quo2.components.button :as quo2.button]
             [status-im.react-native.resources :as resources]
             [quo2.components.communities.community-view :as community-view]
             [status-im.ui.screens.communities.request-to-join-bottom-sheet-redesign :as request-to-join]
             [status-im.utils.handlers :refer [>evt]]
             [status-im.ui.screens.communities.styles :as styles]
             [status-im.ui.screens.communities.icon :as communities.icon]))

(def coinbase-mock-data
  {:id "1"
   :name "Coinbase"
   :description "Jump start your crypto portfolio with the easiest place to buy and sell crypto"
   :locked false
   :cover (js/require "../resources/images/ui/coinbase-community-background.png")
   :tags  [{:id 1 :tag-label (i18n/label :t/music) :resource (resources/get-image :music)}
           {:id 2 :tag-label (i18n/label :t/lifestyle) :resource (resources/get-image :lifestyle)}
           {:id 3 :tag-label (i18n/label :t/podcasts) :resource (resources/get-image :podcasts)}]})

(defn preview-list-component []
  [react/view {:style {:height 20 :margin-top 20}}
   [quo2.text/text {:accessibility-label :communities-screen-title
                    :weight              :semi-bold
                    :size                :label}
    "placeholder for preview list component"]])

(defn channel-list-component []
  [react/view {:style {:height 200 :margin-top 20}}
   [quo2.text/text {:accessibility-label :communities-screen-title
                    :weight              :semi-bold
                    :size                :label}
    "placeholder for channel list component"]])

(defn community-card-page-view [{:keys [name description locked
                                        status tokens cover tags] :as community}]
  [react/view
   {:style
    {:height "100%"
     :border-radius   20}}
   [react/view (styles/community-cover-container  {:height 148})
    [react/image
     {:source      cover
      :style  {:height 180
               :flex 1}}]]
   [react/view (styles/card-view-content-container {:padding-horizontal 20})
    [react/view (styles/card-view-chat-icon {:top -40 :left 20})
     [communities.icon/community-icon-redesign community 80]]
    (when (= status :gated)
      [react/view (styles/permission-tag-styles)
       [community-view/permission-tag-container
        {:locked       locked
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
     {:on-press  #(>evt [:bottom-sheet/show-sheet
                         {:content (fn [] [request-to-join/request-to-join])
                          :content-height 300}])

      :style
      {:width "100%"
       :margin-top 20
       :margin-left :auto
       :margin-right :auto}
      :before :main-icons/redesign-communities}
     (i18n/label :join-open-community)]
    [channel-list-component]]])

(defn view []
  (fn []
    [react/view {:style {}}
     [community-card-page-view
      coinbase-mock-data]]))



