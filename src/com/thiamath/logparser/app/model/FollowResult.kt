package com.thiamath.logparser.app.model

data class FollowResult(
        val fromHostnameList: List<String>,
        val toHostnameList: List<String>,
        val mostActiveHostname: String
)
