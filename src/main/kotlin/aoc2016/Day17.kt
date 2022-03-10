package aoc2016

import java.math.BigInteger
import java.security.MessageDigest

private const val MAX = 3
fun main() {
    val input = readln()

    fun Char.isOpen() = this > 'a'

    data class State(val x: Int, val y: Int, val passcode: String, val path: String = "") {
        val vault = x == MAX && y == MAX
        fun neighbors() = buildList {
            if (vault) return@buildList
            val doors = md5(passcode + path).substring(0..4)
            if (doors[0].isOpen() && y > 0) add(State(x, y - 1, passcode, path.plus("U")))
            if (doors[1].isOpen() && y < MAX) add(State(x, y + 1, passcode, path.plus("D")))
            if (doors[2].isOpen() && x > 0) add(State(x - 1, y, passcode, path.plus("L")))
            if (doors[3].isOpen() && x < MAX) add(State(x + 1, y, passcode, path.plus("R")))
        }
    }

    data class DijkstraEnv<S>(var curr: S) {
        val v = mutableSetOf(curr)
        val u = mutableSetOf<State>()
        val d = mutableMapOf(curr to 0)
    }

    with(DijkstraEnv(State(0, 0, input))) {
        var maxPathLength: Int? = null

        while (true) {
            curr.neighbors().filterNot(v::contains).forEach { n ->
                u += n
                val nd = d.getValue(curr) + 1
                val nd0 = d[n]
                if (nd0 == null || nd0 > nd) d[n] = nd
            }
            v += curr
            u -= curr
            if (curr.vault) {
                if (maxPathLength == null) {
                    println(curr.path)
                }
                maxPathLength = curr.path.length
            }
            curr = u.minByOrNull { d.getValue(it) } ?: break
        }
        println(maxPathLength)
    }
}

private fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}