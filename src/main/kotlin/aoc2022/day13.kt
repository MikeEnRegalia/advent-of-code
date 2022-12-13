package aoc2022

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int

fun main() {
    val data = System.`in`.reader().readText().split("\n\n")
        .map { packets -> packets.split("\n").map { packet -> Json.decodeFromString<JsonArray>(packet) } }

    fun compare(a: JsonArray, b: JsonArray, pos: Int = 0): Boolean? {
        val l = a.elementAtOrNull(pos)
        val r = b.elementAtOrNull(pos)
        return when {
            l == null && r == null -> null
            l == null || r == null -> l == null
            l is JsonPrimitive && r is JsonPrimitive -> if (l == r) compare(a, b, pos + 1) else l.int < r.int
            l is JsonArray && r is JsonArray -> compare(l, r) ?: compare(a, b, pos + 1)
            l is JsonPrimitive && r is JsonArray -> compare(JsonArray(listOf(l)), r) ?: compare(a, b, pos + 1)
            l is JsonArray && r is JsonPrimitive -> compare(l, JsonArray(listOf(r))) ?: compare(a, b, pos + 1)
            else -> throw IllegalStateException("$l $r")
        }
    }

    data.mapIndexedNotNull { i, it -> if (compare(it[0], it[1])!!) i + 1 else null }.sum().also(::println)

    val divs = listOf(2, 6).map { JsonArray(listOf(JsonPrimitive(it))) }

    data.asSequence().flatten().plus(divs).sortedWith { a, b -> if (compare(a, b) == true) -1 else 1 }
        .mapIndexedNotNull { i, p -> if (p in divs) i + 1 else null }.reduce(Int::times).also(::println)
}

