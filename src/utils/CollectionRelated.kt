package utils

import java.util.*

// list
fun List<Char>.joinChars() = joinToString(separator = "") { "$it" }

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val result = (first().indices).map { mutableListOf<T>() }.toMutableList()
    forEach { list -> result.zip(list).forEach { it.first.add(it.second) } }
    return result
}

fun <T> List<T>.prepend(e: T): List<T> = buildList {
    add(e)
    addAll(this@prepend)
}

fun <T> List<T>.combinations(count: Int): List<List<T>> {
    if (count == 0) return listOf(this)
    if (count > size) return emptyList()
    val results = mutableListOf<List<T>>() // the number of all cases

    val result = MutableList(count) {first()}

    fun combinationRecursive(depth: Int = 0, index: Int = 0) {
        if (depth == count) {
            results.add(result.toList()) // toList() to make a copy
            return
        }

        for (i in index..this@combinations.lastIndex) {
            result[depth] = this@combinations[i]
            combinationRecursive(depth + 1, i + 1)
        }
    }

    combinationRecursive()
    return results
}

fun <T> List<T>.permutations(): List<List<T>> {
    fun permutationsRecursive(input: List<T>, index: Int, answers: MutableList<List<T>>) {
        if (index == input.lastIndex) answers.add(input.toList())
        for (i in index..input.lastIndex) {
            Collections.swap(input, index, i)
            permutationsRecursive(input, index + 1, answers)
            Collections.swap(input, i, index)
        }
    }

    val solutions = mutableListOf<List<T>>()
    permutationsRecursive(this, 0, solutions)
    return solutions
}

//map
class MutableNotNullMap<K, V>(private val map: MutableMap<K, V>) : MutableMap<K, V> by map {
    override operator fun get(key: K): V {
        return checkNotNull(map[key]) { "Key ($key) not found in the NeverNullMap" }
    }
}

fun <K, V> Map<K, V>.toMutableNotNullMap() = MutableNotNullMap(toMutableMap())
fun <K, V> Map<K, V>.toNotNullMap() = NotNullMap(this)
class NotNullMap<K, V>(private val map: Map<K, V>) : Map<K, V> by map {
    override operator fun get(key: K): V {
        return checkNotNull(map[key]) { "Key ($key) not found in the NeverNullMap" }
    }
}