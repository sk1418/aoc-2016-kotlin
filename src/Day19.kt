import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day19"

    val input = readInput(today)
    val testInput = readTestInput(today)

    //    https://www.youtube.com/watch?v=uCsD3ZGzMgE&ab_channel=Numberphile
    fun part1(input: List<String>): Int {
        val num = input.single().toInt()
        return num.toString(2).let { it.rotate(it.length - 1).toInt(2) }
    }

    fun part2(input: List<String>): Int {
        val num = input.single().toInt()
        return solveByTwoQueues(num)
    }

    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 2) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private fun solveByTwoQueues(n: Int): Int {
    val left = ArrayDeque((1..n / 2).toList())
    val right = ArrayDeque((n / 2 + 1..n).toList())
    while (left.isNotEmpty() && right.isNotEmpty()) {
        right.removeFirst()
        right.add(left.removeFirst()) //rotate
        if ((left.size + right.size) % 2 == 0) left.add(right.removeFirst())
    }
    return if (left.isNotEmpty()) left.first() else right.first()
}