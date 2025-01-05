import utils.*
import utils.Direction.Up
import kotlin.math.absoluteValue

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day01"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testPart2Input = readTestInput("$today-part2")

    fun parseInput(input: List<String>) = input.single().split(", ").map {
        when (it[0]) {
            'R' -> { d: Direction -> d.turn90() }
            'L' -> { d: Direction -> d.turn90Back() }
            else -> error("Should not happen")
        } to it.drop(1).toInt()
    }

    fun part1(input: List<String>) = parseInput(input).fold(initial = (0 to 0) to Up) { (pos, dir), (turn, steps) ->
        turn(dir).let { d -> pos.move(d, steps) to d }
    }.let { (pos, _) -> pos.first.absoluteValue + pos.second.absoluteValue }

    fun part2(input: List<String>): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        parseInput(input).fold(initial = (0 to 0) to Up) { (pos, dir), (turn, steps) ->
            turn(dir).let { d ->
                var p = pos
                repeat(steps) {
                    p = p.move(d)
                    if (p in visited)
                        return p.first.absoluteValue + p.second.absoluteValue
                    else
                        visited.add(p)
                }
                p to d
            }
        }
        return -1
    }

    chkTestInput(Part1, testInput, 12) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testPart2Input, 4) { part2(it) }
    solve(Part2, input) { part2(it) }
}