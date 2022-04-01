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

to go
    let m 5
    ask turtles [
        ask myself [
            right m
        ]
        right m
    ]
end


to argtest [ value ]
    ifelse value = 0 [
        right 5
    ]
    value > 2
    [
        left 5
    ]
    [
        fw 5
    ]
end

to noarg
    let m 5
    set m 0
    right 0
    fw 1
end