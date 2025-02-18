import utils.*

// https://adventofcode.com/2016/day/22
fun main() {
    val today = "Day22"

    val input = readInput(today)
    val testInput = readTestInput(today)
    fun toNodes(input: List<String>): List<Node> {
        val regex = """node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T\s+(\d+)T""".toRegex()
        return input.mapNotNull { line ->
            regex.find(line)?.destructured?.let { (x, y, size, used, avail) ->
                Node(x.toInt(), y.toInt(), size.toInt(), used.toInt(), avail.toInt())
            }
        }
    }

    fun part1(input: List<String>)=countViablePairs(toNodes(input))

    fun part2(input: List<String>)= findStepsToMoveGoal(toNodes(input))

    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 7) { part2(it) }
    solve(Part2, input) { part2(it) }
}
private data class Node(val x: Int, val y: Int, val size: Int, val used: Int, val avail: Int)
private fun countViablePairs(nodes: List<Node>): Int {
    return nodes.sumOf { a ->
        nodes.count { b -> a != b && a.used > 0 && a.used <= b.avail }
    }
}
//part2
private data class NodeState(val pos:Pair<Int,Int>, val steps: Int)

//bfs
private fun findStepsToMoveGoal(nodes: List<Node>): Int {
    val maxX = nodes.maxOf { it.x } -1
    val emptyNode = nodes.first { it.used == 0 } // The empty node
    val occupied = nodes.filter { it.used > 100 }.map { it.x to it.y }.toSet() // Wall-like nodes
    val queue = ArrayDeque<NodeState>().also { it.add(NodeState(emptyNode.x to emptyNode.y, 0)) }

    val visited = mutableSetOf<Pair<Int, Int>>()
    while (queue.isNotEmpty()) {
        val (pos,  steps) = queue.removeFirst()
        if (pos.first == maxX  && pos.second == 0) { // Reached just left of goal
            return steps + maxX * 5 + 1 // Move goal left (5 moves per shift)
        }
        if (pos in visited ||pos in occupied) continue
        visited.add(pos)
        Direction.entries.map { pos.move(it)}
            .filter { it !in occupied && nodes.any { n -> n.x == it.first && n.y == it.second } }
            .forEach { queue.add(NodeState(it.first to it.second, steps + 1)) }
    }
    return -1
}