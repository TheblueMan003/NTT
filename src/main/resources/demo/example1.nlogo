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
        log test4
    ]
end

to argtest [ value ]
    ifelse-value test [
        argtest test + index * index  + (test - index) + value
    ]
    test = 2
    [
        argtest log 5
    ]
    [
        argtest test
    ]
end

to noarg
    let m 5
    set m 5
    right test2
    fw 1
    log 5
end