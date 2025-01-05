import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day02"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toKeypadPart1(input: List<String>) = Keypad(
        input = input,
        maxX = 2,
        maxY = 2,
        points = buildMap {
            val s = """
               |1 2 3
               |4 5 6
               |7 8 9
           """.trimMargin().split('\n')
                .forEachIndexed { y, line -> line.split(" ").forEachIndexed { x, c -> put(x to y, c) } }
        }.toNotNullMap(),
    )


    fun toKeypadPart2(input: List<String>) = Keypad(
        input = input,
        maxX = 4,
        maxY = 4,
        points = buildMap {
            val s = """
       |# # 1 # #
       |# 2 3 4 #
       |5 6 7 8 9
       |# A B C #
       |# # D # #
           """.trimMargin().split('\n')
                .forEachIndexed { y, line -> line.split(" ").forEachIndexed { x, c -> if (c != "#") put(x to y, c) } }
        }.toNotNullMap(),
    )

    fun part1(input: List<String>) = toKeypadPart1(input).walk()

    fun part2(input: List<String>) = toKeypadPart2(input).walk()

    chkTestInput(Part1, testInput, "1985") { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, "5DB3") { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class Keypad(maxX: Int, maxY: Int, override val points: NotNullMap<Pair<Int, Int>, String>, val input: List<String>) : Matrix<String>(maxX, maxY, points) {
    val start = findOneByValue("5")
    fun walk(): String = buildList {
        var pos = start
        input.forEach { insLine ->
            insLine.forEach { c -> pos.move(c).also { if (it.validAndExist()) pos = it } }
            add(points[pos])
        }
    }.joinToString("")
}