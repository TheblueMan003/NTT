globals [
  initial-trees   ;; how many trees (green patches) we started with
  density
  burned
  total
]

to setup
  set density 57
  clear-all
  ;; make some green trees
  ask patches [
    if (random 100) < density [
      set pcolor green
      set total total + 1.0
    ]
    ;; make a column of burning trees at the left-edge
    if pxcor = min-pxcor [
      set pcolor red
    ]
  ]
  ;; keep track of how many trees there are
  set initial-trees count (patches with [pcolor = green])
  reset-ticks
end

to go
  ;; stop the model when done
  if all? patches [ pcolor != red ] [
    stop
  ]
  ;; ask the burning trees to set fire to any neighboring non-burning trees
  ask patches with [ pcolor = red ] [ ;; ask the burning trees
    ask neighbors with [pcolor = green] [ ;; ask their non-burning neighbor trees
      set pcolor red ;; to catch on fire
      set burned burned + 1.0 ;; keep track of how many trees are on fire
    ]
    set pcolor red - 3.5 ;; once the tree is burned, darken its color
  ]
  let v (burned / total) * 100.0 ;; calculate the percentage of trees on fire
  show v
  tick ;; advance the clock by one “tick”
end


; Copyright 2006 Uri Wilensky.
; See Info tab for full copyright and license.