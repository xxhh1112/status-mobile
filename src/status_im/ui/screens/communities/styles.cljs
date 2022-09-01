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
  ;;  :position       :absolute
  ;;  :top            116
  ;;  :left           12
  ;;  :right          12
   })

(defn list-stats-container []
  {:flex-direction :row
   :align-items    :center})

(defn community-tags-container  []
  {:flex-direction :row
  :margin-top 16
  ;;  :position  :absolute
  ;;  :top       154
})

(defn card-view-content-container [top]
  {:flex               1
  :height 200
  :padding-left 20
  :padding-right 20
  ;;  :position           :absolute
  ;;  :top                top
  ;;  :left               0
  ;;  :right              0
  ;;  :bottom             0
   :border-radius      16
  ;;  :padding-horizontal 12
   :background-color (colors/theme-colors
                      colors/white
                      colors/neutral-90)})

(defn card-view-chat-icon [icon-height]
  {:border-radius    48
   :border-width 3
   :border-color :white
   :position         :absolute
   :top              (- (/ icon-height 2))
   :left             20
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

(defn community-title-description-container []
  {
  :margin-top 56

  })

(defn community-cover-container []

  {:flex-direction          :row
   :height                  64
   :border-top-right-radius 20
   :border-top-left-radius  20
   
   :background-color        colors/primary-50-opa-20})

(defn community-page-cover-container []
    (assoc (community-cover-container)
     
     :height 148)
   )

(defn permission-tag-styles []

  {:position         :absolute
   :top              8
   :right            8})