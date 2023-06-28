(ns quo2.components.buttons.button.type-values
  (:require [quo2.foundations.colors :as colors]))

(defn get-values
  [{:keys [customization-color theme type background pressed?]}]
  (cond
    (= type :primary)                            {:icon-color           colors/white
                                                  :icon-secondary-color colors/white-opa-70
                                                  :label-color          colors/white
                                                  :background-color     (colors/custom-color
                                                                         customization-color
                                                                         50)}

    (= type :positive)                           {:icon-color           colors/white
                                                  :icon-secondary-color colors/white-opa-70
                                                  :label-color          colors/white
                                                  :background-color     colors/success-50}

    (and (= :blur background) (= type :grey))    {:icon-color           (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :icon-secondary-color (colors/theme-colors
                                                                         colors/neutral-80-opa-40
                                                                         colors/white-opa-70
                                                                         theme)

                                                  :label-color          (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :background-color     (if-not pressed?
                                                                          (colors/theme-colors
                                                                           colors/neutral-80-opa-5
                                                                           colors/white-opa-5
                                                                           theme)
                                                                          (colors/theme-colors
                                                                           colors/neutral-80-opa-10
                                                                           colors/white-opa-10
                                                                           theme))}

    (and (= :blur background) (= type :outline)) {:icon-color           (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :icon-secondary-color (colors/theme-colors
                                                                         colors/neutral-80-opa-40
                                                                         colors/white-opa-40
                                                                         theme)

                                                  :label-color          (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :border-color         (if-not pressed?
                                                                          (colors/theme-colors
                                                                           colors/neutral-80-opa-10
                                                                           colors/white-opa-10)
                                                                          (colors/theme-colors
                                                                           colors/neutral-80-opa-20
                                                                           colors/white-opa-20))}

    (= type :grey)                               {:icon-color           (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :icon-secondary-color (colors/theme-colors
                                                                         colors/neutral-50
                                                                         colors/neutral-40
                                                                         theme)
                                                  :label-color          (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :background-color     (if-not pressed?
                                                                          (colors/theme-colors
                                                                           colors/neutral-10
                                                                           colors/neutral-90
                                                                           theme)
                                                                          (colors/theme-colors
                                                                           colors/neutral-20
                                                                           colors/neutral-60
                                                                           theme))}

    (= type :dark-grey)                          {:icon-color           (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :icon-secondary-color (colors/theme-colors
                                                                         colors/neutral-50
                                                                         colors/neutral-40
                                                                         theme)
                                                  :label-color          (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :background-color     (if-not pressed?
                                                                          (colors/theme-colors
                                                                           colors/neutral-20
                                                                           colors/neutral-70
                                                                           theme)
                                                                          (colors/theme-colors
                                                                           colors/neutral-30
                                                                           colors/neutral-60
                                                                           theme))}

    (= type :outline)                            {:icon-color           (colors/theme-colors
                                                                         colors/neutral-50
                                                                         colors/neutral-40
                                                                         theme)
                                                  :icon-secondary-color (colors/theme-colors
                                                                         colors/neutral-50
                                                                         colors/neutral-40
                                                                         theme)
                                                  :label-color          (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :border-color         (if-not pressed?
                                                                          (colors/theme-colors
                                                                           colors/neutral-30
                                                                           colors/neutral-70
                                                                           theme)
                                                                          (colors/theme-colors
                                                                           colors/neutral-40
                                                                           colors/neutral-60
                                                                           theme))}

    (= type :ghost)                              {:icon-color           (colors/theme-colors
                                                                         colors/neutral-50
                                                                         colors/neutral-40
                                                                         theme)
                                                  :icon-secondary-color (colors/theme-colors
                                                                         colors/neutral-50
                                                                         colors/neutral-40
                                                                         theme)
                                                  :label-color          (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :background-color     (when pressed?
                                                                          (colors/theme-colors
                                                                           colors/neutral-10
                                                                           colors/neutral-80
                                                                           theme))}

    (= type :danger)                             {:icon-color           colors/white
                                                  :icon-secondary-color colors/white-opa-70
                                                  :label-color          colors/white
                                                  :background-color     colors/danger-50}

    (= background :photo)                        {:icon-color           (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :icon-secondary-color (colors/theme-colors
                                                                         colors/neutral-80-opa-40
                                                                         colors/neutral-30
                                                                         theme)
                                                  :label-color          (colors/theme-colors
                                                                         colors/neutral-100
                                                                         colors/white
                                                                         theme)
                                                  :background-color     (if-not pressed?
                                                                          (colors/theme-colors
                                                                           colors/white-opa-40
                                                                           colors/neutral-80-opa-40
                                                                           theme)
                                                                          (colors/theme-colors
                                                                           colors/white-opa-50
                                                                           colors/neutral-80-opa-50
                                                                           theme))}
    ;; used to be :shell
    (= type :black)                              {:icon-color       colors/white
                                                  :label-color      colors/white
                                                  :background-color (if-not pressed?
                                                                      colors/neutral-95
                                                                      ;pressed value was incorrect
                                                                      colors/neutral-80)}))
