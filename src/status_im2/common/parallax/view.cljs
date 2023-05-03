(ns status-im2.common.parallax.view
  (:require [react-native.core :as rn]
            [quo.core :as quo]
            [react-native.reanimated :as reanimated]
            [oops.core :as oops]
            [quo.react-native :refer [use-window-dimensions] :as rn-hooks]
            ;; [utils.worklets.parallax :as worklets.parallax]

            [reagent.core :as reagent]
            ["react-native-reanimated" :default r :refer (interpolate)]
            [status-im2.common.resources :as resources]))


(def IMAGE_OFFSET 10)
(def PI js/Math.PI)
(def HALF_PI (/ PI 2))

(def duration 100)
(def timing-options #js {:duration duration})


(defn use-parallax-animation [shared-value sensor-value order]
  (rn/use-effect (fn []
                   (reanimated/set-shared-value
                    shared-value
                    (reanimated/with-timing
                      (interpolate
                       sensor-value
                       (clj->js  [(- HALF_PI) HALF_PI])
                       (clj->js  [(/  (- (* IMAGE_OFFSET 2)) order), 0]))
                      {:duration 100})))
                 [sensor-value]))

(defn use-parallax-animation-roll [shared-value sensor-value order]
  (rn/use-effect (fn []
                   (reanimated/set-shared-value
                    shared-value
                    (reanimated/with-timing
                      (interpolate
                       sensor-value
                       (clj->js  [(- PI) PI])
                       (clj->js  [(/  (- (* IMAGE_OFFSET 2)) order), 0]))
                      {:duration 100})))
                 [sensor-value]))


(defn f-sensor-animated-image [{:keys [p r order source] :or {order 1}}]
  (let [{:keys [height width]}  (rn-hooks/use-window-dimensions)

        pitch (reanimated/use-shared-value 0)
        roll (reanimated/use-shared-value 0)]


    (use-parallax-animation pitch p order)
    (use-parallax-animation-roll roll r order)
    (js/console.log p r)
    [:<>
     [reanimated/image {:source source
                        :style (reanimated/apply-animations-to-style
                                {:transform [{:translateY pitch}
                                             {:translateX roll}]}
                                {:position :absolute

                                 :top 0
                                 :bottom 0
                                 :left 0
                                 :right 0
                                 :height  (/ (+ height  (* 2 IMAGE_OFFSET))  order)
                                 :width  (/ (+ width  (* 2 IMAGE_OFFSET))  order)})}]]))

(defn sensor-animated-image [props]
  [:f> f-sensor-animated-image props])

(defn get-pitch [sensor]
  (let [value (oops/oget sensor "sensor")]
    value.value.pitch))

(defn get-roll [sensor]
  (let [value (oops/oget sensor "sensor")]
    value.value.roll))

(defn f-parallax [{:keys [layers]}]
  (fn []
  [:<>
   (map-indexed (fn [idx, layer]
                  [sensor-animated-image {:p (get-pitch (reanimated/use-animated-sensor 5 {:interval 100}))
                                          :r (get-roll (reanimated/use-animated-sensor 5 {:interval 100}))
                                          :key (str layer idx)
                                          :source layer
                                          :order (inc idx)}])
                layers)]))

;; (defn f-test [props]
;;   (fn []
;;   (let [sensor (reanimated/use-animated-sensor 5)]
    
;;       (reanimated/use-animated-style (fn []
;;                                        (js/console.log (str (get-pitch sensor)))
;;                                        (clj->js {})
;;                                        ))

;;     [quo/text {:style {:color :white}} (str (get-pitch sensor))]
;;     )
;;   ))

(defn parallax [props]
  [:f> f-parallax props])

