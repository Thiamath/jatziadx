package com.thiamath.logparser.app.parse

import com.thiamath.logparser.app.model.ParseResult
import java.io.File

const val TIMESTAMP_SORTING_CONFIDENCE_MS = 300000

class ParseManagerImpl : ParseManager {
    override fun parseFile(filename: String, initDatetime: Long, endDatetime: Long, hostname: String): ParseResult {
        val setResult = HashSet<String>()
        var linesRead = 0
        File(filename).bufferedReader()
                .lineSequence()
                .takeWhile { it.split(" ")[0].toLong() < endDatetime + TIMESTAMP_SORTING_CONFIDENCE_MS }
                .forEach { line ->
                    linesRead++
                    val split = line.split(" ")
                    val timestamp = split[0].toLong()
                    val fromHost = split[1]
                    val toHost = split[2]
                    if (timestamp in initDatetime..endDatetime && toHost == hostname) {
                        setResult.add(fromHost)
                    }
                }
        println("Lines read -> $linesRead")
        return ParseResult(setResult.toList())
    }
}