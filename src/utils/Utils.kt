package utils

import java.io.File
import kotlin.time.measureTimedValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/inputs", "$name.txt").readLines()
fun readTestInput(name: String) = File("src/inputs", "$name-test.txt").readLines()

fun readInputAsInts(name: String) = File("src/inputs", "$name.txt").readLines().map { it.toInt() }

fun <T : Any?> T.alsoLog(prefix: String = "${this!!::class.simpleName}", msg: (T) -> String = { t: T -> "$t" }) = apply { println("[$prefix] ${msg(this)}") }

fun <T> chkTestInput(part: String, testInput: List<String>, expected: T, solveIt: (List<String>) -> T) = measureTimedValue { solveIt(testInput) }
    .also { (actual, time) ->
        println("$CYAN[$part::: TEST ]$RESET: $actual  ${if (actual == expected) "${CYAN}✔$RESET" else "${RED}✘$RESET Should be: $RED$expected$RESET  "}    ($time)")
        check(actual == expected)
    }

fun <T> solve(part: String, input: List<String>, solveIt: (List<String>) -> T) = measureTimedValue { solveIt(input) }
    .also { (result, time) -> println("$BLUE[$part:::RESULT]$RESET: $result    ($time)\n") }

const val Part1 = "Part 1"
const val Part2 = "Part 2"

const val RESET = "\u001B[0m"
const val RED = "\u001B[31m"
const val BLUE = "\u001B[34m"
const val CYAN = "\u001B[36m"