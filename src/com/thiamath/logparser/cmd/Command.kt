package com.thiamath.logparser.cmd

import com.thiamath.logparser.app.parse.parseHandler
import com.thiamath.logparser.app.test.Test
import com.thiamath.logparser.app.test.testSuite
import java.time.LocalDateTime
import java.time.ZoneOffset

sealed class Command(
        val action: Action
) {
    enum class Action(val command: String) {
        TEST("test"), PARSE("parse"), FOLLOW("follow")
    }

    abstract fun execute()
}

class TestCommand : Command(Action.TEST) {
    override fun execute() = testSuite()
            .parallelStream()
            .forEach(Test::launchTest)
}

class ParseCommand(
        val filename: String,
        val initDatetime: String,
        val endDatetime: String,
        val hostname: String
) : Command(Action.PARSE) {
    override fun execute() {
        val parseResult = parseHandler().parseFile(filename, initDatetime, endDatetime, hostname)
        val init = LocalDateTime.ofEpochSecond(initDatetime.toLong() / 1000, ((initDatetime.toLong() % 1000) * 1000).toInt(), ZoneOffset.UTC)
        val end = LocalDateTime.ofEpochSecond(endDatetime.toLong() / 1000, ((endDatetime.toLong() % 1000) * 1000).toInt(), ZoneOffset.UTC)
        println("Hosts that connected to $hostname between $init, and $end: $parseResult")
    }
}

class FollowCommand(
        val filename: String
) : Command(Action.FOLLOW) {
    override fun execute() {
        TODO("Not yet implemented")
    }
}
