package aoc2021b

fun main() {
    fun String.parseBits() = map { it.toString().toInt(16) }.joinToString("") { it.toString(2).padStart(4, '0') }
    println(readln().parseBits().parsePacket())
}

data class Result(val version: Int = 0, val length: Int = 0, val value: Long = 0) {
    fun plusVersion(v: Int) = copy(version = version + v)
    fun plusLength(l: Int) = copy(length = length + l)
}

fun String.parsePacket() = when (val type = drop(3).take(3).toInt(2)) {
    4 -> drop(6).parseLiteralContent()
    else -> drop(6).parseOperatorContent(type)
}.plusVersion(take(3).toInt(2)).plusLength(6)

fun String.parsePackets() = buildList {
    var content = this@parsePackets
    while (content.isNotEmpty()) {
        val packet = content.parsePacket()
        content = content.drop(packet.length)
        add(packet)
    }
}

fun String.parseLiteralContent(): Result {
    var chunks = 1
    while (this[(chunks - 1) * 5] != '0') chunks++
    val n = take(chunks * 5).chunked(5).joinToString("") { it.drop(1) }.toLong(2)
    return Result(length = chunks * 5, value = n)
}

fun String.parseOperatorContent(type: Int): Result {
    if (this[0] == '0') {
        val length = drop(1).take(15).toInt(2)
        val results = drop(16).take(length).parsePackets()
        return Result(results.sumOf { it.version }, 16 + length, results.map { it.value }.compute(type))
    }

    var content = drop(12)
    var result = Result(length = 12)
    val values = mutableListOf<Long>()
    repeat(drop(1).take(11).toInt(2)) {
        val r = content.parsePacket()
        values.add(r.value)
        result = result.plusVersion(r.version).plusLength(r.length)
        content = drop(result.length)
    }
    return result.copy(value = values.compute(type))
}

fun List<Long>.compute(type: Int) = when (type) {
    0 -> sum()
    1 -> reduce(Long::times)
    2 -> minOf { it }
    3 -> maxOf { it }
    5 -> if (get(0) > get(1)) 1 else 0
    6 -> if (get(0) < get(1)) 1 else 0
    7 -> if (get(0) == get(1)) 1 else 0
    else -> throw IllegalArgumentException(type.toString())
}