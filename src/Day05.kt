import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day05"

    val input = readInput(today)
    val testInput = readTestInput(today)
    fun part1(input: List<String>): String {
        val re = """^00000[0-9a-f].*$""".toRegex()
        val s = input.single()
        var i = -1
        return buildList {
            while (true) {
                val md5Str = "$s${i++}".md5()
                if (re in md5Str) {
                    add(md5Str[5])
                    if (size == 8) break
                }
            }
        }.joinChars()
    }

    fun part2(input: List<String>): String {
        val re = """^00000[0-7][0-9a-f].*$""".toRegex()
        val s = input.single()
        var i = -1
        val result = MutableList(8) { 'x' }
        while (true) {
            val md5Str = "$s${i++}".md5()
            if (re in md5Str) {
                if (result[md5Str[5].digitToInt()] == 'x') {
                    result[md5Str[5].digitToInt()] = md5Str[6]
                }
                if ('x' !in result) break
            }
        }
        return result.joinChars()
    }

    chkTestInput(Part1, testInput, "18f47a30") { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, "05ace8e3") { part2(it) }
    solve(Part2, input) { part2(it) }
}