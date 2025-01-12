import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day12"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day12Solution(instructions = input).run().variables["a"]

    fun part2(input: List<String>) = Day12Solution(instructions = input).runWithC1().variables["a"]

    chkTestInput(Part1, testInput, 42) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 42) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day12Solution(
    val variables: MutableNotNullMap<String, Int> = mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0).toMutableNotNullMap(),
    val instructions: List<String>,
) {
    var cur = 0
    private fun exec(line: String) {
        when {
            line.startsWith("inc") -> line.split(" ").last().also { variables[it]++ }
            line.startsWith("dec") -> line.split(" ").last().also { variables[it]-- }
            line.startsWith("cpy") -> line.split(" ").drop(1).also { (a, b) -> variables[b] = if (a.all { it.isDigit() }) a.toInt() else variables[a] }
            else -> line.split(" ").drop(1).also { (a, b) ->
                val notZero = if (a in variables.keys) variables[a] != 0 else a.toInt() != 0
                if (notZero) cur += (b.toInt() - 1)// -1 since there would be a cur++ call
            }
        }
    }

    fun run(): Day12Solution {
        while (cur <= instructions.lastIndex) {
            exec(instructions[cur])
            cur++
        }
        return this
    }

    fun runWithC1(): Day12Solution {
        variables["c"] = 1
        return run()
    }
}