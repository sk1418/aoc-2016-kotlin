import utils.Part1
import utils.readInput
import utils.readTestInput
import utils.solve

// https://adventofcode.com/2016/day/25
fun main() {
    val today = "Day25"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return findSmallestA(input)
    }

    solve(Part1, input) { part1(it) }
}

fun executeAssembunny(instructions: List<String>, a: Int): Boolean {
    val variables = mutableMapOf("a" to a, "b" to 0, "c" to 0, "d" to 0)
    val output = mutableListOf<Int>()
    var pointer = 0

    fun getValue(x: String) = x.toIntOrNull() ?: variables.getOrDefault(x, 0)

    while (pointer in instructions.indices && output.size < 20) { // Check first 20 values
        val parts = instructions[pointer].split(" ")
        when (parts[0]) {
            "cpy" -> variables[parts[2]] = getValue(parts[1])
            "inc" -> variables[parts[1]] = getValue(parts[1]) + 1
            "dec" -> variables[parts[1]] = getValue(parts[1]) - 1
            "jnz" -> if (getValue(parts[1]) != 0) pointer += getValue(parts[2]) - 1
            "out" -> {
                val signal = getValue(parts[1])
                if (signal != output.size % 2) return false // Must alternate 0,1,0,1...
                output.add(signal)
            }
        }
        pointer++
    }
    return output.size == 20 // Ensures correct repeating pattern
}

private fun findSmallestA(instructions: List<String>): Int {
    var a = 1
    while (!executeAssembunny(instructions, a)) {
        a++
    }
    return a
}