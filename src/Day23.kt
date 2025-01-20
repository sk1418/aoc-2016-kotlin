import utils.*

// https://adventofcode.com/2016/day/23
fun main() {
    val today = "Day23"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>, initialA: Int = 0): Int {
        return Day23Solution(instructions = input.toMutableList()).also { it.variables["a"] = initialA }.run().variables["a"]
    }

    fun part2(input: List<String>, initialA: Int = 12): Int {
        //calculate the factorial of initialA (12) + 80*84
       return (1..12).fold(1) { acc, i -> acc * i } + 80 * 84 //get from the instruction log
    }

    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it, initialA = 7) }

    solve(Part2, input) { part2(it) }
}

private data class Day23Solution(
    val variables: MutableNotNullMap<String, Int> = mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0).toMutableNotNullMap(),
    val instructions: MutableList<String>,
) {
    var cur = 0
    private fun exec(line: String) {
        fun numOrValue(v: String) = if (v in variables.keys) variables[v] else v.toInt()
        fun toggle(idx: Int) {
            if (idx > instructions.lastIndex) return
            val inst = instructions[idx]
            val op = inst.split(" ").first()

            instructions[idx] = when (op) {
                "inc" -> inst.replace(op, "dec")
                "dec", "tgl" -> inst.replace(op, "inc")
                "jnz" -> inst.replace(op, "cpy")
                "cpy" -> inst.replace(op, "jnz")
                else -> error("Unknown opcode: $op")
            }
        }
        when {
            line.startsWith("inc") -> line.split(" ").last().also { variables[it]++ }
            line.startsWith("dec") -> line.split(" ").last().also { variables[it]-- }
            line.startsWith("cpy") -> line.split(" ").drop(1).also { (a, b) -> variables[b] = numOrValue(a) }
            line.startsWith("jnz") -> line.split(" ").drop(1).also { (a, b) ->
                val notZero = numOrValue(a) != 0
                if (notZero) cur += (numOrValue(b) - 1)// -1 since there would be a cur++ call
            }

            line.startsWith("tgl") -> line.split(" ").last().also { toggle(variables[it] + cur) }
        }
    }

    fun run(): Day23Solution {
        while (cur <= instructions.lastIndex) {
            exec(instructions[cur])
            cur++
            instructions.alsoLog()
        }
        return this
    }
}