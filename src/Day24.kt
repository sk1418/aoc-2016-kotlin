import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import utils.*

// https://adventofcode.com/2016/day/24
fun main() {
    val today = "Day24"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun initMatrix(input: List<String>) =
        buildMap {
            input.forEachIndexed { y, line -> line.forEachIndexed { x, c -> put(x to y, c) } }
        }.let { MatrixDay24(input.first().lastIndex, input.lastIndex, it.toNotNullMap()) }

    fun part1(input: List<String>): Int {
        return initMatrix(input).solveIt()
    }

    fun part2(input: List<String>): Int {
        return initMatrix(input).solveIt(returnToStart = true)
    }

    chkTestInput(Part1, testInput, 14) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}
private typealias Point = Pair<Int, Int>

private data class PointSteps(val point: Point, val steps: Int)

private class MatrixDay24(maxX: Int, maxY: Int, override val points: NotNullMap<Pair<Int, Int>, Char>) : Matrix<Char>(maxX = maxX, maxY = maxY, points = points) {
    private val allNodes: List<Point> = points.filter { (_, c) -> c.isDigit() }.entries.toList().sortedBy { it.value }.map { it.key }
    private val start = allNodes.first()

    private val shortPathMap: NotNullMap<Set<Point>, Int> = runBlocking(Dispatchers.Default) {
        allNodes.combinations(2).map { twoNodes ->
            async { twoNodes.toSet() to bfs(twoNodes.first(), twoNodes.last()) }
        }.awaitAll().associate { (twoPoints, shortPathDef) -> twoPoints to shortPathDef }.toNotNullMap()
    }

    private fun bfs(p1: Point, p2: Point): Int {
        val unvisited = ArrayDeque<PointSteps>()
        unvisited.add(PointSteps(p1, 0))
        val visited = mutableSetOf<Point>()
        while (unvisited.isNotEmpty()) {
            val current = unvisited.removeFirst().also { visited.add(it.point) }
            if (current.point == p2) return current.steps
            unvisited.addAll(Direction.entries.map { dir -> current.point.move(dir) }
                .filter { it.validAndExist() && points[it] != '#' && it !in visited && it !in unvisited.map { it.point } }
                .map { PointSteps(it, current.steps + 1) })
        }
        return Int.MAX_VALUE
    }

    // solve TSP by bitmask DP
    private fun tsp(currentIdx: Int, visitedMask: Int, cache: MutableMap<Pair<Int, Int>, Int>, returnToStart: Boolean): Int {
        val goal = (1 shl allNodes.size) - 1 // 1111..1
        val key = currentIdx to visitedMask
        return cache.getOrPut(key) {

            // If all nodes visited, return distance back to start (for Part 2)
            if (visitedMask == goal) {
                return if (returnToStart) shortPathMap[setOf(allNodes[currentIdx], start)] else 0
            }

            var minDist = Int.MAX_VALUE
            for (nextIdx in allNodes.indices) {
                if ((visitedMask and (1 shl nextIdx)) == 0) { // If next node not visited
                    val newMask = visitedMask or (1 shl nextIdx)
                    val distance = shortPathMap[setOf(allNodes[currentIdx], allNodes[nextIdx])]
                    minDist = minOf(minDist, distance + tsp(nextIdx, newMask, cache, returnToStart))
                }
            }
            minDist
        }
    }

    fun solveIt(returnToStart: Boolean = false): Int {
        val cache = mutableMapOf<Pair<Int, Int>, Int>()
        return tsp(0, 1, cache, returnToStart)// start idx:0, mask: 000..1
    }
}