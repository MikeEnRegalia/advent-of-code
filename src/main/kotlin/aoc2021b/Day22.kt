package aoc2021b

import kotlin.math.max
import kotlin.math.min

fun main() {
    val regex = Regex("""([-]?\d+)\.\.([-]?\d+).*""".let { """(on|off).*x=$it.*y=$it.*z=$it""" })
    val cuboids = generateSequence(::readLine)
        .mapNotNull { regex.find(it)?.destructured?.toList() }
        .map { tokens -> tokens.first() to tokens.drop(1).map(String::toInt).chunked(2) { it[0]..it[1] } }
        .map { (on, r) -> Cuboid(r[0], r[1], r[2]) to (on == "on") }
        .toList()

    val initProcedureRegion = (-50..50).let { Cuboid(it, it, it) }
    cuboids
        .mapNotNull { (cuboid, on) -> cuboid.intersect(initProcedureRegion)?.let { it to on } }
        .fold(mutableSetOf<Triple<Int, Int, Int>>()) { acc, (cuboid, on) ->
            for (x in cuboid.x)
                for (y in cuboid.y)
                    for (z in cuboid.z)
                        if (on) acc.add(Triple(x, y, z)) else acc.remove(Triple(x, y, z))
            acc
        }
        .run { println(size) }

    cuboids
        .fold(setOf<Cuboid>()) { space, (cuboid, on) -> space.add(cuboid, on) }
        .run { println(sumOf { it.size }) }
}

fun Set<Cuboid>.add(cuboid: Cuboid, on: Boolean): Set<Cuboid> = buildSet {
    cuboid.also { cuboid ->
        val (overlapping, nonOverlapping) = this@add.partition { it.contains(cuboid) }
        addAll(nonOverlapping)
        addAll(overlapping.flatMap { it.remove(cuboid) })
        if (on) add(cuboid)
    }
}

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
    val size = x.size() * y.size() * z.size()

    fun contains(c: Cuboid) = c.x.intersect(x) != null && c.y.intersect(y) != null && c.z.intersect(z) != null
    fun includes(o: Cuboid) = x.includes(o.x) && y.includes(o.y) && z.includes(o.z)
    fun intersect(c: Cuboid) = takeIf { contains(c) }
        ?.let { Cuboid(c.x.intersect(x)!!, c.y.intersect(y)!!, c.z.intersect(z)!!) }

    fun remove(r: Cuboid): Set<Cuboid> {
        if (r.includes(this)) return setOf()
        val i = intersect(r) ?: return setOf(this)
        val beyond = Cuboid(x, y, i.z.last + 1..z.last)
        val before = Cuboid(x, y, z.first until i.z.first)
        val left = Cuboid(x.first until i.x.first, y, i.z)
        val right = Cuboid(i.x.last + 1..x.last, y, i.z)
        val below = Cuboid(i.x, y.first until i.y.first, i.z)
        val above = Cuboid(i.x, i.y.last + 1..y.last, i.z)
        return listOfNotNull(beyond, before, left, right, below, above).filterNot { it.size == 0L }.toSet()
    }
}

fun IntRange.size() = last - first + 1L

private fun IntRange.intersect(o: IntRange): IntRange? {
    val maxFirst = max(first, o.first)
    val minLast = min(last, o.last)
    return if (maxFirst > minLast) null else maxFirst..minLast
}

private fun IntRange.includes(o: IntRange) = first <= o.first && last >= o.last