package com.thiamath.logparser.app.parse

import com.thiamath.logparser.app.model.ParseResult

fun parseManager() = ParseManagerImpl()

interface ParseManager {
    fun parseFile(filename: String, initDatetime: Long, endDatetime: Long, hostname: String): ParseResult
}