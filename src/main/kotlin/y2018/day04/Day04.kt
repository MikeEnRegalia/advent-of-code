package y2018.day04

fun main() {
    with(loadGuardData().groupBy { it.guard }) {
        mapValues { (_, intervals) -> intervals.sumOf { it.until - it.begin } }
            .entries.maxByOrNull { it.value }!!
            .also { (guard) ->
                this[guard]!!
                    .findSleepiestMinute()
                    .also { (minute) -> println(guard.value * minute) }
            }

        mapValues { (_, intervals) -> intervals.findSleepiestMinute() }
            .entries.maxByOrNull { it.value.second }!!
            .also { (guard, value) -> println(guard.value * value.first) }
    }
}

private fun loadGuardData(): List<Sleeping> = with(mutableListOf<Sleeping>()) {
    var prev: GuardState? = null
    generateSequence(::readLine)
        .map {
            """^\[(.+)] (.*)$""".toRegex().find(it)!!.destructured.let { (date, command) -> date to command }
        }
        .sortedBy { it.first }
        .map { it.first.substring(it.first.lastIndexOf(':') + 1).toInt() to it.second }
        .map { (minute, info) ->
            if (info.startsWith("Guard #")) {
                val from = "Guard #".length
                val guard = Guard(info.substring(from, info.indexOf(" ", from)).toInt())
                GuardState(guard, minute, false)
            } else {
                with(prev!!) {
                    next(minute).also { if (asleep) add(Sleeping(guard, this.minute, minute)) }
                }
            }
        }
        .forEach { prev = it }
    this
}

private fun List<Sleeping>.findSleepiestMinute() = (0..59)
    .map { it to count { i -> it in i.begin until i.until } }
    .maxByOrNull { it.second }!!

@JvmInline
value class Guard(val value: Int)

data class GuardState(val guard: Guard, val minute: Int, val asleep: Boolean) {
    fun next(minute: Int) = copy(minute = minute, asleep = !asleep)
}

data class Sleeping(val guard: Guard, val begin: Int, val until: Int)