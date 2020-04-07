package com.thiamath.logparser.app.test

fun testSuite() = arrayListOf(
        ParserTest, FollowTest
)

interface Test {
    fun launchTest()
}

object FollowTest : Test {
    override fun launchTest() {
        //TODO("Not yet implemented")
    }
}