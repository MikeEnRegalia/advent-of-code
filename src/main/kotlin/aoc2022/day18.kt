package aoc2022

fun main() {
    data class Point(val x: Int, val y: Int, val z: Int) {
        fun neighbors() = sequenceOf(
            copy(x = x + 1), copy(y = y + 1), copy(z = z + 1),
            copy(x = x - 1), copy(y = y - 1), copy(z = z - 1),
        )
    }

    val cubes = generateSequence(::readlnOrNull)
        .map { it.split(",").map(String::toInt).let { (x, y, z) -> Point(x, y, z) } }
        .toSet()

    println(cubes.sumOf { p -> p.neighbors().filter { it !in cubes }.count() })

    fun calcAir(): Set<Point> {
        val (minX, maxX) = cubes.minOf { it.x } to cubes.maxOf { it.x }
        val (minY, maxY) = cubes.minOf { it.y } to cubes.maxOf { it.y }
        val (minZ, maxZ) = cubes.minOf { it.z } to cubes.maxOf { it.z }

        var s = Point(minX - 1, minY - 1, minZ - 1)
        val air = mutableSetOf<Point>()
        val u = mutableSetOf<Point>()

        while (true) {
            s.neighbors()
                .filter { it !in cubes && it !in air}
                .filter { it.x in minX - 1..maxX + 1 }
                .filter { it.y in minY - 1..maxY + 1 }
                .filter { it.z in minZ - 1..maxZ + 1 }
                .forEach { u += it }
            air += s
            u -= s
            s = u.firstOrNull() ?: break
        }
        return air
    }

    val air = calcAir()

    println(cubes.sumOf { p -> p.neighbors().filter { it !in cubes && it in air }.count() })
}

