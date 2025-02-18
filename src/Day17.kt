import utils.*
import utils.Direction.*

// https://adventofcode.com/2016/day/17
fun main() {
    val today = "Day17"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = MatrixDay17(input.single()).bfs()

    fun part2(input: List<String>) = MatrixDay17(input.single()).dfs()

    chkTestInput(Part1, testInput, "DRURDRUDDLLDLUURRDULRLDUUDDDRR") { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 830) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class MatrixDay17(
    var passcode: String,
    points: NotNullMap<Pair<Int, Int>, Int> = buildMap {
        (0..3).forEach { x -> (0..3).forEach { y -> put(x to y, 0) } }
    }.toNotNullMap(),
) : Matrix<Int>(3, 3, points) {

    private val hashCache = mutableMapOf<String, String>()
    private fun String.top4FromMd5() = hashCache.getOrPut(this) { this.md5().take(4) }

    data class Pos(val xy: Pair<Int, Int>, val code: String)


    private fun openDoors(code: String): List<Direction> {
        val opens = code.top4FromMd5().map { it in 'b'..'f' }
        return listOf(Up, Down, Left, Right).filterIndexed { idx, _ -> opens[idx] }
    }

    private val pQueue = ArrayDeque<Pos>().also { it.add(Pos(0 to 0, passcode)) }
    fun bfs(): String {
        while (pQueue.isNotEmpty()) {
            val curPos = pQueue.removeFirst()
            if (curPos.xy == 3 to 3) return curPos.code.drop(passcode.length)
            pQueue.addAll(with(curPos) { openDoors(code).map { Pos(xy.move(it), code + it) }.filter { it.xy.validAndExist() } })
        }
        error("No solution")
    }

    private val stack = ArrayDeque(listOf(Pos(0 to 0, passcode)))
    fun dfs(): Int {
        var longest = 0
        while (stack.isNotEmpty()) {
            val curPos = stack.removeLast()
            if (curPos.xy == 3 to 3) {
                longest = maxOf(curPos.code.drop(passcode.length).length, longest)
                continue
            }
            stack.addAll(with(curPos) { openDoors(code).map { Pos(xy.move(it), code + it) }.filter { it.xy.validAndExist() } })
        }
        return longest
    }

}