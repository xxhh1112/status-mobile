(ns utils.worklets.parallax)

(def ^:private parallax-worklets (js/require "../src/js/worklets/parallax.js"))

(defn sensor-animated-image
  [props]
  (.sensorAnimatedImage ^js parallax-worklets props))

(defn use-animated-sensor-worklet
  []
  (.useAnimatedSensorWorklet ^js parallax-worklets))
