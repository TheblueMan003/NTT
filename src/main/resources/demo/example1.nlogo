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
        argtest test + index * index  + (test - index)
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
    right 1
    fw 1
end