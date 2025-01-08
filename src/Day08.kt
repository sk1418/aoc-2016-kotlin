import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day08"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun initMatrix(input: List<String>) =
        buildMap {
            (0..49).forEach { x -> (0..5).forEach { y -> put(x to y, ' ') } }
        }.toMutableNotNullMap().let { MatrixDay08(49, 5, it, input) }

    fun part1(input: List<String>): Int =
        initMatrix(input).performActions().countLight()

    fun part2(input: List<String>) = initMatrix(input).performActions()

    chkTestInput(Part1, testInput, 6) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}

private class MatrixDay08(maxX: Int, maxY: Int, override val points: MutableNotNullMap<Pair<Int, Int>, Char>, val actions: List<String>) : Matrix<Char>(maxX = maxX, maxY = maxY, points = points) {
    private val rect = { w: Int, t: Int -> (0..<w).forEach { x -> (0..<t).forEach { y -> points[x to y] = '█' } } }
    private val rotateCol = { x: Int, steps: Int ->
        val r = (0..maxY).map { y -> points[x to y] } rotate steps
        (0..maxY).forEach { y -> points[x to y] = r[y] }
    }

    private val rotateRow = { y: Int, steps: Int ->
        val r = (0..maxX).map { x -> points[x to y] } rotate steps
        (0..maxX).forEach { x -> points[x to y] = r[x] }
    }


    fun countLight() = points.values.count { it != ' ' }
    fun performActions() = apply {
        actions.forEach { line ->
            when {
                "rect" in line -> line.split(" ", "x").let { rect(it[1].toInt(), it[2].toInt()) }
                "rotate col" in line -> line.split(" ", "=").let { rotateCol(it[3].toInt(), it[5].toInt()) }
                "rotate row" in line -> line.split(" ", "=").let { rotateRow(it[3].toInt(), it[5].toInt()) }
                else -> error("Unexpected line")
            }
        }
    }
}