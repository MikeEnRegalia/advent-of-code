package aoc2021b

// aoc2019b.part2: not 47336 (too high)
fun main() {
    Day23.solve(generateSequence(::readLine))
}

class Day23 {
    companion object {
        fun solve(lines: Sequence<String>) {
            val pods = lines
                .flatMapIndexed { y, l -> l.mapIndexed { x, c -> Pos(x, y) to c } }
                .toMap().entries.filter { e -> e.value.toString() in PodType.values().map { it.name } }.map {
                    Pod(PodType.valueOf(it.value.toString()), it.key)
                }.toSet()

            println(Day23().walk(pods))
        }

        val hallway = listOf(1, 2, 4, 6, 8, 10, 11).map { Pos(it, 1) }
        val nonStop = listOf(3, 5, 7, 9).map { Pos(it, 1) }
        fun all(pods: Set<Pod>) = hallway.plus(nonStop).plus(PodType.values().flatMap { it.room(pods) })
    }

    private var min = Integer.MAX_VALUE
    private val cacheConfigs = mutableMapOf<Set<Pod>, Int>()
    private val cacheInvalidConfigs = mutableSetOf<Set<Pod>>()

    fun walk(pods: Set<Pod>, prevEnergy: Int = 0): Int? {
        val podsToMove = pods.filterNot { it.isSet(pods.minus(it)) }
        if (podsToMove.isEmpty()) {
            if (prevEnergy < min) {
                min = prevEnergy
                println("Bingo: $prevEnergy")
            }
            return prevEnergy
        }

        cacheConfigs[pods]?.let { return prevEnergy + it }

        if (pods in cacheInvalidConfigs || prevEnergy > min) return null
        return podsToMove.sortedBy { it.type }.flatMap { podToMove ->
            destinations(podToMove, pods.minus(podToMove)).mapNotNull { (newPos, energy) ->
                walk(pods.minus(podToMove).plus(podToMove.copy(pos = newPos)), prevEnergy + energy)
            }
        }.minOfOrNull { it }.also {
            if (it == null) cacheInvalidConfigs.add(pods) else cacheConfigs[pods] = it - prevEnergy
        }
    }

    private fun destinations(pod: Pod, others: Set<Pod>): Set<Pair<Pos, Int>> {
        fun walk(path: List<Pos>): Sequence<List<Pos>> {
            return path.last().adj()
                .filter { it !in path }
                .filter { it in all(others.plus(pod)) }
                .filter { pos -> others.none { it.pos == pos } }
                .map { path.plus(it) }
                .flatMap { sequenceOf(it).plus(walk(it)) }
        }

        val validPaths = walk(listOf(pod.pos)).filter { isValidDestination(pod, others, it.last()) }

        if (false) {
            printPaths(others, pod, validPaths)
        }

        val toRoom = validPaths.singleOrNull { it.last() in pod.type.room(others.plus(pod)) }
        if (toRoom != null) {
            return setOf(toRoom.last() to pod.type.energy * (toRoom.size - 1))
        }

        return validPaths.map { it.last() to pod.type.energy * (it.size - 1) }.toSet()
    }

    private fun printPaths(
        others: Set<Pod>,
        pod: Pod,
        validPaths: Sequence<List<Pos>>
    ) {
        for (y in 0..4) {
            println((0..12).joinToString("") { x ->
                val p = others.plus(pod).singleOrNull { it.pos == Pos(x, y) }
                when {
                    p != null -> p.type.toString()
                    Pos(x, y) in validPaths.map { it.last() } -> "X"
                    Pos(x, y) in all(others.plus(pod)) -> "."
                    else -> "#"
                }
            })
        }
    }

    private fun isValidDestination(pod: Pod, others: Set<Pod>, pos: Pos): Boolean {
        if (pos == pod.pos) return false
        if (pod.isSet(others)) return false
        if (pos in nonStop) return false
        if (pos in pod.type.room(others.plus(pod))) return pod.copy(pos = pos).isSet(others)
        if (pos in hallway) return pod.pos !in hallway
        return false
    }

    enum class PodType(private val column: Int, val energy: Int) {
        A(3, 1),
        B(5, 10),
        C(7, 100),
        D(9, 1000);

        fun room(pods: Iterable<Pod>): List<Pos> = (0 until pods.count { it.type == this }).map { Pos(column, 2 + it) }
    }

    data class Pod(val type: PodType, val pos: Pos) {
        fun isSet(others: Set<Pod>) = type.room(others.plus(this)).let { room ->
            pos in room && room.filter { it.y > pos.y }
                .all { it in others.filter { it.type == type }.map { it.pos } }
        }
    }

    data class Pos(val x: Int, val y: Int) {
        fun adj() = sequenceOf(copy(y = y - 1), copy(y = y + 1), copy(x = x - 1), copy(x = x + 1))
    }
}