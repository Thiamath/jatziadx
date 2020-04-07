package com.thiamath.logparser.app.model

data class ParseResult(
        val hostnameList: List<String>
) {
    override fun toString(): String = hostnameList.joinToString(", ")
}
