globals [
    test
    index
]

turtles-own [
    speed
    color
]

breed [ wolves wolf ]

to setup
    create-turtles 10 [
        set speed 0
        set xcord 0
    ]
end

to-report fct [ value ]
    report value + 1
end

to go
    let p 1
    show("start of tick")
    ask turtles [
        set color random 4
        show "selected"
    ]
    ask turtles with [color < 1] [
        show("filtered")
    ]
    show("middle of tick")
    ask turtles [
        left 90.0
        show fct test
    ]
    show "part 2"
    ask turtles [
        set index index + 1
        show index
    ]
    show("end of tick")

    let c [ speed ] of turtles
end