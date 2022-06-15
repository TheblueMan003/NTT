turtles-own [
  mass
  velocity-x             ; particle velocity in the x axis
  velocity-y             ; particle velocity in the y axis
  force-accumulator-x    ; force exerted in the x axis
  force-accumulator-y    ; force exerted in the y axis
]

globals [
    step-size
    rate
    max-number-of-particles
    wind-constant-x
    wind-constant-y
    viscosity-constant
    gravity-constant
    initial-range-x
    initial-velocity-x
    initial-velocity-y
]

to setup
  clear-all
  set-default-shape turtles "circle"
  reset-ticks

  set step-size 0.02
  set rate 0.1
  set max-number-of-particles 200
  set wind-constant-x 0
  set wind-constant-y 0
  set viscosity-constant 0.5
  set gravity-constant 8.9
  set initial-range-x 2
  set initial-velocity-x 7
  set initial-velocity-y 30
end

to go
  create-particles
  compute-forces    ; calculate the forces and add them to the accumulator
  apply-forces      ; calculate the new location and speed by multiplying the
                    ; forces by the step-size
  tick-advance step-size
  display
end

to create-particles
  ;; using a Poisson distribution keeps the rate of particle emission
  ;; the same regardless of the step size
  let n random-poisson (rate * step-size)
  ask patch 0 (min-pycor + 1)
  [
    sprout n
    [
      set color blue
      set size 0.1 + random-float 1.0
      set mass size ^ 2  ; mass proportional to size squared
      set velocity-x initial-velocity-x - random-float initial-range-x + random-float initial-range-x
      set velocity-y initial-velocity-y
    ]
  ]
end

to compute-forces
  ask turtles
  [
    set force-accumulator-x 0
    set force-accumulator-y 0
    apply-gravity
    apply-wind
    apply-viscosity
  ]
end

to apply-gravity  ; turtle procedure
  set force-accumulator-y force-accumulator-y - gravity-constant / mass
end

to apply-wind  ; turtle procedure
  set force-accumulator-x force-accumulator-x + wind-constant-x
  set force-accumulator-y force-accumulator-y + wind-constant-y
end

to apply-viscosity  ; turtle procedure
  set force-accumulator-x force-accumulator-x - viscosity-constant * velocity-x
  set force-accumulator-y force-accumulator-y - viscosity-constant * velocity-y
end

; calculates the position of the particles at each step
to apply-forces
  ask turtles
  [
    ; calculate the new velocity of the particle
    set velocity-x velocity-x + force-accumulator-x * step-size
    set velocity-y velocity-y + force-accumulator-y * step-size
    ; calculate the displacement of the particle
    let step-x velocity-x * step-size
    let step-y velocity-y * step-size
    ;; if the turtle tries to leave the world let it die
    if patch-at step-x step-y = nobody [ die ]
    ;; if the turtle does not go out of bounds
    ;; add the displacement to the current position
    let new-x xcor + step-x
    let new-y ycor + step-y
    facexy new-x new-y
    setxy new-x new-y
  ]
end


; Copyright 2007 Uri Wilensky.
; See Info tab for full copyright and license.