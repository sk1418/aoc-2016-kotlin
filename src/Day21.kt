import utils.*

// https://adventofcode.com/2016/day/21
fun main() {
    val today = "Day21"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>, initialCode: String) = solveDay21(initialCode, input)
    fun part2(input: List<String>, initialCode: String) = solveDay21(initialCode, input, true)


    chkTestInput(Part1, testInput, "decab") { part1(it, "abcde") }
    solve(Part1, input) { part1(it, "abcdefgh") }

    solve(Part2, input) { part2(it, "fbgdceah") }
}

private fun solveDay21(initCode: String, operations: List<String>, rev: Boolean = false): String {
    var str = initCode
    val re1D = """(\d)""".toRegex()
    val re2D = """(\d).*(\d)""".toRegex()
    val re1L = """letter (.)""".toRegex()
    val re2L = """letter (.).* letter (.)""".toRegex()
    val findOneNum = { s: String -> re1D.find(s)!!.destructured.let { (a) -> a.toInt() } }
    val findTwoNums = { s: String -> re2D.find(s)!!.destructured.let { (a, b) -> a.toInt() to b.toInt() } }
    val findTwoLetters = { s: String -> re2L.find(s)!!.destructured.let { (a, b) -> a to b } }
    val findOneLetter = { s: String -> re1L.find(s)!!.destructured.let { (a) -> a } }
    (if (rev) operations.reversed() else operations).forEach { op ->
        str = when {
            op.startsWith("swap position") -> findTwoNums(op).let { (i, j) -> str.swapChar(i, j) }
            op.startsWith("swap letter") -> findTwoLetters(op).let { (a, b) -> str.swapChar(str.indexOf(a), str.indexOf(b)) }
            op.startsWith("rotate left") -> findOneNum(op).let { if (rev) str.rotateRight(it) else str.rotateLeft(it) }
            op.startsWith("rotate right") -> findOneNum(op).let { if (rev) str.rotateLeft(it) else str.rotateRight(it) }
            op.startsWith("reverse") -> findTwoNums(op).let { (i, j) -> str.reverseBetween(i, j) }
            op.startsWith("move") -> {
                val (i, j) = findTwoNums(op)
                str.toMutableList().let { arr ->
                    if (rev) arr.add(i, arr.removeAt(j)) else arr.add(j, arr.removeAt(i))
                    arr.joinChars()
                }
            }

            op.startsWith("rotate based on") -> findOneLetter(op).let { l ->
                val i = str.indexOf(l)
                if (rev) {
                    str.indices.forEach { i ->
                        val test = str.rotateLeft(i)
                        val testIdx = test.indexOf(l)
                        val actual = 1 + testIdx + if (testIdx >= 4) 1 else 0
                        if (actual % str.length == i) return@let test
                    }
                    error("won't happen")
                } else {
                    str.rotateRight(1 + i + if (i >= 4) 1 else 0)
                }
            }

            else -> error("Should not happen")
        }
    }
    return str
}