package com.thiamath.logparser.cmd

import com.thiamath.logparser.app.follow.followHandler
import com.thiamath.logparser.app.model.FollowResult
import com.thiamath.logparser.app.parse.parseHandler
import com.thiamath.logparser.app.test.Test
import com.thiamath.logparser.app.test.testSuite
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.LinkedBlockingQueue

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
        val filename: String,
        val hostname: String
) : Command(Action.FOLLOW) {
    override fun execute() {
        val queue = LinkedBlockingQueue<FollowResult>()
        followHandler(queue).followParseFile(filename, hostname)
        var lastWrite: LocalDateTime? = null
        while (true) {
            val followResult = queue.take()
            if (lastWrite == null) {
                println("On the file so far:")
            } else {
                println("Between $lastWrite and ${LocalDateTime.now()}:")
            }
            println("Hostnames connected to $hostname: ${followResult.toHostnameList}")
            println("Hostnames connected from $hostname: ${followResult.fromHostnameList}")
            println("Hostname most active: ${followResult.mostActiveHostname}")
            lastWrite = LocalDateTime.now()
        }
    }
}
