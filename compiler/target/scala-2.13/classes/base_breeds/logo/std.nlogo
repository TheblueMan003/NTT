globals[
    black
    gray
    white
    red
    orange
    brown
    yellow
    green
    lime
    turquoise
    cyan
    sky
    blue
    violet
    magenta
    pink

    max-pxcor
    max-pycor
    min-pxcor
    min-pycor

    ticks
]
agents-own [
    pxcor pycor shape size
]
turtles-own [
    xcor ycor heading shape-size color visible
]

to-report random-xcor
    report min-pxcor + random (max-pxcor - min-pxcor)
end
to-report random-ycor
    report min-pycor + random (max-pycor - min-pycor)
end

patches-own [
    pcolor
]

to default_setup
    set black 0
    set gray 5
    set white 9.9
    set red 15
    set orange 25
    set brown 35
    set yellow 45
    set green 55
    set lime 65
    set turquoise 75
    set cyan 85
    set sky 95
    set blue 105
    set violet 115
    set magenta 125
    set pink 135
end

to default_init_turtle
    set shape-size 1
end

to default_init 
    set heading 0.0
    distance myself
    face myself
end

to show-turtle
    set visible true
end

to hide-turtle
    set visible false
end

to-report dx
    report cos heading
end
to-report dy
    report sin heading
end

to face [ value ]
    let x [ xcor ] of value
    let y [ ycor ] of value
    facexy x y
end

to facexy [ x y ]
    set heading atan (x - xcor) (y - ycor)
end

to forward [ value ]
    let m value * cos heading
    set xcor xcor + m
    set m value * sin heading
    set ycor ycor + m
end

to fd [ value ]
    forward value
end

to-report can-move? [ value ]
    set heading heading
    report true
end

to left [ value ]
    set heading heading + value
end
to lt [ value ]
    left value
end

to right [ value ]
    set heading heading - value
end
to rt [ value ]
    set heading heading - value
end

to setxy [ x y ]
    set xcor x
    set ycor y

    set pxcor default_to_int x
    set pycor default_to_int y
end

to move-to [ a ]
    setxy [ xcor ] of a [ ycor ] of a
end

to home
    setxy 0.0 0.0
end

to-report distance [ value ]
    report distancexy [ xcor ] of value [ ycor ] of value
end
to-report distancexy [ x y ]
    let dx (xcor - x) ^ 2
    let dy (ycor - y) ^ 2
    let d sqrt(dx + dy)
    report d
end

to tick-advance [ value ]
    set ticks (ticks + value)
end

to tick
    set ticks ticks + 1
end

to reset-ticks
    set ticks 0
end