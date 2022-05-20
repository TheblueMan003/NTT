turtles-own [
    xcord ycord angle
]

patches-own [
    pxcord pycord
]

agents-own[
    default_is_done
]

to default_init 
    set angle 0.0
end

to forward [ value ]
    let m value * cos angle
    set xcord xcord + m
    set m value * sin angle
    set ycord ycord + m
end

to fw [ value ]
    forward value
end

to-report can-move
    set angle angle
    report true
end

to left [ value ]
    set angle angle + value
end
to right [ value ]
    set angle angle - value
end

to setxy [ x y ]
    set xcord x
    set ycord y
end

to home
    set xcord 0.0
    set ycord 0.0
end