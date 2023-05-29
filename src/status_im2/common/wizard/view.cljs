(ns status-im2.common.wizard.view
  (:require [react-native.core :as rn]
            [oops.core :as oops]
            [react-native.reanimated :as reanimated]
            [reagent.core :as reagent]))

(def slide-away-ms 100)
(def slide-up-ms 3)

(def slide-to-initial-ms 3)

(def swap-content-ms 25)

(def slide-to-center 10)

(defn next-step-animation [translate-x translate-x-2 translate-x-3 window-width]
  (reanimated/set-shared-value
   translate-x-3
   (reanimated/with-timing
    (* (- window-width) 2)
    {:duration slide-away-ms
     :easing   (:easing1 reanimated/easings)}))

  (reanimated/set-shared-value
   translate-x
   (reanimated/with-timing
    (- window-width)
    {:duration slide-away-ms
     :easing   (:easing1 reanimated/easings)}))

  (reanimated/set-shared-value
   translate-x-2
   (reanimated/with-timing
    (- window-width)
    {:duration slide-away-ms
     :easing   (:easing1 reanimated/easings)})))

(defn f-step
  [{:keys [content-container-style current-step translateX content]}]
  [reanimated/view {:style (reanimated/apply-animations-to-style
                            {:transform [{:translateX translateX}]}
                            content-container-style)}
   content])


(defn page-container [window-width]
  {:position :absolute
   :top      0
   :bottom   0
   :left     window-width
   :right    (- window-width)})

(defn f-view
  [{:keys [steps active-step current-step on-next on-prev first-step? last-step? duration
           wizard-ref]}]
  (let [active-step-no    (atom 0)
        following-step-no (atom 1)
        previous-step-no  (atom -1) ;; TODO: consider we can "jump" steps
        {window-width  :width
         window-height :height} (rn/get-window)
        next?             (reagent/atom true)]
    (fn []
      (let [translateX    (reanimated/use-shared-value 0)
            translateY    (reanimated/use-shared-value 0)
            opacity       (reanimated/use-shared-value 1)
            translate-x-2 (reanimated/use-shared-value 0)
            translate-x-3 (reanimated/use-shared-value 0)
            opacity-2     (reanimated/use-shared-value 1)]
        (oops/oset! wizard-ref "current"
                    {:next (fn []
                             (prn "next step called")
                             (when (not= (- (count steps) 1) @active-step-no)
                               (next-step-animation translateX translate-x-2 translate-x-3 window-width)
                               (swap! active-step-no inc)
                               (swap! following-step-no inc)
                               (reset! next? true)))

                     :prev (fn []
                             (when-not (zero? @active-step-no)
                               #_(prev-step-animation translateX opacity window-width)
                               (swap! active-step-no dec)
                               (swap! following-step-no dec)
                               (reset! next? true)))})
        #_[:<>
         (when-not (zero? @active-step-no)
           [:f> f-step {:content-container-style (page-container 0)
                        :current-step            @active-step-no
                        :translateX              translate-x-3
                        :content                 (:content (nth steps (dec @active-step-no)))}])

         [:f> f-step {:content-container-style (page-container 0)
                      :current-step            @active-step-no
                      :translateX              translateX
                      :content                 (:content (nth steps @active-step-no))}]

         [:f> f-step {:content-container-style (page-container window-width)
                      :current-step            @active-step-no
                      :translateX              translate-x-2
                      :content                 (:content (nth steps (inc @active-step-no)))}]]


        (doall
         (map-indexed (fn [step idx]
                        (if (or (= idx (dec @active-step-no))
                                (= idx @active-step-no)
                                (= idx (inc @active-step-no)))
                         [:f> f-step {:content-container-style (page-container 0)
                                      :current-step            @active-step-no
                                      :translateX              translateX
                                      :content                 (:content (nth steps @active-step-no))}]))

                      steps))


        ))))

(defn view [props]
  [:f> f-view props])

