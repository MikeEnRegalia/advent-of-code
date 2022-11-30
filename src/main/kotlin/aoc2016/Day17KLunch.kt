package aoc2016

import java.math.BigInteger
import kotlin.Int.Companion.MAX_VALUE
import kotlin.math.min
import java.security.MessageDigest.getInstance as messageDigest

private const val MAX = 3

fun main() {
    val passcode = "rrrbmfta"

    data class State(val x: Int = 0, val y: Int = 0, val path: String = "") {
        val isVault = x == MAX && y == MAX
        val isInMaze = x in 0..MAX && y in 0..MAX
        val doors = (passcode + path).md5().substring(0..3).map { it in "bcdef" }

        fun neighbors() = doors.mapIndexedNotNull { door, open -> neighbor(door)?.takeIf { open && it.isInMaze } }

        private fun neighbor(door: Int) = when (door) {
            0 -> copy(y = y - 1, path = path + "U")
            1 -> copy(y = y + 1, path = path + "D")
            2 -> copy(x = x - 1, path = path + "L")
            3 -> copy(x = x + 1, path = path + "R")
            else -> null
        }
    }

    var s = State()
    val v = mutableSetOf<State>()
    val u = mutableSetOf<State>()
    val d = mutableMapOf(s to 0)

    val vaultPaths = mutableSetOf<String>()

    while (true) {
        if (s.isVault) vaultPaths += s.path
        else s.neighbors().filter { it !in v }.also { u += it }.forEach { n ->
            d.compute(n) { _, old -> min(old ?: MAX_VALUE, d.getValue(s) + 1) }
        }
        v += s
        u -= s
        s = u.randomOrNull() ?: break
    }

    println(vaultPaths.minBy { it.length })
    println(vaultPaths.maxOf { it.length })
}

private fun String.md5() = BigInteger(1, messageDigest("MD5").digest(toByteArray())).toString(16).padStart(32, '0')