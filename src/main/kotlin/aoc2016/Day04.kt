package aoc2016

fun main() {
    data class Room(val name: String, val sectorId: Int)

    fun String.isValid(checksum: String) = checksum == filterNot { it == '-' }
        .groupingBy { it.toString() }.eachCount().entries
        .sortedBy { it.key }.sortedByDescending { it.value }
        .take(5).joinToString("") { it.key }

    fun String.decrypt(sId: Int) = map { c -> if (c in "- ") ' ' else 'a' + (c - 'a' + sId) % 26 }.joinToString("")

    generateSequence(::readLine)
        .map { Regex("""^(.*)-(\d+)\[(.*)]$""").find(it)!!.destructured }.mapNotNull { (name, sectorId, checksum) ->
            if (!name.isValid(checksum)) null else Room(name.decrypt(sectorId.toInt()), sectorId.toInt())
        }.toList()
        .run { sumOf(Room::sectorId) to single { it.name == "northpole object storage" }.sectorId }.also(::println)
}