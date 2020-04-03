package com.thiamath.logparser.cmd

sealed class Command(
        val action: Action
) {
    enum class Action(val command: String) {
        TEST("test"), PARSE("parse"), FOLLOW("follow")
    }

    abstract fun execute()
}

class TestCommand : Command(Action.TEST) {
    override fun execute() {
        TODO("Not yet implemented")
    }
}

class ParseCommand(
        val filename: String,
        val initDatetime: Long,
        val endDatetime: Long,
        val hostname: String
) : Command(Action.PARSE) {
    override fun execute() {
        TODO("Not yet implemented")
    }
}

class FollowCommand(
        val filename: String
) : Command(Action.FOLLOW) {
    override fun execute() {
        TODO("Not yet implemented")
    }
}
