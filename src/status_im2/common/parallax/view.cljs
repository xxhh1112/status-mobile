(ns status-im2.common.parallax.view
  (:require [react-native.core :as rn]
            [quo.core :as quo]
            [react-native.reanimated :as reanimated]
            [oops.core :as oops]
            [quo.react-native :refer [use-window-dimensions] :as rn-hooks]
            [reagent.core :as reagent]
            ["react-native-reanimated" :default r :refer (interpolate)]
            [status-im2.common.resources :as resources]))


(def IMAGE_OFFSET 100)
(def PI js/Math.PI)
(def HALF_PI (/ PI 2))
(def duration 100)
(def timing-options #js {:duration duration})

(defn get-pitch [sensor]
  (let [value (oops/oget sensor "sensor")]
    value.value.pitch))

(defn get-roll [sensor]
  (let [value (oops/oget sensor "sensor")]
    value.value.roll))

(defn use-parallax-animation [shared-pitch shared-roll order]
  (let [sensor (reanimated/use-animated-sensor 5 {:interval 200})]
    (rn/use-effect (fn []
                     (js/setInterval
                      (fn []
                        (reanimated/set-shared-value
                         shared-pitch
                         (reanimated/with-timing
                           (interpolate
                            (get-pitch sensor)
                            (clj->js  [(- HALF_PI) HALF_PI])
                            (clj->js  [(+ (* (- 20) (+ order 1)) 20)  (- (* 20 (+ order 1)) 20)])
                            (clj->js {:extrapolateLeft  "clamp"
                                      :extrapolateRight "clamp"}))
                           {:duration 100}))

                        (reanimated/set-shared-value
                         shared-roll
                         (reanimated/with-timing
                           (interpolate
                            (* -1 (get-roll sensor))
                            (clj->js  [(- PI) PI])
                            (clj->js   [(+ (* (- 20) (+ order 1)) 20)  (- (* 20 (+ order 1)) 20)])
                            (clj->js {:extrapolateLeft  "clamp"
                                      :extrapolateRight "clamp"}))
                           {:duration 100})))
                      200))
                   [])))

(defn scalar3 [order max]
  (- 1 (js/Math.pow (/ max order) (- 2))))

(defn s3 [order _]
  (cond
    (= 1 order) 1
    (= 2 order) 0.55
    (= 3 order) 0.55
    (= 4 order) 0.55))

(defn f-sensor-animated-image [{:keys [order source max-layers] :or {order 1}}]
  (let [{:keys [height width]}  (rn-hooks/use-window-dimensions)
        pitch (reanimated/use-shared-value 0)
        roll (reanimated/use-shared-value 0)]
    (use-parallax-animation pitch roll order)
    (fn []
      [:<>
       [reanimated/image {:source source
                          :style (reanimated/apply-animations-to-style
                                  {:transform [{:translateY pitch}
                                               {:translateX roll}]}
                                  {:position :absolute
                                   :aspect-ratio 1
                                   :overflow :visible
                                   :margin :auto
                                   :top  0
                                   :bottom 0
                                   :left 0
                                   :right 0
                                   :height (* height  (s3 order max-layers))
                                   :width width})}]])))

(defn sensor-animated-image [props]
  [:f> f-sensor-animated-image props])



(defn f-parallax [{:keys [layers]}]
  [:<>
   (map-indexed (fn [idx, layer]
                  [sensor-animated-image {:key (str layer idx)
                                          :max-layers (+ 2 (count layers))
                                          :source layer
                                          :order   (inc idx)}])
                layers)])

(defn parallax [props]
  [:f> f-parallax props])


