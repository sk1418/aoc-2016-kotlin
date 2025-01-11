import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day10"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val twoNumRe = """(\d+).*(bot \d+)""".toRegex()
    val ruleRe = """(bot \d+).* (\w+ \d+).* (\w+ \d+)""".toRegex()
    fun parseInput(input: List<String>) =
        input.partition { it.startsWith("value") }.let { (initValues, compareRules) ->
            val robots: MutableMap<String, MutableList<Int>> = mutableMapOf()
            initValues.map { str ->
                val (v, robotId) = twoNumRe.find(str)!!.destructured
                robotId to v
            }.groupByTo(robots, { it.first }, { it.second.toInt() })
            val rules = compareRules.associate { str ->
                val (bot, toLow, toHigh) = ruleRe.find(str)!!.destructured
                bot to Rule(bot, toLow, toHigh)
            }
            robots.toMutableNotNullMap() to rules.toNotNullMap()
        }

    fun part1(input: List<String>, targetInts: Set<Int>): String {
        val (botMap, ruleMap) = parseInput(input)
        return Day10Solution(botMap, ruleMap).whoCompareInts(targetInts)
    }

    fun part2(input: List<String>): Int {
        val (botMap, ruleMap) = parseInput(input)
        return Day10Solution(botMap, ruleMap).productOfOutput012()
    }

    chkTestInput(Part1, testInput, "bot 2") { part1(it, setOf(2, 5)) }
    solve(Part1, input) { part1(it, setOf(17, 61)) }

    chkTestInput(Part2, testInput, 30) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Rule(val bot: String, val lowTo: String, val highTo: String)
private class Day10Solution(
    val bots: MutableNotNullMap<String, MutableList<Int>>,
    val botRules: NotNullMap<String, Rule>,
    val outputs: MutableNotNullMap<String, MutableList<Int>> = mapOf<String, MutableList<Int>>().toMutableNotNullMap(),
) {

    private fun MutableNotNullMap<String, MutableList<Int>>.addAnInt(key: String, n: Int) {
        if (key in this) {
            this[key].add(n)
        } else {
            this[key] = mutableListOf(n)
        }
    }

    private fun go(bot: String) {
        val rule = botRules[bot]
        val ints = bots[bot]
        buildList {
            if ("bot" in rule.lowTo) bots.addAnInt(rule.lowTo, ints.min()).also { add(rule.lowTo) } else outputs.addAnInt(rule.lowTo, ints.min())
            if ("bot" in rule.highTo) bots.addAnInt(rule.highTo, ints.max()).also { add(rule.highTo) } else outputs.addAnInt(rule.highTo, ints.max())
            bots[bot].clear()
        }.filter { bots[it].size > 1 }.forEach { go(it) }
    }

    private fun goWithTargetInts(bot: String, targetInts: Set<Int>): String? {
        val rule = botRules[bot]
        val ints = bots[bot]
        if (ints.toSet() == targetInts) return bot

        return buildList {
            if ("bot" in rule.lowTo) bots.addAnInt(rule.lowTo, ints.min()).also { add(rule.lowTo) } else outputs.addAnInt(rule.lowTo, ints.min())
            if ("bot" in rule.highTo) bots.addAnInt(rule.highTo, ints.max()).also { add(rule.highTo) } else outputs.addAnInt(rule.highTo, ints.max())
            bots[bot].clear()
        }.asSequence().filter { bots[it].size > 1 }.map {
            goWithTargetInts(it, targetInts)
        }.firstOrNull { it != null }
    }

    fun whoCompareInts(targetInts: Set<Int>) = bots.keys.first { bots[it].size == 2 }.let { goWithTargetInts(it, targetInts)!! }

    fun productOfOutput012(): Int {
        bots.keys.first { bots[it].size == 2 }.also { go(it) }
        return "012".map { outputs["output $it"].first() }.reduce { a, b -> a * b }
    }
}