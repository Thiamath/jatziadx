package com.thiamath.logparser.app.follow

import com.thiamath.logparser.app.model.FollowResult
import com.thiamath.logparser.app.model.LogLine
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.lang.Thread.sleep
import java.time.Clock
import java.util.concurrent.BlockingQueue


class FollowManagerImpl(
        override val resultQueue: BlockingQueue<FollowResult>,
        val resultIntervalMs: Long = 60 * 60 * 1000, // ONE HOUR
        var clock: Clock = Clock.systemUTC()
) : FollowManager {

    private var followThread: Thread? = null
    private var running = true

    override fun followParseFile(filename: String, hostname: String) {
        var lastReadTimeMs = clock.millis()
        followThread = Thread {
            BufferedInputStream(File(filename).inputStream()).use {
                firstRead(it, hostname)
                val fromHostnameList = HashSet<String>()
                val toHostnameList = HashSet<String>()
                val hostnameActivity = HashMap<String, Int>()
                while (running) {
                    try {
                        val line = readLine(it)
                        if (line != "") {
                            parseLine(line, hostname, toHostnameList, fromHostnameList, hostnameActivity)
                        }
                        try {
                            sleep(100)
                        } catch (ex: InterruptedException) {
                            running = false
                        }
                        if (clock.millis() > lastReadTimeMs + resultIntervalMs) {
                            resultQueue.put(FollowResult(
                                    fromHostnameList.toList(),
                                    toHostnameList.toList(),
                                    hostnameActivity.entries.maxWith(Comparator { o1, o2 -> o1.value.compareTo(o2.value) })?.key
                                            ?: "No activity")
                            )
                            fromHostnameList.clear()
                            toHostnameList.clear()
                            hostnameActivity.clear()
                            lastReadTimeMs = clock.millis()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        running = true
        followThread!!.start()
    }

    private fun readLine(inputStream: BufferedInputStream): String {
        val line = StringBuffer()
        loop@ while (inputStream.available() > 0) {
            when (val char = inputStream.read().toChar()) {
                '\r' -> continue@loop
                '\n' -> break@loop
                else -> line.append(char)
            }
        }
        return line.toString()
    }

    private fun parseLine(line: String, hostname: String, toHostnameList: MutableSet<String>, fromHostnameList: MutableSet<String>, hostnameActivity: MutableMap<String, Int>) {
        val logLine = LogLine.fromString(line)
        if (logLine.receiverHostname == hostname) {
            toHostnameList.add(logLine.senderHostname)
        } else if (logLine.senderHostname == hostname) {
            fromHostnameList.add(logLine.receiverHostname)
        }
        hostnameActivity.computeIfPresent(logLine.senderHostname) { _, value -> value + 1 }
        hostnameActivity.putIfAbsent(logLine.senderHostname, 1)
    }

    private fun firstRead(inputStream: BufferedInputStream, hostname: String) {
        val fromHostnameList = HashSet<String>()
        val toHostnameList = HashSet<String>()
        val hostnameActivity = HashMap<String, Int>()
        var line = StringBuffer()
        loop@ while (inputStream.available() > 0) {
            when (val char = inputStream.read().toChar()) {
                '\r' -> continue@loop
                '\n' -> {
                    if (line.toString() != "") {
                        parseLine(line.toString(), hostname, toHostnameList, fromHostnameList, hostnameActivity)
                    } else {
                        break@loop
                    }
                    line = StringBuffer()
                }
                else -> line.append(char)
            }
        }
        resultQueue.put(FollowResult(
                fromHostnameList.toList().sorted(),
                toHostnameList.toList().sorted(),
                hostnameActivity.entries.maxWith(Comparator { o1, o2 -> o1.value.compareTo(o2.value) })!!.key)
        )
    }

    fun stop() {
        if (followThread != null) {
            followThread!!.interrupt()
            followThread!!.join()
        }
    }
}