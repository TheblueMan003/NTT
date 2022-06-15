globals [
  sample-car
  acceleration
  deceleration
  number-of-cars
]

turtles-own [
  speed
  speed-limit
  speed-min
]

to setup
  set acceleration 0.0045
  set deceleration 0.0026
  set number-of-cars 10
  clear-all
  ask patches [ setup-road ]
  setup-cars
  watch sample-car
  reset-ticks
end

to setup-road ;; patch procedure
  if pycor < 2 and pycor > -2 [ set pcolor white ]
end

to setup-cars
  set-default-shape turtles "car"
  create-turtles number-of-cars [
    set color blue
    set xcor random-xcor
    set heading 90
    ;; set initial speed to be in range 0.1 to 1.0
    set speed 0.1 + random-float 0.9
    set speed-limit 1
    set speed-min 0
    separate-cars
  ]
  set sample-car one-of turtles
  ask sample-car [ set color red ]
end

; this procedure is needed so when we click "Setup" we
; don't end up with any two cars on the same patch
to separate-cars ;; turtle procedure
  if any? other turtles-here [
    fd 1
    separate-cars
  ]
end

to go
  ;; if there is a car right ahead of you, match its speed then slow down
  ask turtles [
    let car-ahead one-of turtles-on patch-ahead 1
    ifelse car-ahead != nobody
      [ slow-down-car car-ahead ]
      [ speed-up-car ] ;; otherwise, speed up
    ;; don't slow down below speed minimum or speed up beyond speed limit
    if speed < speed-min [ set speed speed-min ]
    if speed > speed-limit [ set speed speed-limit ]
    fd speed
  ]
  tick
end

to slow-down-car [ car-ahead ] ;; turtle procedure
  ;; slow down so you are driving more slowly than the car ahead of you
  set speed [ speed ] of car-ahead - deceleration
end

to speed-up-car ;; turtle procedure
  set speed speed + acceleration
end


; Copyright 1997 Uri Wilensky.
; See Info tab for full copyright and license.