import utils.*


// https://adventofcode.com/2016/day/15
fun main() {
    val today = "Day15"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val diskRe = """has (\d+).*(\d)[.]$""".toRegex()
    fun parseInput(input: List<String>): List<Disk> =
        input.mapIndexed { idx, line -> diskRe.find(line)!!.destructured.let { (x, y) -> Disk(x.toInt(), y.toInt(), idx + 1) } }

    fun part1(input: List<String>): Int {
        val disks = parseInput(input)
        var time = 0

        while (true) {
            if (disks.all { (it.initialPosition + time + it.delay) % it.positions == 0 }) {
                return time
            }
            time++
        }
    }

    fun part2(input: List<String>): Int {
        val disks = parseInput(input).let { discs -> discs + Disk(11, 0, discs.size+1)}
        var time = 0

        while (true) {
            if (disks.all { (it.initialPosition + time + it.delay) % it.positions == 0 }) {
                return time
            }
            time++
        }
    }

    chkTestInput(Part1, testInput, 5) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}

private data class Disk(val positions: Int, val initialPosition: Int, val delay: Int)