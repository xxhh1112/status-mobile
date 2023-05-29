(ns status-im2.common.wizard.view
  (:require [react-native.core :as rn]
            [oops.core :as oops]
            [react-native.reanimated :as reanimated]
            [reagent.core :as reagent]))

(def slide-away-ms 5)
(def slide-up-ms 3)

(def slide-to-initial-ms 3)

(def swap-content-ms 25)

(def slide-to-center 10)

(defn next-step-animation [translateX translateY opacity window-width window-height]
  (reanimated/set-shared-value
   translateX
   (reanimated/with-sequence
     (reanimated/with-timing
      (- window-width)
       {:duration slide-away-ms
        :easing (:easing1 reanimated/easings)})


     (reanimated/with-timing window-width
       {:duration slide-to-initial-ms
        :easing (:easing1 reanimated/easings)})

     (reanimated/with-timing 0
       {:duration slide-to-center
        :easing (:easing1 reanimated/easings)})))

  (reanimated/set-shared-value
   opacity
   (reanimated/with-sequence
     (reanimated/with-timing  0
       {:duration 10
        :easing (:easing1 reanimated/easings)})

   (reanimated/with-delay 30 #_swap-content-ms
     (reanimated/with-timing  1
       {:duration 10
        :easing (:easing1 reanimated/easings)}))))

  #_(reanimated/set-shared-value
   translateY
   (reanimated/with-sequence
     (reanimated/with-delay (* slide-away-ms 6)
       (reanimated/with-timing window-height
         {:duration slide-up-ms
          :easing (:easing1 reanimated/easings)}))

     (reanimated/with-timing 0
       {:duration 10
        :easing (:easing1 reanimated/easings)}))))

(defn prev-step-animation [shared-val opacity window-width]
  (reanimated/set-shared-value
   shared-val
   (reanimated/with-sequence
     (reanimated/with-timing  (+ (* 2   window-width))
       {:duration 25
        :easing (:easing1 reanimated/easings)})

     (reanimated/with-timing  (- (* 2 window-width))
       {:duration 5
        :easing (:easing1 reanimated/easings)})

     (reanimated/with-timing  0
       {:duration 25
        :easing (:easing1 reanimated/easings)})))

  (reanimated/set-shared-value
   opacity
   (reanimated/with-sequence
    (reanimated/with-timing
     0
     {:duration 25
      :easing   (:easing1 reanimated/easings)})

    (reanimated/with-delay
     150
     (reanimated/with-timing
      1
      {:duration 200
       :easing   (:easing1 reanimated/easings)})))))

(defn f-step
  [{:keys [content animation duration translateX translateY opacity current-step
           prev-step use-native-driver content-container-style]}]
  [reanimated/view {:style (reanimated/apply-animations-to-style
                            {:transform [{:translateX translateX}
                                         {:translateY translateY}]
                             :opacity   opacity}
                            {:position :absolute
                             :top      0
                             :bottom   0
                             :left     0
                             :right    0})}
   content])

(def page-container
  {:position :absolute
   :top      0
   :bottom   0
   :left     0
   :right    0})

(defn f-view
  [{:keys [steps active-step current-step on-next on-prev first-step? last-step? duration
           wizard-ref]}]
  (let [active-step-no (reagent/atom 0)
        prev-step-no   (reagent/atom 0)
        {window-width  :width
         window-height :height} (rn/get-window)
        next?          (reagent/atom true)]
    (fn []
      (let [translateX (reanimated/use-shared-value 0)
            translateY (reanimated/use-shared-value 0)
            opacity    (reanimated/use-shared-value 1)]
        (oops/oset! wizard-ref "current"
                    {:next (fn []
                             (when (not= (- (count steps) 1) @active-step-no)
                               (next-step-animation translateX translateY opacity window-width window-height)
                               (js/setTimeout (fn []
                                                (swap! active-step-no inc)
                                                (reset! next? true))
                                              swap-content-ms)))

                     :prev (fn []
                             (when-not (zero? @active-step-no)
                               (prev-step-animation translateX opacity window-width)
                               (js/setTimeout (fn []
                                                (swap! active-step-no dec)
                                                (reset! next? true))
                                              50)))})

        [:f> f-step {:content-container-style page-container
                     :current-step            @active-step-no
                     :translateX              translateX
                     :translateY              translateY
                     :opacity                 opacity
                     :content                 (:content (nth steps @active-step-no))}]))))

(defn view [props]
  [:f> f-view props])

