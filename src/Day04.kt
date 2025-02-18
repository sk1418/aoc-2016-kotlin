import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day04"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day04(input).sumRealRoomIds()

    fun part2(input: List<String>) = Day04(input).findNorthPoleRoom()

    chkTestInput(Part1, testInput, 1514) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}

private val re = """^([a-z-]+)-(\d+)\[([a-z]+)\]$""".toRegex()

private class Day04(val input: List<String>) {
    private fun realRoomSectorId(line: String): Int {
        val (letters, sectorId, chksum) = re.find(line)?.destructured!!
        val result = buildList {
            letters.replace("-", "")
                .groupBy { it }.mapValues { it.value.size }.entries
                .groupBy({ it.value }, { it.key }).entries.sortedByDescending { it.key }
                .forEach { (_, charList) ->
                    addAll(charList.sorted())
                    if (size >= 5) return@buildList
                }
        }.take(5).joinToString("")
        return if (result == chksum) sectorId.toInt() else 0
    }

    fun sumRealRoomIds() = input.sumOf { realRoomSectorId(it.trim()) }

    fun findNorthPoleRoom(): String {
        // decryptName("qzmt-zixmtkozy-ivhz-343[xxx]").alsoLog()  <-- test
        return input.asSequence().filter { realRoomSectorId(it.trim()) > 0 }
            .map { decryptName(it) }
            .first { (name, _) -> "north" in name }.second
    }

    private fun decryptName(line: String): Pair<String, String> {
        val (letters, sectorId, chksum) = re.find(line)!!.groupValues.drop(1)
        return letters.split("-").joinToString(" ") { segment ->
            segment.map { it.rotateLowerLetter(sectorId.toInt()) }.joinToString("")
        } to sectorId
    }
}