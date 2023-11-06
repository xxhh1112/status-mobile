(ns status-im2.contexts.wallet.common.utils
  (:require [clojure.string :as string]
            [status-im2.constants :as constants]))

(defn get-first-name
  [full-name]
  (first (string/split full-name #" ")))

(defn get-balance-by-address
  [balances address]
  (->> balances
       (filter #(= (:address %) address))
       first
       :balance))

(defn get-account-by-address
  [accounts address]
  (some #(when (= (:address %) address) %) accounts))

(defn prettify-balance
  [balance]
  (str "$" (.toFixed (if (number? balance) balance 0) 2)))

(defn get-derivation-path
  [number-of-accounts]
  (str constants/path-wallet-root "/" number-of-accounts))

(defn format-derivation-path
  [path]
  (string/replace path "/" " / "))

(defn get-formatted-derivation-path
  [number-of-accounts]
  (let [path (get-derivation-path number-of-accounts)]
    (format-derivation-path path)))

(defn calculate-raw-balance
  [raw-balance decimals]
  (if-let [n (utils.number/parse-int raw-balance nil)]
    (/ n (Math/pow 10 (utils.number/parse-int decimals)))
    0))

(defn total-per-token
  [item]
  (reduce (fn [ac balances]
            (+ (calculate-raw-balance (:rawBalance balances)
                                      (:decimals item))
               ac))
          0
          (vals (:balancesPerChain item))))
