package y2018.day03

private val regex = """^#(\d+) @ (\d+),(\d+): (\d+)x(\d+)$""".toRegex()

fun main() {
    val claims = generateSequence(::readLine).toList().map {
        regex.find(it)!!.destructured.let { (number, x, y, width, height) ->
            Claim(number.toInt(), x.toInt(), y.toInt(), width.toInt(), height.toInt())
        }
    }

    val (allOverlapping, sum) = claims.findOverlaps()

    println(sum)
    claims.filterNot { allOverlapping.contains(it) }.forEach { println(it.number) }
}

private fun List<Claim>.findOverlaps(): Pair<MutableSet<Claim>, Int> {
    val overlapping = mutableSetOf<Claim>()
    var sum = 0
    coordinates().forEach { (x, y) ->
        val claims = filter { it.contains(x, y) }
        if (claims.count() > 1) {
            ++sum
            overlapping.addAll(claims)
        }
    }
    return Pair(overlapping, sum)
}

private fun coordinates() = sequence {
    for (x in 1..1000) {
        for (y in 1..1000) {
            yield(x to y)
        }
    }
}

data class Claim(val number: Int, val x: Int, val y: Int, val width: Int, val height: Int) {
    fun contains(x: Int, y: Int) = x in this.x until (this.x + width) && y in this.y until (this.y + height)
}