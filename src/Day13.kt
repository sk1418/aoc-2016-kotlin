import utils.*
import java.util.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day13"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>, target: Pair<Int, Int>): Int {
        return Day13Solution(input.single().toInt()).findPath(target)
    }

    fun part2(input: List<String>): Int {
        return Day13Solution(input.single().toInt()).howManyPosCanBeReached()
    }

    chkTestInput(Part1, testInput, 11) { part1(it, 7 to 4) }
    solve(Part1, input) { part1(it, 31 to 39) }

    solve(Part2, input) { part2(it) }
}


private class Day13Solution(val fav: Int) {
    val points = mutableMapOf<Pair<Int, Int>, Char>()
    val start = 1 to 1

    private fun Pair<Int, Int>.isFree() = let { (x, y) ->
        x >= 0 && y >= 0 && '.' == points.getOrPut(x to y) {
            if (Integer.toBinaryString(x * x + 3 * x + 2 * x * y + y + y * y + fav).count { it == '1' } % 2 == 0) '.' else '#'
        }
    }

    //    private data class PointScoreWithPath(val pos: Pair<Int, Int>, val direction: Direction, val score: Int, val path: List<Pair<Pair<Int, Int>, Direction>>)
    private data class PointScore(val pos: Pair<Int, Int>, val direction: Direction, val score: Int)

    fun findPath(target: Pair<Int, Int>): Int {
        val unvisited = PriorityQueue<PointScore> { p1, p2 -> p1.score.compareTo(p2.score) }
        unvisited.addAll(Direction.entries.map { PointScore(start, it, 0) })

        val visitedPosAndDir = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
        while (unvisited.isNotEmpty()) {

            val (pos, direction, score) = unvisited.remove().also { visitedPosAndDir.add(it.pos to it.direction) }
            if (pos == target) return score
            val nextPos = pos.move(direction)
            if (nextPos.isFree()) {
                unvisited.addAll(
                    Direction.entries.map { dir -> PointScore(nextPos, dir, score + 1) }
                        .filterNot { it.pos to it.direction in visitedPosAndDir }
                )
            }
        }
        return -1
    }

    fun howManyPosCanBeReached(): Int {
        val unvisited = PriorityQueue<PointScore> { p1, p2 -> p1.score.compareTo(p2.score) }
        unvisited.addAll(Direction.entries.map { PointScore(start, it, 0) })

        val visitedPosAndDir = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
        while (unvisited.any { it.score <= 50 }) {
            val (pos, direction, score) = unvisited.remove().also { visitedPosAndDir.add(it.pos to it.direction) }
            val nextPos = pos.move(direction)
            if (nextPos.isFree()) {
                unvisited.addAll(
                    Direction.entries.filter { it != direction.opposite() }.map { dir -> PointScore(nextPos, dir, score + 1) }
                        .filterNot { it.pos to it.direction in visitedPosAndDir }
                )
            }
        }
        return visitedPosAndDir.map { it.first }.distinct().size
    }
}