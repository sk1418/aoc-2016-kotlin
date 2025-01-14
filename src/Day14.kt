import utils.*

// https://adventofcode.com/2016/day/14

fun main() {
    val today = "Day14"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day14Solution(input.single()).idxFor64thKey()
    fun part2(input: List<String>) = Day14Solution(input.single()).idxFor64thKey(true)


    chkTestInput(Part1, testInput, 22728) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 22551) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class Day14Solution(val salt: String) {
    private val tripleRe = """([a-f0-9])\1\1""".toRegex()
    private val dict = mutableMapOf<Int, String>()
    private var idx = 0
    private var keyCount = 0

    private fun String.md5Hash(stretching: Boolean = false): String {
        var hash = this
        repeat(if (stretching) 2017 else 1) { hash = hash.md5() }
        return hash
    }

    fun idxFor64thKey(stretching: Boolean = false): Int {
        dict.clear()
        while (keyCount < 64) {
            val hash = dict.getOrPut(idx) { "$salt$idx".md5Hash(stretching) }
            val triple = tripleRe.find(hash)
            if (triple != null) {
                val five = "${triple.value.first()}".repeat(5)
                if ((idx + 1..idx + 1000).any { five in dict.getOrPut(it) { "$salt$it".md5Hash(stretching) } })
                    keyCount++
            }
            idx++
        }
        return idx - 1
    }
}