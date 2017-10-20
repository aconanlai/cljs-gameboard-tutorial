(ns gameboard.core
    (:require [
      goog.dom :as dom
    ]))

(enable-console-print!)

(def ctx (.getContext (dom/getElement "canvas") "2d"))
(def tile-size 50)
(def black "black")
(def red "red")

(def game-state
  (atom 
    {:units 
     [{:team black, :x 1, :y 0},
      {:team black, :x 3, :y 0},
      {:team black, :x 5, :y 0},
      {:team black, :x 7, :y 0},
      {:team black, :x 0, :y 1},
      {:team black, :x 2, :y 1},
      {:team black, :x 4, :y 1},
      {:team black, :x 6, :y 1},
      {:team black, :x 1, :y 2},
      {:team black, :x 3, :y 2},
      {:team black, :x 5, :y 2},
      {:team black, :x 7, :y 2},
      {:team red, :x 0, :y 5},
      {:team red, :x 2, :y 5},
      {:team red, :x 4, :y 5},
      {:team red, :x 6, :y 5},
      {:team red, :x 1, :y 6},
      {:team red, :x 3, :y 6},
      {:team red, :x 5, :y 6},
      {:team red, :x 7, :y 6},
      {:team red, :x 0, :y 7},
      {:team red, :x 2, :y 7},
      {:team red, :x 4, :y 7},
      {:team red, :x 6, :y 7}]}))

(defn tile-color [x y]
  (if (= (even? x) (even? y))
    "red"
    "black"))

(defn draw-tile! [x y color]
  (.beginPath ctx)
  (.rect ctx x y tile-size tile-size)

  (set! (.-fillStyle ctx) color)
  (.fill ctx)

  (set! (.-lineWidth ctx) 0.5)
  (set! (.-strokeStyle ctx) "black")
  (.stroke ctx))


(defn draw-board! [w h]
  (.setTransform ctx 1, 0, 0, 1, 0.5, 0.5)
  (set! (.-height (dom/getElement "canvas")) (+ 1 (* h tile-size)))
  (set! (.-width (dom/getElement "canvas")) (+ 1 (* w tile-size)))
  (mapv
    (fn [y]
      (mapv
        (fn [x] (draw-tile! (* tile-size x) (* tile-size y) (tile-color x y)))
      (range 0 w)))
(range 0 h)))

(def tile-offset (/ tile-size 2))

(defn board-position [x y]
   (mapv #(* tile-size %) [x y]))

(defn draw-unit! [unit]
  (let [[x y] (board-position (:x unit)(:y unit))]
    (.beginPath ctx)
    (.arc ctx (+ x tile-offset) (+ y tile-offset) 20 0  (* Math/PI 2) false)
    (set! (.-fillStyle ctx) (:team unit))
    (.fill ctx)

    (set! (.-lineWidth ctx) 3)
    (set! (.-strokeStyle ctx) "white")
    (.stroke ctx)
    ))

(draw-board! 8 8)

(mapv draw-unit! (:units @game-state))
