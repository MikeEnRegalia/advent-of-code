package aoc2016

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.Int.Companion.MAX_VALUE

private const val MAX = 3
fun main() {
    val passcode = readln()

    fun Char.isOpen() = this > 'a'

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    data class State(val passcode: String, val x: Int = 0, val y: Int = 0, val path: String = "") {
        val isVault = x == MAX && y == MAX
        fun next() = buildList {
            if (isVault) return@buildList
            val doors = md5(passcode + path).substring(0..4)
            if (doors[0].isOpen() && y > 0) add(copy(y = y - 1, path = path + "U"))
            if (doors[1].isOpen() && y < MAX) add(copy(y = y + 1, path = path + "D"))
            if (doors[2].isOpen() && x > 0) add(copy(x = x - 1, path = path + "L"))
            if (doors[3].isOpen() && x < MAX) add(copy(x = x + 1, path = path + "R"))
        }
    }

    var s = State(passcode)
    val v = mutableSetOf<State>()
    val u = mutableSetOf<State>()
    val d = mutableMapOf<State, Int>()

    var maxPathLength: Int? = null

    while (true) {
        s.next().filter { it !in v }.forEach { n ->
            u += n
            val nd = d.getValue(s) + 1
            if (nd < d.getOrDefault(n, MAX_VALUE)) d[n] = nd
        }
        v += s
        u -= s
        if (s.isVault) {
            if (maxPathLength == null) {
                println(s.path)
            }
            maxPathLength = s.path.length
        }
        s = u.minByOrNull { d.getValue(it) } ?: break
    }
    println(maxPathLength)
}

