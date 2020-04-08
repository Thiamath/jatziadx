package com.thiamath.logparser.app.follow

import com.thiamath.logparser.app.model.FollowResult
import java.util.concurrent.BlockingQueue

fun followManager(queue: BlockingQueue<FollowResult>) = FollowManagerImpl(queue)

interface FollowManager {
    val resultQueue: BlockingQueue<FollowResult>
    fun followParseFile(filename: String, hostname: String)
}