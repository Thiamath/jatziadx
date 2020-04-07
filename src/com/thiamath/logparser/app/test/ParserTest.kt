package com.thiamath.logparser.app.test

import com.thiamath.logparser.app.model.ParseResult
import com.thiamath.logparser.app.parse.ParseHandlerImpl
import com.thiamath.logparser.app.parse.ParseManager
import com.thiamath.logparser.app.parse.ParseManagerImpl
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.asserter

private const val TEST_RESOURCES_PATH = "resources/test/parserTest"

object ParserTest : Test {
    override fun launchTest() {
        arrayListOf(ParserHandlerImplTest, ParseManagerImplTest)
                .forEach { it.launchTest() }
    }

    object ParserHandlerImplTest : Test {
        override fun launchTest() {
            arrayListOf(ParserHandlerImplHappyPathTest, ParserHandlerImplValidationTest)
                    .forEach { it.launchTest() }
        }

        object ParserHandlerImplHappyPathTest : Test {
            override fun launchTest() {
                // Given the parser handler and stubbed manager
                val parseManager = object : ParseManager {
                    override fun parseFile(filename: String, initDatetime: Long, endDatetime: Long, hostname: String): ParseResult {
                        return ParseResult(emptyList())
                    }
                }
                val parseHandler = ParseHandlerImpl(parseManager)
                // When
                try {
                    parseHandler.parseFile("Test.txt", "1000", "2000", "hostname_test")
                } catch (e: Exception) {
                    asserter.fail("parseFile should work")
                }
            }
        }

        object ParserHandlerImplValidationTest : Test {
            override fun launchTest() {
                // Given the parser handler and stubbed manager
                val parseManager = object : ParseManager {
                    override fun parseFile(filename: String, initDatetime: Long, endDatetime: Long, hostname: String): ParseResult {
                        return ParseResult(emptyList())
                    }
                }
                val parseHandler = ParseHandlerImpl(parseManager)
                // When
                assertFailsWith(IllegalArgumentException::class, "Call must fail with IllegalArgumentException") {
                    parseHandler.parseFile("Test.txt", "abc", "xyz", "hostname_test")
                }
            }
        }
    }

    object ParseManagerImplTest : Test {
        override fun launchTest() {
            arrayListOf(ParseManagerImplHappyPathTest, ParseManagerImplBarelySortedTest)
        }

        object ParseManagerImplHappyPathTest : Test {
            override fun launchTest() {
                println("The parser must distinguish hostnames that are not perfectly sorted")
                // Given the ParseManager
                val parseManager = ParseManagerImpl()
                // When
                val parseResult = parseManager.parseFile("$TEST_RESOURCES_PATH/ParseManagerImplHappyPathTest.txt", 1586166221000, 1586166321000, "Perry")
                // Then
                assertEquals(ParseResult(
                        listOf("Peter")
                ), parseResult, "The result must be equal to the expected")
            }
        }

        object ParseManagerImplBarelySortedTest : Test {
            override fun launchTest() {
                println("The parser must distinguish hostnames that are not perfectly sorted")
                // Given the ParseManager
                val parseManager = ParseManagerImpl()
                // When asking for the list of hostnames between 9:35:00 and 9:40:00 that connected to Perry
                val parseResult = parseManager.parseFile("$TEST_RESOURCES_PATH/ParseManagerImplBarelySortedTest.txt", 1586166221000, 1586166321000, "Perry")
                // Then
                assertEquals(ParseResult(
                        listOf("Aleksandra", "Ronin", "Kainani", "Glorian")
                ), parseResult, "The result must be equal to the expected")
            }
        }
    }
}