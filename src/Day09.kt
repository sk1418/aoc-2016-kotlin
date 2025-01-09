import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day09"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day09Solution().decompressedLen(input.single())

    fun part2(input: List<String>) = Day09Solution().recursiveDecompressedLen(input.single())

    chkTestInput(Part1, testInput, 18) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 20L) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class Day09Solution() {
    private val markerRe = """[(](\d+)x(\d+)[)]""".toRegex()
    fun decompressedLen(line: String): Int {
        val markerResult = markerRe.find(line) ?: return line.length
        var result = 0
        val (markerStart, markerEnd) = markerResult.range.let { it.first to it.last }
        val (len, times) = markerResult.destructured.toList().map { it.toInt() }
        result += markerStart + len * times
        result += decompressedLen(line.drop(markerEnd + len + 1))
        return result
    }

    fun recursiveDecompressedLen(line: String): Long {
        val markerResult = markerRe.find(line) ?: return line.length.toLong()
        var result = 0L
        val (markerStart, markerEnd) = markerResult.range.let { it.first to it.last }
        val (len, times) = markerResult.destructured.toList().map { it.toInt() }
        // Count leading characters (to the left of the processed group).
        result += markerStart
        result += times * recursiveDecompressedLen(line.substring(markerEnd + 1, markerEnd + len + 1))
        result += recursiveDecompressedLen(line.drop(markerEnd + len + 1))
        return result
    }
}