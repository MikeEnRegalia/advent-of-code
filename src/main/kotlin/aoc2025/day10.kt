package aoc2025

fun main() {
    val machines = generateSequence(::readLine).toList().map { line ->
        Triple(
            line.split(" ").first().drop(1).dropLast(1).map { it == '#' },
            line.split(" ").filter { it.startsWith("(") }.map { it.drop(1).dropLast(1).split(",").map(String::toInt) },
            line.split(" ").filter { it.startsWith("{") }.map { it.drop(1).dropLast(1).split(",").map(String::toInt) }
                .first(),
        )
    }

    fun solve(target: List<Boolean>, buttons: List<List<Int>>): Int {

        fun List<Boolean>.evolve(): List<List<Boolean>> = buttons.map { b ->
            buildList {
                for (i in this@evolve.indices) {
                    val s = this@evolve[i]
                    add(if (i in b) !s else s)
                }
            }
        }

        val initialState = List(target.size) { false }

        val V = mutableSetOf(initialState)
        val D = mutableMapOf(initialState to 0)
        val N = mutableSetOf<List<Boolean>>()

        var curr = initialState

        while (true) {
            val next = curr.evolve().filter { it !in V }.onEach { n ->
                val d = D.getValue(curr) + 1
                D.compute(n) { _, old -> if (old == null || old > d) d else old }
            }
            N.addAll(next)
            curr = N.minByOrNull { D.getValue(it) } ?: throw IllegalStateException()
            N.remove(curr)
            V += curr
            if (curr == target) {
                return D.getValue(curr)
            }
        }
    }

    println(machines.sumOf { solve(it.first, it.second) })

    fun solve2(target: List<Int>, buttons: List<List<Int>>): Int {

        fun List<Int>.evolve(): List<Pair<Int, List<Int>>> = buttons.map { b ->
            val n = 1
            n to buildList {
                for (i in this@evolve.indices) {
                    val s = this@evolve[i]
                    add(if (i in b) s + n else s)
                }
            }
        }

        fun List<Int>.factor(): Int? = when {
            indices.all { i -> this[i] > 0 && target[i] % this[i] == 0 } -> indices.map { i -> target[i] / this[i] }
                .distinct()
                .takeIf { it.size == 1 }?.first()

            else -> null
        }

        val initialState = List(target.size) { 0 }

        val V = mutableSetOf(initialState)
        val D = mutableMapOf(initialState to 0)
        val N = mutableSetOf<List<Int>>()

        var curr = initialState

        while (true) {
            val next =
                curr.evolve()
                    .filter { it.second !in V && it.second.indices.none { xi -> it.second[xi] > target[xi] } }
                    .onEach { (dx, n) ->
                        val d = D.getValue(curr) + dx
                        D.compute(n) { _, old -> if (old == null || old > d) d else old }
                    }
            N.addAll(next.map { it.second })
            curr = N.minByOrNull { D.getValue(it) } ?: throw IllegalStateException()
            N.remove(curr)
            V += curr
            //println(curr)
            val factor = curr.factor()
            if (factor != null) {
                return D.getValue(curr) * factor
            }
        }
    }

    println(machines.sumOf { solve2(it.third, it.second) })
}
