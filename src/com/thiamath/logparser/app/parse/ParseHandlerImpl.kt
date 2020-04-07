package com.thiamath.logparser.app.parse

import com.thiamath.logparser.app.model.ParseResult

class ParseHandlerImpl(override val parseManager: ParseManager) : ParseHandler {

    override fun parseFile(filename: String, initDatetime: String, endDatetime: String, hostname: String): ParseResult {
        try {
            return parseManager.parseFile(filename, initDatetime.toLong(), endDatetime.toLong(), hostname)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("initDatetime or endDatetime are not well formed. Those must be milliseconds from EPOCH.")
        }
    }
}