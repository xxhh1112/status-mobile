(ns utils.worklets.parallax)

(def ^:private parallax-worklets (js/require "../src/js/worklets/parallax.js"))

(defn sensor-animated-image
  [sensor order]
  (.sensorAnimatedImage ^js parallax-worklets sensor order))
