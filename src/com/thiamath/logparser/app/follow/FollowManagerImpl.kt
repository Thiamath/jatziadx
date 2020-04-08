package com.thiamath.logparser.app.follow

import com.thiamath.logparser.app.model.FollowResult
import java.util.concurrent.BlockingQueue

class FollowManagerImpl(
        override val resultQueue: BlockingQueue<FollowResult>,
        val resultIntervalMs: Long = 60 * 60 * 1000 // ONE HOUR
) : FollowManager {

    override fun followParseFile(filename: String, hostname: String) {

        TODO("Not yet implemented")
    }
}