package utils

import utils.Direction.*

fun Pair<Int, Int>.move(directionChar: Char, steps: Int = 1) = when (directionChar) {
    'U' -> first to (second - steps)
    'D' -> first to (second + steps)
    'L' -> (first - steps) to second
    'R' -> (first + steps) to second
    else-> error("Unknown direction char: $directionChar")
}

fun Pair<Int, Int>.move(direction: Direction, steps: Int = 1) = when (direction) {
    Up -> first to (second - steps)
    Down -> first to (second + steps)
    Left -> (first - steps) to second
    Right -> (first + steps) to second
}

//Matrix related:
open class Matrix<T : Any>(val maxX: Int, val maxY: Int, open val points: Map<Pair<Int, Int>, T>) {

    protected fun findOneByValue(value: T) = points.entries.first { it.value == value }.key
    protected fun findByValue(value: T) = points.filterValues { it == value }.keys

    protected fun Pair<Int, Int>.validPoint() = first in 0..maxX && second in 0..maxY
    protected fun Pair<Int, Int>.invalidPoint() = validPoint().not()

    protected fun Pair<Int, Int>.validAndExist() = validPoint() && this in points
    protected fun Pair<Int, Int>.notValidAndExist() = validAndExist().not()

    operator fun contains(pos: Pair<Int, Int>) = pos.validPoint()

    protected fun Pair<Int, Int>.safeMove(direction: Direction) = when (direction) {
        Up -> (first to ((second - 1).takeIf { it >= 0 } ?: 0))
        Down -> (first to ((second + 1).takeIf { it <= maxY } ?: maxY))
        Left -> (((first - 1).takeIf { it >= 0 } ?: 0) to second)
        Right -> (((first + 1).takeIf { it <= maxX } ?: maxX) to second)
    }

    protected fun Pair<Int, Int>.allAround() =
        listOf(
            move(Up), move(Left), move(Down), move(Right),
            move(Up).move(Left), move(Up).move(Right), move(Down).move(Left), move(Down).move(Right)
        ).filter { it.validPoint() }

    override fun toString(): String = buildString {
        append('\n')
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                append(if (x to y in points) points[x to y] else ' ')
            }
            append('\n')
        }
    }

}

enum class Direction {
    Left, Up, Right, Down;

    fun isHorizontal() = this == Left || this == Right
    fun isVertical() = this == Up || this == Down
    fun opposite() = ordinal.let { entries[(it + 2).let { if (it > 3) it - 4 else it }] }
    fun turn90() = ordinal.let { entries[if (it == 3) 0 else it + 1] }
    fun turn90Back() = ordinal.let { entries[if (it == 0) 3 else it - 1] }
}