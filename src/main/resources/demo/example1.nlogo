globals [
    test
    index
]

breed [ wolves wolf ]

turtles-own [
    test
    test3-test?
    test4
    other
]
wolves-own [
    test2
    test5-test?
]
sheeps-own [
    wool
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

to go 
    fct value
end

to fct [ arg ]
    ???
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