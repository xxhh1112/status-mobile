(ns status-im.ui.screens.communities.styles
  (:require
   [quo2.foundations.colors :as colors]))

(def category-item
  {:flex           1
   :flex-direction :row
   :align-items    :center
   :height         52
   :padding-left   18})

(defn community-card [radius]
  {:shadow-offset    {:width 0
                      :height 2}
   :shadow-radius     radius
   :shadow-opacity    1
   :shadow-color      colors/shadow
   :border-radius     radius
   :justify-content   :space-between
   :elevation         2
   :background-color  (colors/theme-colors
                       colors/white
                       colors/neutral-90)})

(defn stats-count-container []
  {:flex-direction :row
   :align-items    :center
   :margin-right   16})

(defn card-stats-container []
  {:flex-direction :row
  :margin-top 12
   })

(defn list-stats-container []
  {:flex-direction :row
   :align-items    :center})

(defn community-tags-container  []
  {:flex-direction :row
  :margin-top 16})

(defn card-view-content-container [{:keys [padding-horizontal]}]
  {:flex               1
  :height 20
  :padding-left padding-horizontal
  :padding-right padding-horizontal
   :border-radius      16
   :background-color (colors/theme-colors
                      colors/white
                      colors/neutral-90)})

(defn card-view-chat-icon [{:keys [top left]}]
  {:border-radius    48
   :position         :absolute
   :top              top
   :left             left
   :padding          2
   :background-color (colors/theme-colors
                      colors/white
                      colors/neutral-90)})

(defn list-view-content-container []
  {:flex-direction    :row
   :border-radius     16
   :align-items       :center
   :background-color (colors/theme-colors
                      colors/white
                      colors/neutral-90)})

(defn list-view-chat-icon []
  {:border-radius    32
   :padding          12})

(defn community-title-description-container [{:keys [margin-top] }]
  {  :margin-top margin-top})

(defn community-cover-container [{:keys [height]}]
  {:flex-direction          :row
   :height                  height
   :border-top-right-radius 20
   :border-top-left-radius  20
   :background-color        colors/primary-50-opa-20})

(defn permission-tag-styles []
  {:position         :absolute
   :top              8
   :right            8})