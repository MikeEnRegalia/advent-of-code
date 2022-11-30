package aoc2016

import java.lang.Integer.MAX_VALUE
import java.security.MessageDigest

private val Char.open get() = "bcdef".contains(this)
private const val MAX = 3

fun main() {
    val passcode = "veumntbg"

    data class State(val x: Int = 0, val y: Int = 0, val path: String = "") {
        val isVault = x == MAX && y == MAX
        val inBounds = x in 0..MAX && y in 0..MAX
        private val hash = passcode.plus(path).md5()

        fun neighbors() = listOfNotNull(
            copy(y = y - 1, path = path + 'U').takeIf { hash[0].open },
            copy(y = y + 1, path = path + 'D').takeIf { hash[1].open },
            copy(x = x - 1, path = path + 'L').takeIf { hash[2].open },
            copy(x = x + 1, path = path + 'R').takeIf { hash[3].open }
        ).filter { it.inBounds }
    }

    var s = State()
    val v = mutableSetOf<State>()
    val u = mutableSetOf<State>()
    val d = mutableMapOf(s to 0).withDefault { MAX_VALUE }

    var longestPath: Int? = null

    while (true) {
        if (s.isVault) {
            if (longestPath == null) println(s.path)
            longestPath = s.path.length
        } else {
            s.neighbors().filterNot { it in v }.forEach { neighbor ->
                u += neighbor
                val distance = d.getValue(s) + 1
                if (d.getValue(neighbor) > distance) d[neighbor] = distance
            }
        }
        v += s
        u -= s
        s = u.minByOrNull { d.getValue(it) } ?: break
    }
    println(longestPath)
}

private fun String.md5() = toByteArray().md5().joinToString("") { it.toUByte().toHex() }
private fun ByteArray.md5() = MessageDigest.getInstance("MD5").digest(this)
private fun UByte.toHex() = toString(16).padStart(2, '0')
