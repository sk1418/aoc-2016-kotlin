import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day03"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = input.map { line ->
        line.trim().split(" +".toRegex()).map { it.toInt() }.let { (a, b, c) -> Triple(a, b, c) }
    }.count { (a, b, c) -> a + b > c && a + c > b && b + c > a }

    fun part2(input: List<String>): Int = input.map { line -> line.trim().split(" +".toRegex()).map { it.toInt() } }
        .transpose().sumOf { col ->
            col.chunked(3).count { (a, b, c) -> a + b > c && a + c > b && b + c > a }
        }

    chkTestInput(Part1, testInput, 0) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}