(ns status-im.ui.screens.wallet-connect.styles
  (:require [quo.design-system.colors :as colors]))

(defn sheet []
  {:flex 1})

(defn acc-sheet []
  {:background-color        colors/white
   :border-top-right-radius 16
   :border-top-left-radius  16
   :padding-bottom          60})

(defn header [small-screen?]
  {:flex-direction  :row
   :align-items     :center
   :justify-content :space-between
   :padding-top     (when-not small-screen? 16)
   :padding-left    16})
