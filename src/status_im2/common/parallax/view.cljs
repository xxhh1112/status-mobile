(ns status-im2.common.parallax.view
  (:require [react-native.core :as rn]
            [react-native.reanimated :as reanimated]
            [oops.core :as oops]
            [utils.worklets.parallax :as worklets.parallax]
            ["react-native-reanimated" :default r :refer (interpolate)]
            [status-im2.common.resources :as resources]))

(def IMAGE_OFFSET 100)
(def PI js/Math.PI)
(def HALF_PI (/ PI 2))

  ;;   :left (reanimated/with-timing
                           ;;           (reanimated/interpolate roll
                           ;;                                   [(- PI) PI]
                           ;;                                   [(/ (* (- IMAGE_OFFSET) 2) order) 0])
                                    ;;  {:duration 100})

(defn sensor-animated-image [{:keys [image order] :or {order 1}}]
  [:f>
   (fn []
     (let [{:keys [width height]} (rn/use-window-dimensions)
         ;;   sensor  (reanimated/use-animated-sensor 5 (clj->js { :interval 10 }))
         ;;   value sensor.sensor.value
         ;;   image-style (worklets.parallax/sensor-animated-image (clj->js {:pitch value.pitch
         ;;                                                                  :roll value.roll}))
           ]
       
       (js/console.log (reanimated/use-animated-sensor 2 (clj->js {:interval 10})))
      ;;  [reanimated/image {:source image
      ;;                     :style {[{
      ;;                               :width (+ width (/ (* 2 IMAGE_OFFSET) order))
      ;;                                :height (+ height (/ (* 2 IMAGE_OFFSET) order))
      ;;                               :position :absolute
      ;;                               }] image-style}}]
       [rn/view {:top 0
                 :left 0
                 :right 0
                 :bottom 0
                 :position :absolute}
        [reanimated/image {:source (resources/get-mock-image :sticker)
                           :style (reanimated/apply-animations-to-style
                                   {:height 200
                                    :width 100}
                                   {:height 200
                                    :width 100})}]
        [rn/text {:color :red :height 20 :width 20} (str "value.pitch")]]))])

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
