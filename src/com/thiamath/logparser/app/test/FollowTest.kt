package com.thiamath.logparser.app.test

import com.thiamath.logparser.app.follow.FollowManagerImpl
import com.thiamath.logparser.app.model.FollowResult
import java.io.File
import java.util.concurrent.LinkedBlockingDeque
import kotlin.test.assertEquals

private const val TEST_RESOURCES_PATH = "resources/test/parserTest"

object FollowTest : Test {
    override fun launchTest() {
        arrayListOf(FollowManagerImplTest)
                .forEach { it.launchTest() }
    }

    object FollowManagerImplTest : Test {
        override fun launchTest() {
            // Given
            val testQueue = LinkedBlockingDeque<FollowResult>()
            val followManager = FollowManagerImpl(testQueue, resultIntervalMs = 4000)
            // When
            val followThread = Thread {
                followManager.followParseFile("$TEST_RESOURCES_PATH/FollowManagerImplHappyPathTest.txt", "Peter")
            }
            followThread.start()
            // Then
            assertEquals(testQueue.count(), 1, "The first round must have only one result on queue.")
            assertEquals(testQueue.first, FollowResult(listOf("Alice", "Perry"), listOf("Alice"), "Peter"), "The queued result must satisfy.")
            val producerThread = Thread {
                File("$TEST_RESOURCES_PATH/FollowManagerImplHappyPathTest.txt").bufferedWriter().use {
                    it.append("1586167210000 Alice Bob\n" +
                            "1586167220000 Alice Perry\n" +
                            "1586167240000 Alice Bob\n" +
                            "1586167239000 Peter Bob\n" +
                            "1586167321000 Alice Peter")
                }
            }
            producerThread.start()
            assertEquals(testQueue.count(), 1, "The second round must have only one result on queue.")
            assertEquals(testQueue.first, FollowResult(listOf("Alice", "Peter"), listOf("Alice"), "Peter"), "The queued result must satisfy.")
            Thread.sleep(5000)
            assertEquals(testQueue.count(), 2, "The third round must have two results on queue.")
            testQueue.pollFirst()
            assertEquals(testQueue.first, FollowResult(listOf("Bob"), listOf("Alice"), "Alice"), "The queued result must satisfy.")
            followThread.interrupt()
            producerThread.join()
            followThread.join()
        }
    }
}