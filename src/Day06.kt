import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day06"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = input.map { it.toList() }.transpose().map {
        it.groupBy { it }.mapValues { it.value.size }.entries.maxByOrNull { it.value }!!.key
    }.joinChars()

    fun part2(input: List<String>) = input.map { it.toList() }.transpose().map {
        it.groupBy { it }.mapValues { it.value.size }.entries.minByOrNull { it.value }!!.key
    }.joinChars()

    chkTestInput(Part1, testInput, "easter") { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, "advent") { part2(it) }
    solve(Part2, input) { part2(it) }
}