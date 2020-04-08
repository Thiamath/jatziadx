package com.thiamath.logparser.app.parse

import com.thiamath.logparser.app.model.LogLine
import com.thiamath.logparser.app.model.ParseResult
import java.io.File

const val TIMESTAMP_SORTING_CONFIDENCE_MS = 300000

class ParseManagerImpl : ParseManager {
    override fun parseFile(filename: String, initDatetime: Long, endDatetime: Long, hostname: String): ParseResult {
        val setResult = HashSet<String>()
        var linesRead = 0
        File(filename).bufferedReader()
                .lineSequence()
                .map { LogLine.fromString(it) }
                .takeWhile { it.timestamp < endDatetime + TIMESTAMP_SORTING_CONFIDENCE_MS }
                .forEach { logLine ->
                    linesRead++
                    if (logLine.timestamp in initDatetime..endDatetime && logLine.receiverHostname == hostname) {
                        setResult.add(logLine.senderHostname)
                    }
                }
        println("Lines read -> $linesRead")
        return ParseResult(setResult.toList())
    }
}