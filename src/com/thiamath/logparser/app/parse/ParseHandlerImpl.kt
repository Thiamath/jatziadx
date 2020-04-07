package com.thiamath.logparser.app.parse

import com.thiamath.logparser.app.model.ParseResult

class ParseHandlerImpl(override val parseManager: ParseManager) : ParseHandler {

    override fun parseFile(filename: String, initDatetime: String, endDatetime: String, hostname: String): ParseResult {
        TODO("Not yet implemented")
    }
}