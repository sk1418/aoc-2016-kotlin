package utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.pow

//Char
fun Char.rotateLowerLetter(n: Int): Char = (this + n % 26).let { tmp ->
    if (tmp > 'z') 'a' + (tmp - 'z') - 1 else tmp
}

//Strings
fun String.md5(pad: Int = 32): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).let { if (pad > 0) it.padStart(pad, '0') else it }
fun String.rotate(steps: Int): String {
    val times = steps % length
    return this.repeat(2).drop(length - times).take(length)
}
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