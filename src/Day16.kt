import utils.*

// https://adventofcode.com/2016/day/16
fun main() {
    val today = "Day16"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>, diskLength: Int) = Day16Solution(input.single(), diskLength).calcChecksum()
    fun part2(input: List<String>) = part1(input, 35651584)

    chkTestInput(Part1, testInput, "01100") { part1(it, 20) }
    solve(Part1, input) { part1(it, 272) }

    solve(Part2, input) { part2(it) }
}

private class Day16Solution(val input: String, val len: Int) {
    private fun strToFillDisk(): String {
        var result = input
        while (result.length < len) {
            val b = result.reversed().replace('0', 'x').replace('1', '0').replace('x', '1')
            result = "${result}0$b"
        }
        return result.take(len)
    }

    private fun String.chksum() = this.toList().chunked(2).map { (a, b) -> if (a == b) '1' else '0' }

    fun calcChecksum(): String {
        var result = strToFillDisk().chksum()
        while (result.size % 2 == 0) {
            result = result.joinChars().chksum()
        }
        return result.joinChars()
    }
}