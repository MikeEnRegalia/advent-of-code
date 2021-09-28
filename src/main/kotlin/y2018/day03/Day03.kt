package y2018.day03

private val regex = """^#(\d+) @ (\d+),(\d+): (\d+)x(\d+)$""".toRegex()

fun main() {
    val claims = generateSequence(::readLine).toList()
        .map { row ->
            with(regex.find(row)!!.groupValues.drop(1).map { it.toInt() }) {
                Claim(get(0), get(1), get(2), get(3), get(4))
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