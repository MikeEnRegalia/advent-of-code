package aoc2016

fun main() {
    data class Room(val name: String, val sectorId: Int, val checksum: String) {
        val valid by lazy {
            checksum == name.filterNot { it == '-' }
                .groupingBy { it.toString() }.eachCount().entries
                .sortedBy { it.key }.sortedByDescending { it.value }
                .take(5).joinToString("") { it.key }
        }
        val decrypted by lazy {
            if (!valid) null
            else name.map { c -> if (c in "- ") ' ' else 'a' + (c - 'a' + sectorId) % 26 }.joinToString("")
        }
    }

    generateSequence(::readLine)
        .map { Regex("""^(.*)-(\d+)\[(.*)]$""").find(it)!!.destructured }
        .map { (name, sectorId, checksum) -> Room(name, sectorId.toInt(), checksum) }
        .filter(Room::valid).toList()
        .run { sumOf(Room::sectorId) to single { it.decrypted == "northpole object storage" }.sectorId }
        .also(::println)
}