package com.thiamath.logparser.app.parse

import com.thiamath.logparser.app.model.ParseResult

fun parseHandler() = ParseHandlerImpl(parseManager())

interface ParseHandler {
    val parseManager: ParseManager
    fun parseFile(filename: String, initDatetime: String, endDatetime: String, hostname: String): ParseResult
}
