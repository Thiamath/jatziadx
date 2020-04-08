package com.thiamath.logparser.app.follow

class FollowHandlerImpl(override val followManager: FollowManager) : FollowHandler {
    override fun followParseFile(filename: String, hostname: String) {
        followManager.followParseFile(filename, hostname)
    }
}