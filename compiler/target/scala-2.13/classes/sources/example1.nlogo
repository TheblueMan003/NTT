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
        set xcor 0
    ]
end

to-report fct [ value ]
    report value + 1
end

to go
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
        show xcor
        show fct test
    ]
    show "part 2"
    ask wolves [
        set index index + 1
        show index
        let v [color] of myself
    ]
    show("end of tick")
end