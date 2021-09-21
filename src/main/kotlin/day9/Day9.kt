package day9

import java.util.Collections.swap

fun main() {
    val distances = input()
    val locations = with(distances) {
        sequenceOf(
            map { it.first[0] },
            map { it.first[1] })
            .flatten()
            .distinct()
            .toList()
    }
    val distanceMap = distances.associate { Pair(Pair(it.first[0], it.first[1]), it.second) }

    println(distanceMap)

    val allDistances = locations.permutations()
        .mapNotNull { distance(it, distanceMap)?.run { Pair(it, this) } }


    val min = allDistances.sortedBy { it.second }.firstNotNullOf { it.second }
    val max = allDistances.sortedBy { it.second }.reversed().firstNotNullOf { it.second }

    println("min: $min, max: $max")
}

fun distance(path: List<String>, distances: Map<Pair<String, String>, Int>): Int? {
    var distance = 0
    for ((index, location) in path.withIndex()) {
        if (index < path.size - 1) {
            val otherLocation = path[index + 1]
            val inc =
                distances[Pair(location, otherLocation)] ?: distances[Pair(otherLocation, location)] ?: return null
            distance += inc
        }
    }
    return distance
}

fun <V> List<V>.permutations(): List<List<V>> {
    val result: MutableList<List<V>> = mutableListOf()

    fun generate(k: Int, list: List<V>) {
        // If only 1 element, just output the array
        if (k == 1) {
            result.add(list.toList())
            return
        }
        for (i in 0 until k) {
            generate(k - 1, list)
            swap(list, if (k % 2 == 0) i else 0, k - 1)
        }
    }

    generate(count(), toList())
    return result
}

fun input() = """AlphaCentauri to Snowdin = 66
AlphaCentauri to Tambi = 28
AlphaCentauri to Faerun = 60
AlphaCentauri to Norrath = 34
AlphaCentauri to Straylight = 34
AlphaCentauri to Tristram = 3
AlphaCentauri to Arbre = 108
Snowdin to Tambi = 22
Snowdin to Faerun = 12
Snowdin to Norrath = 91
Snowdin to Straylight = 121
Snowdin to Tristram = 111
Snowdin to Arbre = 71
Tambi to Faerun = 39
Tambi to Norrath = 113
Tambi to Straylight = 130
Tambi to Tristram = 35
Tambi to Arbre = 40
Faerun to Norrath = 63
Faerun to Straylight = 21
Faerun to Tristram = 57
Faerun to Arbre = 83
Norrath to Straylight = 9
Norrath to Tristram = 50
Norrath to Arbre = 60
Straylight to Tristram = 27
Straylight to Arbre = 81
Tristram to Arbre = 90"""
    .split("\n")
    .map { it.split(" = ") }
    .map { Pair(it[0].split(" to "), it[1].toInt()) }
