package aoc2021b

import kotlin.math.abs

fun main() {
    val scanners = loadScanners()
    val allBeacons = scanners.first().toMutableSet()
    val scannerPositions = mutableMapOf(scanners.first() to Pos3D(0, 0, 0))

    while (scanners.any { it !in scannerPositions.keys }) {
        val (allBeaconsX, allBeaconsY, allBeaconsZ) = with(allBeacons) {
            listOf(map { it.x }, map { it.y }, map { it.z })
        }
        val (minX, maxX) = with(allBeaconsX) { minOf { it } to maxOf { it } }
        val (minY, maxY) = with(allBeaconsY) { minOf { it } to maxOf { it } }
        val (minZ, maxZ) = with(allBeaconsZ) { minOf { it } to maxOf { it } }

        fun connect(scanner: Set<Pos3D>) = rotate24.map { scanner.map(it) }.firstNotNullOfOrNull { rotated ->
            val (rMinX, rMaxX) = rotated.minOf { it.x } to rotated.maxOf { it.x }
            val xOffsets = (minX - rMaxX..maxX - rMinX).filter { o ->
                rotated.map { it.x + o }.count { it in allBeaconsX } >= 12
            }.ifEmpty { return@firstNotNullOfOrNull null }

            val (rMinY, rMaxY) = rotated.minOf { it.y } to rotated.maxOf { it.y }
            val yOffsets = (minY - rMaxY..maxY - rMinY).filter { o ->
                rotated.map { it.y + o }.count { it in allBeaconsY } >= 12
            }.ifEmpty { return@firstNotNullOfOrNull null }

            val (rMinZ, rMaxZ) = rotated.minOf { it.z } to rotated.maxOf { it.z }
            val zOffsets = (minZ - rMaxZ..maxZ - rMinZ).filter { o ->
                rotated.map { it.z + o }.count { it in allBeaconsZ } >= 12
            }.ifEmpty { return@firstNotNullOfOrNull null }

            for (ox in xOffsets) for (oy in yOffsets) for (oz in zOffsets) {
                val moved = rotated.map { it.move(ox, oy, oz) }
                if (moved.count { it in allBeacons } >= 12) {
                    scannerPositions[scanner] = Pos3D(ox, oy, oz)
                    return@firstNotNullOfOrNull moved
                }
            }
            null
        }

        scanners.filter { it !in scannerPositions.keys }.firstNotNullOfOrNull(::connect)?.let { allBeacons.addAll(it) }
    }

    println(allBeacons.size)
    scannerPositions.values.let { them ->
        them.flatMap { them.map(it::distanceTo) }.maxOf { it }.also { println(it) }
    }
}

private fun loadScanners() = buildList {
    var scanner: MutableList<Pos3D>? = null
    for (line in generateSequence(::readLine).filterNot(String::isBlank)) {
        if (line.startsWith("---")) {
            if (scanner != null) add(scanner.toSet())
            scanner = mutableListOf()
            continue
        }
        scanner!!.add(line.split(",").map(String::toInt).let { Pos3D(it[0], it[1], it[2]) })
    }
    if (scanner != null) add(scanner.toSet())
}

val rotate24 = sequenceOf<List<(Pos3D) -> Pos3D>>(
    listOf({ it }, { it.rX() }, { it.rX2() }, { it.rX3() }),
    listOf({ it.rY2() }, { it.rY2().rX() }, { it.rY2().rX2() }, { it.rY2().rX3() }),

    listOf({ it.rY() }, { it.rY().rX() }, { it.rY().rX2() }, { it.rY().rX3() }),
    listOf({ it.rY3() }, { it.rY3().rX() }, { it.rY3().rX2() }, { it.rY3().rX3() }),

    listOf({ it.rZ() }, { it.rZ().rX() }, { it.rZ().rX2() }, { it.rZ().rX3() }),
    listOf({ it.rZ3() }, { it.rZ3().rX() }, { it.rZ3().rX2() }, { it.rZ3().rX3() }),
).flatten()

data class Pos3D(val x: Int, val y: Int, val z: Int) {
    fun move(x: Int, y: Int, z: Int) = copy(x = this.x + x, y = this.y + y, z = this.z + z)
    fun distanceTo(o: Pos3D) = abs(x - o.x) + abs(y - o.y) + abs(z - o.z)

    fun rX() = copy(y = -z, z = y)
    fun rX2() = copy(y = -y, z = -z)
    fun rX3() = copy(y = z, z = -y)
    fun rY() = copy(x = z, z = -x)
    fun rY2() = copy(x = -x, z = -z)
    fun rY3() = copy(x = -z, z = x)
    fun rZ() = copy(x = -y, y = x)
    fun rZ2() = copy(x = -x, y = -y)
    fun rZ3() = copy(x = y, y = -x)
}