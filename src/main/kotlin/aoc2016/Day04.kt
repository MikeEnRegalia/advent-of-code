package aoc2016

fun main() {
    fun String.shift(n: Int = 1) = buildString {
        for (c in this@shift) append(
            when (c) {
                '-', ' ' -> ' '
                else -> 'a' + ((c - 'a') + n) % 26
            }
        )
    }

    data class Room(val name: String, val sectorId: Int, val checksum: String) {
        val valid by lazy {
            checksum == name.filterNot { it == '-' }
                .groupingBy { it.toString() }.eachCount().entries
                .sortedBy { it.key }.sortedByDescending { it.value }
                .take(5).joinToString("") { it.key }
        }
        val decrypted by lazy { if (valid) name.shift(sectorId % 26) else null }
    }

    val rooms = generateSequence(::readLine)
        .mapNotNull { Regex("""^(.*)-(\d+)\[(.*)]$""").find(it)?.destructured?.toList() }
        .map { Room(it[0], it[1].toInt(), it[2]) }
        .filter(Room::valid)
        .toList()

    with(rooms) {
        sumOf(Room::sectorId).also(::println)
        single { it.decrypted == "northpole object storage" }.sectorId.also(::println)
    }
}