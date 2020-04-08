package com.thiamath.logparser.app.model

data class LogLine(
        val timestamp: Long,
        val senderHostname: String,
        val receiverHostname: String
) {
    companion object {
        fun fromString(line: String) = line.split(" ").let {
            LogLine(
                    timestamp = it[0].toLong(),
                    senderHostname = it[1],
                    receiverHostname = it[2]
            )
        }
    }
}
