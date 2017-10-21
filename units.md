# Adding units to the game board

## Intro

Now that we have drawn a Checkers board the next step is adding units to the game board. The units in this case will be
the red and black pieces for each respective side. Each unit will have a position on the board, I am going to use `x`
and `y` values. So we can assume that `x` is whichever column (with respect to the tiles) the unit is in and `y` is
whichever row the unit is in (e.g. [0,0] would refer to the top left tile). 
 
We have established that our units can have state (where the unit is at), and that state can change when a player moves
a unit. For now, let's not worry about where a unit can move, and focus on moving a unit from one position on the board
to another. Even with this modest requirement we still need to deal with keeping track of all of the units, and the
changes to state that are made over time. 

To manage the state of our units we are going to use an atom. "Atoms provide a way to manage shared, synchronous, 
independent state."[1](http://clojure.org/atoms) This is exactly what we want to do. For every move that a player makes
we want to update the position of that unit in the atom.

In classic Checkers, we know that there are two teams (red and black), we know that each team has 12 units, the units 
all sit on black tiles, and the black units start on one side while the red units start on the other. They each fill up
three rows of black tiles in their starting positions. This information may seem unnecessary or obvious, but it helps me 
when I am writing a program to get all of the known information out on the table.

## Creating a game-state atom

The documentation says that "the intended use of atom is to hold one of Clojure's immutable data structures"[1]. We are
going to be holding a vector of maps. The information we need for each unit is, team, x, and y. With this information we 
can draw the correct unit anywhere on the board. 

Open up the `./src/gameboard/core.cljs` file in your project, and add the following right below `(tile-size 50)`.

```clojure
(def black "black")
(def red "red")

(def game-state
  (atom 
    {:units 
     [{:team :black, :x 1, :y 0},
      {:team :black, :x 3, :y 0},
      {:team :black, :x 5, :y 0},
      {:team :black, :x 7, :y 0},
      {:team :black, :x 0, :y 1},
      {:team :black, :x 2, :y 1},
      {:team :black, :x 4, :y 1},
      {:team :black, :x 6, :y 1},
      {:team :black, :x 1, :y 2},
      {:team :black, :x 3, :y 2},
      {:team :black, :x 5, :y 2},
      {:team :black, :x 7, :y 2},
      {:team :red, :x 1, :y 5},
      {:team :red, :x 3, :y 5},
      {:team :red, :x 5, :y 5},
      {:team :red, :x 7, :y 5},
      {:team :red, :x 0, :y 6},
      {:team :red, :x 2, :y 6},
      {:team :red, :x 4, :y 6},
      {:team :red, :x 6, :y 6},
      {:team :red, :x 1, :y 7},
      {:team :red, :x 3, :y 7},
      {:team :red, :x 5, :y 7},
      {:team :red, :x 7, :y 7}]}))
```
    
[code at this point](...)
    
## Drawing units

We are now keeping track of all the units and their positions on the board, the next step is drawing the units in their
respective positions. Let's write a `draw-unit!` function that takes a unit argument, and renders a red/black circle in 
the correct position on the board. 

```clojure
(defn draw-unit! [unit]
  (let [[x y] (board-position (:x unit)(:y unit))]
    (.beginPath ctx)
    (.arc ctx (+ x tile-offset) (+ y tile-offset) 20 0  (* Math/PI 2) false)
    (set! (.-fillStyle ctx) (:team unit))
    (.fill ctx)

    (set! (.-lineWidth ctx) 3)
    (set! (.-strokeStyle ctx) "white")
    (.stroke ctx)))
```

The `draw-unit!` function takes a unit which will give us all the information we need for drawing the piece. We need to
start with the `beginPath` method as we did when drawing the game board. Then we use the 
[arc method](https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/arc) to draw a circle. It takes
x, y, radius, startAngle, endAngle, and anticlockwise. We set x and y as the unit's `x` and `y` values. We set the
radius as 25 pixels, set the startAngle at 0, and the endAngle at 2π, and the anticlockwise to false. The radius is 
pretty straightforward, but the startAngle and endAngle are a little weird. Why would we need to use 2π as the endAngle.
The reason is that the some Canvas API designers decided that radian would make more sense than degrees, so that's what
we have to use. Anticlockwise isn't really relevant for a circle so we set it to false. We set the fill to be whatever
color the team is (red or black), and then draw a stroke around the circle as we did with the tiles on the game board.

You might've noticed that there are a couple terms in `draw-unit!` that we haven't defined yet - `board-position` and `tile-offset`. Hopefully the names give you some insight as to what these should do. Try implementing them yourself before we continue.

Currently, the x and y units of our game piece atom correspond to the index `0-7` that each piece sits on, rather than the pixel position required by our canvas. `board-position` solves this problem for us, you can paste it above `draw-unit!`:

```clojure
(defn board-position [x y]
   (mapv #(* tile-size %) [x y]))
```

However, this would still draw the pieces right on the top left corner of each tile - so we define a constant `tile-offset` that gives us the length from the edge of the tile to the center (since our tiles
are square this works for both x and y) - again, you can paste this above `draw-unit`:

```clojure
(def tile-offset (/ tile-size 2))
```

Finally, add this to the end of our file:

```clojure
(mapv draw-unit! (:units @game-state))
```

This function is mapping all of our units over the `draw-unit!` function which draws all of our game pieces on their
respective tiles.

[code at this point](https://github.com/aconanlai/cljs-gameboard-tutorial/tree/05_drawing_pieces)

Your game board should look like this:

![](http://imgur.com/W1Gw3lE.png)

Well done!