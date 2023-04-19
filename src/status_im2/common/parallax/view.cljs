(ns status-im2.common.parallax.view
  (:require [react-native.core :as rn]
            [react-native.reanimated :as reanimated]
            [oops.core :as oops]))

(def IMAGE_OFFSET 100)
(def PI js/Math.PI)
(def HALF_PI (/ 2 js/Math.PI))


(defn sensor-animated-image [{:keys [image order]}]
  [:f>
   (fn []
     (let [{:keys [width height]} (rn/use-window-dimensions)
        ;;    sensor (reanimated/use-animated-sensor reanimated/sensor-type-rotation )
           ]
       (js/console.log reanimated/sensor-type-rotation)
    ;;        image-style (reanimated/use-animated-style
    ;;                     (fn []
    ;;                       (let [{:keys [pitch roll]} (oops/oget sensor "sensor.value")]
    ;;                         {:top (reanimated/with-timing
    ;;                                 (reanimated/interpolate pitch
    ;;                                                         [(- HALF_PI) HALF_PI]
    ;;                                                         [(/ (* (- IMAGE_OFFSET) 2) order)]
    ;;                                                         0)
    ;;                                 {:duration 100})
    ;;                          :left (reanimated/with-timing
    ;;                                  (reanimated/interpolate roll
    ;;                                                          [(- PI) PI]
    ;;                                                          [(/ (* (- IMAGE_OFFSET) 2) order)]
    ;;                                                          0)
    ;;                                  {:duration 100})})))]

    ;;    [reanimated/image {:source image
    ;;                       :style {[{
    ;;                                 :width (+ width (/ (* 2 IMAGE_OFFSET) order))
    ;;                                  :height (+ height (/ (* 2 IMAGE_OFFSET) order))
    ;;                                 :position :absolute
    ;;                                 }] image-style}}]
       
       [rn/view {}]
       ))])

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
