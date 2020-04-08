package com.thiamath.logparser.app.test

fun testSuite() = arrayListOf(
        ParserTest, FollowTest
)

interface Test {
    fun launchTest()
}