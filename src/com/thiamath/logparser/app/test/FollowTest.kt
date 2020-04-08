package com.thiamath.logparser.app.test

import com.thiamath.logparser.app.follow.FollowManagerImpl
import com.thiamath.logparser.app.model.FollowResult
import java.io.File
import java.io.FileWriter
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.LinkedBlockingDeque
import kotlin.test.assertEquals

private const val TEST_RESOURCES_PATH = "resources/test/followTest"

object FollowTest : Test {
    override fun launchTest() {
        arrayListOf(FollowManagerImplTest)
                .forEach { it.launchTest() }
    }

    object FollowManagerImplTest : Test {
        override fun launchTest() {
            val testFilename = "$TEST_RESOURCES_PATH/FollowManagerImplHappyPathTest.txt"
            // Setup
            File(testFilename).writeText("""
                    1586166210000 Peter Alice
                    1586166220000 Peter Perry
                    1586166240000 Perry Bob
                    1586166321000 Alice Peter
                """.trimIndent().plus("\n"))
            // Given
            val testQueue = LinkedBlockingDeque<FollowResult>()
            val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
            val followManager = FollowManagerImpl(testQueue, resultIntervalMs = 4000, clock = clock)
            // When
            try {
                followManager.followParseFile(testFilename, "Peter")
                // Then
                Thread.sleep(500)
                assertEquals(1, testQueue.count(), "The first round must have one result on queue")
                assertEquals(FollowResult(
                        fromHostnameList = listOf("Alice", "Perry"),
                        toHostnameList = listOf("Alice"),
                        mostActiveHostname = "Peter"
                ), testQueue.first, "The queued result must satisfy on first round")
                val producerThread = Thread {
                    val file = File(testFilename)
                    FileWriter(file, true).buffered().use {
                        it.appendln("1586167210000 Alice Bob")
                        it.appendln("1586167220000 Alice Perry")
                        it.appendln("1586167240000 Alice Bob")
                        it.appendln("1586167239000 Peter Bob")
                        it.appendln("1586167321000 Alice Peter")
                    }
                }
                producerThread.start()
                assertEquals(1, testQueue.count(), "The second round must have only one result on queue")
                assertEquals(FollowResult(listOf("Alice", "Perry"), listOf("Alice"), "Peter"), testQueue.first, "The queued result must satisfy on second round")
                Thread.sleep(1000)
                followManager.clock = Clock.offset(clock, Duration.ofSeconds(5))
                Thread.sleep(1000)
                assertEquals(2, testQueue.count(), "The third round must have two results on queue")
                testQueue.pollFirst()
                assertEquals(FollowResult(listOf("Bob"), listOf("Alice"), "Alice"), testQueue.first, "The queued result must satisfy on third round")
                producerThread.join()
            } finally {
                // cleanup
                followManager.stop()
                File(testFilename).delete()
            }
        }
    }
}