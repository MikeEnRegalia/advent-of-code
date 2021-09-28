package y2018.day03

fun main() {
    val claims = generateSequence(::readLine).toList()
        .map { row ->
            row.split(" ").let {
                val coordinates = it[2].substringBefore(':').split(",")
                val size = it[3].split("x")
                Claim(
                    it[0].substring(1).toInt(),
                    coordinates[0].toInt(),
                    coordinates[1].toInt(),
                    size[0].toInt(),
                    size[1].toInt()
                )
            }
        }

    val allOverlapping = mutableSetOf<Claim>()
    var sum = 0
    for (x in 1..1000) {
        for (y in 1..1000) {
            val overlapping = claims.filter { it.contains(x, y) }
            if (overlapping.count() > 1) {
                ++sum
                allOverlapping.addAll(overlapping)
            }
        }
    }
    println(sum)

    claims.filterNot { allOverlapping.contains(it) }.forEach { println(it.number) }
}

data class Claim(val number: Int, val x: Int, val y: Int, val width: Int, val height: Int) {
    fun contains(x: Int, y: Int) = x in this.x until (this.x + width) && y in this.y until (this.y + height)
}