globals [
  erasing?        ;; is the current draw-cells mouse click erasing or adding?
  initial-density
  bgcolor
  fgcolor
]

patches-own [
  living?         ;; indicates if the cell is living
  live-neighbors  ;; counts how many neighboring cells are alive
]

to setup
  set initial-density 0.35
  set bgcolor 0
  set fgcolor 1
  clear-all
  ask patches
    [ ifelse (random-float 1.0) < initial-density
      [ cell-birth ]
      [ cell-death ] ]
  reset-ticks
end

to cell-birth
  set living? true
  set pcolor fgcolor
end

to cell-death
  set living? false
  set pcolor bgcolor
end

to go
  show "starting"
  ask patches
    [ set live-neighbors count (neighbors with [living?]) ]
    
  ;; Starting a new "ask patches" here ensures that all the patches
  ;; finish executing the first ask before any of them start executing
  ;; the second ask.  This keeps all the patches in synch with each other,
  ;; so the births and deaths at each generation all happen in lockstep.

  let y 0
  while y < 10 [
    let x 0
    while x < 10 [
      ask patches-at x y [
          ifelse living? [
            show2 "#"
          ] [
            show2 " "
          ]
      ]
      set x x + 1
    ]
    show2 "\n"
    set y y + 1
  ]

  
  ask patches
    [ ifelse live-neighbors = 3
      [ cell-birth ]
      [ if live-neighbors != 2
        [ cell-death ] ] ]
  tick
end

; Copyright 1998 Uri Wilensky.
; See Info tab for full copyright and license.