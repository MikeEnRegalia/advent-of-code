package aoc2016

fun main() {
    fun String.shift(n: Int = 1) = map { c -> if ("- ".contains(c)) ' ' else 'a' + (c - 'a' + n) % 26 }

    data class Room(val name: String, val sectorId: Int, val checksum: String) {
        val valid by lazy {
            checksum == name.filterNot { it == '-' }
                .groupingBy { it.toString() }.eachCount().entries
                .sortedBy { it.key }.sortedByDescending { it.value }
                .take(5).joinToString("") { it.key }
        }
        val decrypted by lazy { name.takeIf { valid }?.shift(sectorId % 26)?.joinToString("") }
    }

    val rooms = generateSequence(::readLine)
        .map { Regex("""^(.*)-(\d+)\[(.*)]$""").find(it)!!.destructured }
        .map { (name, sectorId, checksum) -> Room(name, sectorId.toInt(), checksum) }

    with(rooms.filter(Room::valid).toList()) {
        sumOf(Room::sectorId).also(::println)
        single { it.decrypted == "northpole object storage" }.sectorId.also(::println)
    }
}