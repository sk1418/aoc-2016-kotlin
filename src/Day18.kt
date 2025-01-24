import utils.*

// https://adventofcode.com/2016/day/18
fun main() {
    val today = "Day18"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>, rows: Int) = countSafeCells(input.single(), rows)

    chkTestInput(Part1, testInput, 38) { part1(it, 10) }
    solve(Part1, input) { part1(it, 40) }

    solve(Part2, input) { part1(it, 400000) }
}

private fun countSafeCells(line: String, rows: Int): Int {
    //safe:0, trap:1
    var row = line.map { if (it == '.') 0 else 1 }.toMutableList()
    var count = 0
    repeat(rows) {
        count += row.count { it == 0 }
        row.addFirst(0)
        row.addLast(0)
        row = row.windowed(3).map { (l, _, r) -> determineCell(l, r) }.toMutableList()
    }
    return count
}

private fun determineCell(l: Int, r: Int) = if (l != r) 1 else 0