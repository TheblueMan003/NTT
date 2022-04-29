globals [
    test
    index
]

breed [ wolves wolf ]

turtles-own [
    speed
    color
]
wolves-own [
    test2
    test5-test?
]

to go
    let m2 5
    let m 2.0
end

to argtest [ value ]
    let m 5
    set m 0.0
    ;; let k turtles with [ test = 0 ]
    ifelse value = 0.0 [
        if m = 0 [
            right 5
        ]
    ]
    value > 2.0
    [
        left 5
    ]
    [
        fw 5
    ]
end

to arg
    let m 5.0
    set m 0
    right 0
    fw 1
    argtest 5.0
    argtest 5
    show "test"
end

to setup
    create-turtles 10 [
        set speed 0
        set xcor 0
    ]
end

to go
    let m 1
    ask turtles [
        forward 1
    ]
end