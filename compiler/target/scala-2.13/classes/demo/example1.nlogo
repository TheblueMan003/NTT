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
        set p (p + 5) mod 4
    ]
    show "part 2"
    ask wolves [
        set index index + 1
        show index
        let v [color] of myself
        hatch 1 [
            show "hello"
        ]
    ]
    let l min-n-of turtles 5 [ color ]
    show("end of tick")
    let c [ color ] of turtles
    let d count (turtles-on neighbors) with [ color != [ color ] of myself ]
end