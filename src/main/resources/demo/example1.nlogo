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
to fct [ value ]
<<<<<<< HEAD
    set value ifelse-value value = 0
        [ value  + (((((((((1))))))))) ]
        value = 1
        [ value ]
        [ value ]
    set value 0
    report 5
=======
    set value 0 
>>>>>>> f33ea4146cb734c06122f5425c9ebb709097f186
end
to go
    let p 1
    show("start of tick")
    let m [ p ] of turtles
    ask turtles [
        ask turtles [
            forward 1.0
            show("walk")
        ]
    ]
    show("middle of tick")
    ask turtles [
        left 90.0
        show("turn")
    ]
    show("end of tick")

    let c [ speed ] of turtles
end