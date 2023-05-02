(ns status-im2.common.parallax.view
  (:require [react-native.core :as rn]
            [quo.core :as quo]
            [react-native.reanimated :as reanimated]
            [oops.core :as oops]
            ;; [utils.worklets.parallax :as worklets.parallax]
            [reagent.core :as reagent]
            ["react-native-reanimated" :default r :refer (interpolate)]
            [status-im2.common.resources :as resources]))


(def IMAGE_OFFSET 100)
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
                       (clj->js   [1 0])) ;;adjust 
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
                       (clj->js   [1 0])) ;;adjust 
                      {:duration 100})))
                 [sensor-value]))


(defn f-sensor-animated-image [{:keys [p r  order source] :or {order 1}}]
  (let [width 100
        height 100
        pitch (reanimated/use-shared-value 0)
        roll (reanimated/use-shared-value 0)]

    (use-parallax-animation pitch p order)
    (use-parallax-animation-roll roll r order)

    [:<>
     [reanimated/image {:source source
                        :style (reanimated/apply-animations-to-style
                                {:transform [{:translateY pitch}
                                             {:translateX roll}]}
                                {:height 100
                                 :width 100})}]]))

(defn sensor-animated-image [props]
  [:f> f-sensor-animated-image props])

;; import { useWindowDimensions } from "react-native";
;; import Animated, {
;;   useAnimatedSensor,
;;   SensorType,
;;   useAnimatedStyle,
;;   interpolate,
;;   withTiming,
;; } from "react-native-reanimated";

;; const IMAGE_OFFSET = 100;
;; const PI = Math.PI;
;; const HALF_PI = PI / 2;

;; const SensorAnimatedImage = ({ image, order }) => {
;;   const { width, height } = useWindowDimensions();

;;   const sensor = useAnimatedSensor(SensorType.ROTATION);

;;   const imageStyle = useAnimatedStyle(() => {
;;     const { pitch, roll } = sensor.sensor.value;

;;     return {
;;       top: withTiming(
;;         interpolate(
;;           pitch,
;;           [-HALF_PI, HALF_PI],
;;           [(-IMAGE_OFFSET * 2) / order, 0]
;;         ),
;;         { duration: 100 }
;;       ),
;;       left: withTiming(
;;         interpolate(roll, [-PI, PI], [(-IMAGE_OFFSET * 2) / order, 0]),
;;         {
;;           duration: 100,
;;         }
;;       ),
;;     };
;;   });

;;   return (
;;     <Animated.Image
;;       source={image}
;;       style={[
;;         {
;;           width: width + (2 * IMAGE_OFFSET) / order,
;;           height: height + (2 * IMAGE_OFFSET) / order,
;;           position: "absolute",
;;         },
;;         imageStyle,
;;       ]}
;;     />
;;   );
;; };

;; export default SensorAnimatedImage;
