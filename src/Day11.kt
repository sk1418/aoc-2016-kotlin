import utils.*

// https://adventofcode.com/2016/day/1x
fun main() {
    val today = "Day11"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun initState(input: List<String>): State {
        return State(0, input.mapIndexed { idx, line -> line.substringBefore(" :").split(" ").toSet() })
    }

    fun solveIt(initialState: State): Int {
        val visited = mutableSetOf<Pair<Int, List<Set<String>>>>()
        val queue = ArrayDeque<State>()
        queue.add(initialState)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current.floors.dropLast(1).all { it.isEmpty() }) {
                return current.steps // All items on the top floor
            }
            val stateKey = current.elevator to current.floors
            if (stateKey !in visited) {
                visited.add(stateKey)
                queue.addAll(current.generateNeighbors())
            }
        }
        return -1
    }

    fun part1(input: List<String>) = solveIt( initState(input).alsoLog())

    fun part2(input: List<String>): Int {
        val state = State(
            0, listOf(
                setOf("SG", "SM", "PG", "PM", "DM", "DG", "EG", "EM"),
                setOf("TG", "RG", "RM", "CG", "CM"),
                setOf("TM"),
                emptySet(),
            )
        )
        return solveIt(state)
    }
    chkTestInput(Part1, testInput, 11) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}

private data class State(
    val elevator: Int,
    val floors: List<Set<String>>,
    val steps: Int = 0,
)

private fun State.isValidState(): Boolean {
    for (floor in floors) {
        val chips = floor.filter { it.endsWith("M") }
        val generators = floor.filter { it.endsWith("G") }
        if (generators.isNotEmpty() && chips.any { chip -> "${chip[0]}G" !in generators }) {
            return false // Unshielded chip on a floor with a generator
        }
    }
    return true
}

private fun State.generateNeighbors(): List<State> {
    val neighbors = mutableListOf<State>()
    val currentFloor = elevator
    val currentItems = floors[currentFloor]

    // Generate all possible moves (1 or 2 items)
    val possibleMoves = currentItems.flatMap { item1 ->
        listOf(setOf(item1)) + currentItems.filter { it != item1 }.map { item2 -> setOf(item1, item2) }
    }

    // Try moving up or down
    for (move in possibleMoves) {
        for (direction in listOf(-1, 1)) {
            val newFloor = currentFloor + direction
            if (newFloor in floors.indices) {
                val newFloors = floors.mapIndexed { index, floor ->
                    when (index) {
                        currentFloor -> floor - move
                        newFloor -> floor + move
                        else -> floor
                    }
                }
                val newState = State(newFloor, newFloors, steps + 1)
                if (newState.isValidState()) neighbors.add(newState)
            }
        }
    }
    return neighbors
}