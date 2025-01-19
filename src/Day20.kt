import utils.*

// https://adventofcode.com/2016/day/20
fun main() {
    val today = "Day20"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toSortedPairs(input: List<String>) = input.map { it.substringBefore("-").toLong() to it.substringAfter("-").toLong() }.sortedBy { it.first }
    fun part1(input: List<String>) = lowestAllowedIps(toSortedPairs(input))
    fun part2(input: List<String>, upperBound: Long) = countAllowedIps(toSortedPairs(input), upperBound)


    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 2) { part2(it, 9) }
    solve(Part2, input) { part2(it, UInt.MAX_VALUE.toLong()) }
}

private fun lowestAllowedIps(sortedPairs: List<Pair<Long, Long>>): Long {
    var ip = 0L
    var idx = 0
    while (ip <= UInt.MAX_VALUE.toLong() && idx in sortedPairs.indices) {
        if (ip >= sortedPairs[idx].first) {
            if (ip <= sortedPairs[idx].second) ip = sortedPairs[idx].second + 1
            idx++
        } else return ip
    }
    return -1
}

private fun countAllowedIps(sortedPairs: List<Pair<Long, Long>>, upperBound: Long): Long {
    var ip = 0L
    var idx = 0
    var result = 0L
    while (ip <= upperBound && idx in sortedPairs.indices) {
        if (ip >= sortedPairs[idx].first) {
            if (ip <= sortedPairs[idx].second) ip = sortedPairs[idx].second + 1
            idx++
        } else {
            result++
            ip++
        }
    }
    return result + upperBound - ip + 1
}