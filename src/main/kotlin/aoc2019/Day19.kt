package aoc2019

import kotlin.math.min

fun main() {
    fun String.toReactant() = split(" ").let { Reactant(it[0].toLong(), it[1]) }

    val reactions = generateSequence { readLine() }.map { it.split(Regex(" => ")) }.map { (left, right) ->
        Reaction(left.split(Regex(", ")).map { it.toReactant() }, right.toReactant())
    }.toSet()

    fun factor(wanted: Long, provided: Long) = (wanted / provided) + if (wanted % provided == 0L) 0 else 1

    fun Set<Reaction>.resolve(reaction: Reaction): Long {
        val extraProduced = mutableMapOf<String, Long>()
        fun takeFromExtra(r: Reactant): Long? {
            val takeFromExtra = min(r.q, extraProduced.getOrDefault(r.name, 0))
            extraProduced.compute(r.name) { _, q -> (q ?: 0) - takeFromExtra }
            return (r.q - takeFromExtra).takeUnless { it == 0L }
        }

        fun Iterable<Reaction>.bar(input: Reactant, needed: Long): Pair<Long, Reaction> {
            val next = single { it.output.name == input.name }
            val factor = factor(needed, next.output.q)
            val extra = next.output.q * factor - needed
            if (extra > 0) {
                extraProduced.compute(next.output.name) { _, v -> (v ?: 0) + extra }
            }
            return factor to next
        }

        var r = reaction
        while (r.input.any { !it.elementary(this) }) {
            r = r.input.flatMap { input ->
                if (input.elementary(this)) listOf(input) else {
                    val needed = takeFromExtra(input) ?: return@flatMap listOf()
                    val (factor, next) = bar(input, needed)
                    next.input.map { it.copy(q = it.q * factor) }
                }
            }.run {
                distinctBy { it.name }.map { e -> Reactant(filter { it.name == e.name }.sumOf { it.q }, e.name) }
            }.let { inputs -> Reaction(inputs, r.output) }
        }

        return r.input.distinctBy { it.name }.sumOf { input ->
            r.input.filter { it.name == input.name }.sumOf { it.q }.let { neededTotalQ ->
                val sumInput = Reactant(neededTotalQ, input.name)
                val needed = takeFromExtra(sumInput) ?: return@let 0
                val (factor, next) = bar(sumInput, needed)
                next.input.single().q * factor
            }
        }
    }

    val reactionOneFuel = reactions.single { it.output.name == "FUEL" }
    val oreSpentForOneFuel = reactions.resolve(reactionOneFuel)
    println(oreSpentForOneFuel)

    var x = generateSequence(1_000_000_000_000) { (it / 1.005).toLong() }.first {
        reactions.resolve(reactionOneFuel.times(it)) < 1_000_000_000_000
    }

    while (true) {
        val oreSpent = reactions.resolve(reactionOneFuel.times(x))
        println("$oreSpent ORE for $x FUEL")
        if (oreSpent >= 1_000_000_000_000) {
            println(x - 1)
            break
        }
        x++
    }
}

data class Reaction(val input: List<Reactant>, val output: Reactant) {
    val elementary = input.all { it.name == "ORE" }
    override fun toString() = "${input.joinToString()} => $output"
    fun times(x: Long) = Reaction(input.map { it.copy(q = it.q * x) }, output.copy(q = output.q * x))
}

data class Reactant(val q: Long, val name: String) {
    fun elementary(reactions: Set<Reaction>) = reactions.single { it.output.name == name }.elementary
    override fun toString() = "$q $name"
}
