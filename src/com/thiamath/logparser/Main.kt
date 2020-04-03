package com.thiamath.logparser

import java.io.File

fun main(args: Array<String>) {
    val filename = args[0]
    var num = 0L
    File(args[0]).forEachLine { num++ }
    println(num)
}