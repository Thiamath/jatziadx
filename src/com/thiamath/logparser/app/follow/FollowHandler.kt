package com.thiamath.logparser.app.follow

import com.thiamath.logparser.app.model.FollowResult
import java.util.concurrent.BlockingQueue

fun followHandler(queue: BlockingQueue<FollowResult>) = FollowHandlerImpl(followManager(queue))

interface FollowHandler {
    val followManager: FollowManager
    fun followParseFile(filename: String, hostname: String)
}