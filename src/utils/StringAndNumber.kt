package utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.pow

//Strings
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

fun String.toInts(sep: String = " ") = split(sep.toRegex()).map { it.toInt() }
fun String.toLongs(sep: String = " ") = split(sep.toRegex()).map { it.toLong() }

/**
 *    [aaabbbcddd] -> [[a,a,a],[b,b,b],[c],[d,d,d]]
 */
fun String.groupContinuousChars(initialChar: Char = '@'): List<List<Char>> {
    val dQ = ArrayDeque(toList())
    var cur = initialChar
    var segment = mutableListOf<Char>()
    return buildList {
        while (dQ.isNotEmpty()) {
            val nextChar = dQ.removeFirst()
            if (nextChar != cur) {
                cur = nextChar
                segment = mutableListOf()
                add(segment)
            }
            segment.add(nextChar)
        }
    }
}

infix fun Int.pow(exponent: Int): Int = toDouble().pow(exponent).toInt()
infix fun Long.pow(exponent: Long): Long = toDouble().pow(exponent.toDouble()).toLong()