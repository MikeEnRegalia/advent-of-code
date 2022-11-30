package aoc2016

import java.math.BigInteger
import java.security.MessageDigest

private const val MAX = 3

val Char.isOpen get() = this in "bcdef"

fun main() {
    val passcode = "rrrbmfta"

    data class State(val x: Int = 0, val y: Int = 0, val path: String = "") {
        val isVault = x == MAX && y == MAX
        fun neighbors() = md5(passcode + path).let { hash ->
                buildList {
                    if (hash[0].isOpen && y > 0) add(copy(y = y - 1, path = path + "U"))
                    if (hash[1].isOpen && y < MAX) add(copy(y = y + 1, path = path + "D"))
                    if (hash[2].isOpen && x > 0) add(copy(x = x - 1, path = path + "L"))
                    if (hash[3].isOpen && x < MAX) add(copy(x = x + 1, path = path + "R"))
                }
            }
    }

    var state = State()
    val visited = mutableSetOf<State>()
    val unvisited = mutableSetOf<State>()
    val distances = mutableMapOf(state to 0)

    val vaultPaths = mutableSetOf<String>()

    while (true) {
        if (state.isVault) {
            vaultPaths += state.path
        } else {
            state.neighbors().filter { it !in visited }.forEach { neighbor ->
                unvisited += neighbor
                val distance = distances.getValue(state) + 1
                if (distances.getOrDefault(neighbor, Integer.MAX_VALUE) > distance)
                    distances[neighbor] = distance
            }
        }

        visited += state
        unvisited -= state

        state = unvisited.randomOrNull() ?: break
    }
    println(vaultPaths.minBy { it.length })
    println(vaultPaths.maxOf { it.length })
}

private fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}