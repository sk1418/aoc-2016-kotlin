import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day07"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val abbaRe = """([a-z])(?!\1)([a-z])\2\1""".toRegex()
    fun part1(input: List<String>) = input.count {
        it.split('[', ']').let {
            abbaRe in it.slice(0..<it.size step 2).joinToString(separator = " ") &&
                abbaRe !in it.slice(1..<it.size step 2).joinToString(separator = " ")
        }
    }

    val abaRe = """([a-z])(?!\1)([a-z])\1""".toRegex()
    fun part2(input: List<String>) = input.count {
        it.split('[', ']').let {
            it.slice(0..<it.size step 2).joinToString(separator = " ")
                .windowed(3).filter { abaRe in it }.map { "${it[1]}${it[0]}${it[1]}" }
                .any { bab -> bab in it.slice(1..<it.size step 2).joinToString(separator = " ") }

        }
    }

    chkTestInput(Part1, testInput, 2) { part1(it) }
    solve(Part1, input) { part1(it) }
    chkTestInput(Part2, testInput, 3) { part2(it) }
    solve(Part2, input) { part2(it) }
}