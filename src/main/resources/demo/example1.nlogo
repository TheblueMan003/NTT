globals [
    test
    index
]

breed [ wolves wolf ]

turtles-own [
    test
    test3-test?
    test4
]
wolves-own [
    test
    test5-test?
]

to go
    let m test
    ask turtles [
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
        argtest test
    ]
    [
        argtest test
    ]
end

to noarg
    let m 5
    set m 5
    right m
    fw 1
    log 5
end