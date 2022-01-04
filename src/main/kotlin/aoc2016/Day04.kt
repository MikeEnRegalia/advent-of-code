package aoc2016

fun main() {
    val input = generateSequence(::readLine)
        .mapNotNull { Regex("""^(.*)-(\d+)\[(.*)]$""").find(it)?.destructured?.toList() }
        .map { (room, sectorId, checksum) -> Triple(room, sectorId.toInt(), checksum) }
        .toList()

    val real = input
        .filter { (room, _, checksum) ->
            room.filterNot { it == '-' }
                .groupingBy { it.toString() }.eachCount().entries
                .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
                .take(5).joinToString("") { it.key } == checksum
        }

    println(real.sumOf { it.second })

    real.map { (room, sectorId) ->
        var newRoom = room
        repeat(sectorId % 26) {
            newRoom = newRoom.map {
                when (it) {
                    'z' -> 'a'
                    '-' -> ' '
                    ' ' -> '-'
                    else -> it + 1
                }
            }.joinToString("")
        }
        newRoom to sectorId
    }
        .filter { it.second in 392..598 }
        .sortedBy { it.first }
        .forEach(::println)
}