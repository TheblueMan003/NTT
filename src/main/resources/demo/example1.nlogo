globals [
    test
    index
]

breed [ wolf wolves ]

turtles-own [
    test2
    test3-test?
]
wolves-own [
    test4
    test5-test?
]

to go

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
end