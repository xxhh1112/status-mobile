(ns quo2.components.buttons.button.style)

(def blur-view
  {:position :absolute
   :top      0
   :left     0
   :right    0
   :bottom   0})

(defn before-icon-style
  [{:keys [override-margins size icon-container-size icon-background-color icon-container-rounded?
           icon-size]}]
  (merge
   {:margin-left     (or (get override-margins :left)
                         (if (= size 40) 12 8))
    :margin-right    (or (get override-margins :right) 4)
    :align-items     :center
    :justify-content :center}
   (when icon-container-size
     {:width  icon-container-size
      :height icon-container-size})
   (when icon-background-color
     {:background-color icon-background-color})
   (when icon-container-rounded?
     {:border-radius (/ (or icon-container-size icon-size) 2)})))

(defn after-icon-style
  [{:keys [override-margins size icon-container-size icon-background-color icon-container-rounded?
           icon-size]}]
  (merge
   {:margin-left     (or (get override-margins :left) 4)
    :margin-right    (or (get override-margins :right)
                         (if (= size 40) 12 8))
    :align-items     :center
    :justify-content :center}
   (when icon-container-size
     {:width  icon-container-size
      :height icon-container-size})
   (when icon-background-color
     {:background-color icon-background-color})
   (when icon-container-rounded?
     {:border-radius (/ (or icon-container-size icon-size) 2)})))

(defn shape-style-container
  [type icon size]
  {:height        size
   :border-radius (if (and icon (#{:primary :secondary :danger} type))
                    24
                    (case size
                      56 12
                      40 12
                      32 10
                      24 8))})

(defn padding-horizontal
  [check size]
  (when-not check
    (case size
      56 16
      40 16
      32 12
      24 8)))

(defn style-container
  [{:keys [type size disabled background-color border-color icon-only? above width before after blur?]}]
  (merge {:height             size
          :align-items        :center
          :justify-content    :center
          :flex-direction     (if above :column :row)
          :padding-horizontal (padding-horizontal (or icon-only? before after) size)
          :padding-left       (padding-horizontal (or icon-only? before) size)
          :padding-right      (padding-horizontal (or icon-only? after) size)
          :padding-top        (when-not (or icon-only? before after)
                                (case size
                                  56 0
                                  40 9
                                  32 5
                                  24 3))
          :padding-bottom     (when-not (or icon-only? before after)
                                (case size
                                  56 0
                                  40 9
                                  32 5
                                  24 4))
          :overflow           :hidden}
         (when (not blur?)
           {:background-color background-color})
         (shape-style-container type icon-only? size)
         (when width
           {:width width})
         (when icon-only?
           {:width size})
         (when border-color
           {:border-color border-color
            :border-width 1})
         (when disabled
           {:opacity 0.3})))
