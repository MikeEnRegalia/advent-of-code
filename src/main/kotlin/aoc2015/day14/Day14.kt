package aoc2015.day14

import kotlin.math.min

fun main() {
    val deer = input()

    deer
        .map { it.travelled(2503) }
        .maxOrNull()
        .also { println("max distance: $it") }

    (1..2503)
        .fold(mutableMapOf<String, Int>()) { acc, time ->
            val race = deer.associate { it.name to it.travelled(time) }
            val maxDistance = race.values.maxOrNull()
            race
                .filterValues { it == maxDistance }.keys
                .forEach { acc.inc(it) }
            acc
        }
        .maxByOrNull { it.value }
        .also { println("max points: ${it?.value}") }
}

fun MutableMap<String, Int>.inc(key: String) {
    compute(key) { _, v -> v?.plus(1) ?: 1 }
}

data class Reindeer(val name: String, val speed: Int, val burst: Int, val rest: Int) {
    private val cycleDuration = burst + rest
    private fun dist(duration: Int) = speed * min(duration, this.burst)

    fun travelled(time: Int) = time / cycleDuration * dist(burst) + dist(time % cycleDuration)
}


fun String.toReindeer() =
    split(" ").mapNotNull { it.toIntOrNull() }.let {
        Reindeer(substring(0, indexOf(" ")), it[0], it[1], it[2])
    }


fun input() = """Dancer can fly 27 km/s for 5 seconds, but then must rest for 132 seconds.
Cupid can fly 22 km/s for 2 seconds, but then must rest for 41 seconds.
Rudolph can fly 11 km/s for 5 seconds, but then must rest for 48 seconds.
Donner can fly 28 km/s for 5 seconds, but then must rest for 134 seconds.
Dasher can fly 4 km/s for 16 seconds, but then must rest for 55 seconds.
Blitzen can fly 14 km/s for 3 seconds, but then must rest for 38 seconds.
Prancer can fly 3 km/s for 21 seconds, but then must rest for 40 seconds.
Comet can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.
Vixen can fly 18 km/s for 5 seconds, but then must rest for 84 seconds."""
    .split("\n")
    .map { it.toReindeer() }