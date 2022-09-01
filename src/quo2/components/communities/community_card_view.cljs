(ns quo2.components.communities.community-card-view
  (:require
    [quo2.components.communities.community-view :as community-view]
   [quo2.components.text :as text]
   [quo2.components.icon :as icons]
   [quo2.foundations.colors :as colors]
   [quo2.components.permission-tag :as permission]
   [quo2.components.filter-tag  :as filter-tag]
   [status-im.communities.core :as communities]
   [status-im.utils.handlers :refer [>evt <sub]]
   [status-im.ui.components.react :as react]
   [status-im.utils.money :as money]
   [status-im.i18n.i18n :as i18n]
   [status-im.ui.screens.communities.styles :as styles]
   [status-im.ui.screens.communities.community :as community]
   [status-im.ui.screens.communities.icon :as communities.icon]))

(defn community-card-view-item [{:keys [id name description locked
                                        status tokens cover tags featured] :as community}]
  (let [width (* (<sub [:dimensions/window-width]) 0.90)]
    [react/view {:style (merge (styles/community-card 20)
                               {:margin-bottom      16}
                               (if featured
                                 {:margin-right      12
                                  :width             width}
                                 {:flex              1
                                  :margin-horizontal 20}))}
     [react/view {:style         {:height          230
                                  :border-radius   20}
                  :on-press      (fn []
                                   (>evt [::communities/load-category-states id])
                                   (>evt [:dismiss-keyboard])
                                   (>evt [:navigate-to :community {:community-id id}]))
                  :on-long-press #(>evt [:bottom-sheet/show-sheet
                                         {:content (fn [] [community/community-actions community])}])}
      [react/view {:flex    1}
       [react/view (styles/community-cover-container {:height 40})
        [react/image {:source      cover
                      :style  {:flex            1
                      
                               :border-radius   20}}]]
       [react/view (styles/card-view-content-container {:padding-horizontal 12} )
        [react/view (styles/card-view-chat-icon {:top -24 :left 12 })
         [communities.icon/community-icon-redesign community 48]]
        (when (= status :gated)
          [react/view (styles/permission-tag-styles)
           [community-view/permission-tag-container {:locked       locked
                                      :status       status
                                      :tokens       tokens}]])
        [community-view/community-title {:title       name
                          :description description}]
        [community-view/community-stats-column :card-view]
        [community-view/community-tags tags]]]]]))
