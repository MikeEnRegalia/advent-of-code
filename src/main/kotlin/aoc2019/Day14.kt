package aoc2019

import kotlin.math.ceil
import kotlin.math.min

fun main() {
    fun String.toReactant() = split(" ").let { Reactant(it[0].toLong(), it[1]) }
    fun String.toReaction() = split(Regex(" => ")).let { (left, right) ->
        Reaction(left.split(Regex(", ")).map(String::toReactant), right.toReactant())
    }

    val reactions = generateSequence(::readLine).map(String::toReaction).toSet()

    fun factor(wanted: Long, provided: Long) = ceil(wanted / provided.toDouble()).toLong()

    fun resolve(target: Reaction): Long {
        val surplus = mutableMapOf<String, Long>()

        fun takeFromSurplus(r: Reactant): Long? {
            val takeFromExtra = min(r.q, surplus.getOrDefault(r.name, 0))
            surplus.compute(r.name) { _, q -> (q ?: 0) - takeFromExtra }
            return (r.q - takeFromExtra).takeUnless { it == 0L }
        }

        fun addToSurplus(input: Reactant, needed: Long): List<Reactant> {
            val next = reactions.single { it.output.name == input.name }
            val factor = factor(needed, next.output.q)
            surplus.compute(next.output.name) { _, v -> (v ?: 0) + (next.output.q * factor - needed) }
            return next.input.map { it.copy(q = it.q * factor) }
        }

        var reaction = target
        while (true) {
            val (elementary, complex) = reaction.input.partition { it.elementary(reactions) }
            if (complex.isEmpty()) break
            reaction = elementary.plus(complex.flatMap { input ->
                takeFromSurplus(input)?.let { addToSurplus(input, it) } ?: return@flatMap listOf()
            }).run {
                distinctBy { it.name }.map { e -> Reactant(filter { it.name == e.name }.sumOf { it.q }, e.name) }
            }.let { inputs -> Reaction(inputs, reaction.output) }
        }

        return reaction.input.sumOf { input ->
            val needed = takeFromSurplus(input) ?: return@sumOf 0
            addToSurplus(input, needed).sumOf { it.q }
        }
    }

    val reactionOneFuel = reactions.single { it.output.name == "FUEL" }
    val oreSpentForOneFuel = resolve(reactionOneFuel)
    println(oreSpentForOneFuel)

    var x = generateSequence(1_000_000_000_000) { (it / 1.005).toLong() }
        .first { resolve(reactionOneFuel.times(it)) < 1_000_000_000_000 }

    while (true) {
        if (resolve(reactionOneFuel.times(x)) >= 1_000_000_000_000) {
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
