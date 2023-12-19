import kotlin.math.max
import kotlin.math.min

fun main() {
    val (workflows, parts) = String(System.`in`.readAllBytes()).split("\n\n").let { (a, b) ->
        Pair(
            a.lines().associate { line -> line.split("{").let { it[0] to it[1].dropLast(1).split(",") } },
            b.lines().map { line ->
                line.drop(1).dropLast(1).split(",").associate {
                    it.split("=").let { (k, v) -> k to v.toInt() }
                }
            }
        )
    }

    fun String.parseRule(): Pair<String, Triple<String, Boolean, Int>> {
        val (cond, newWf) = split(":")
        val lt = "<" in cond
        val (attr, value) = cond.split(if (lt) "<" else ">")
        return Pair(newWf, Triple(attr, lt, value.toInt()))
    }

    fun Map<String, Int>.applyWorkflow(wf: String): String {
        if (wf == "R" || wf == "A") return wf
        for (rule in workflows.getValue(wf)) {
            if (":" !in rule) return applyWorkflow(rule)
            val (newWf, condition) = rule.parseRule()
            val (attr, lt, value) = condition
            val useWf = getValue(attr).let { if (lt) it < value else it > value }
            if (useWf) return applyWorkflow(newWf)
        }
        return "R"
    }

    println(parts.filter { it.applyWorkflow("in") == "A" }.sumOf { it.values.sum() })

    fun Map<String, List<IntRange>>.simulate(wf: String): Long = when (wf) {
        "R" -> 0L
        "A" -> values.map { it.sumOf { it.last - it.first + 1 } }.map(Int::toLong).reduce(Long::times)
        else -> {
            var remaining = toMutableMap()
            var sum = 0L
            for (rule in workflows.getValue(wf)) {
                if (values.any(List<IntRange>::isEmpty)) break
                if (":" !in rule) {
                    sum += remaining.simulate(rule)
                    break
                }

                val (newWf, condition) = rule.parseRule()
                val (attr, lt, value) = condition

                fun List<IntRange>.lessThan(v: Int) =
                    mapNotNull { if (it.first >= v) null else it.first..min(it.last, v - 1) }

                fun List<IntRange>.greaterThan(v: Int) =
                    mapNotNull { if (it.last <= v) null else max(v + 1, it.first)..it.last }

                val newRanges = remaining.toMutableMap().apply {
                    set(attr, getValue(attr).let { if (lt) it.lessThan(value) else it.greaterThan(value) })
                }

                sum += newRanges.simulate(newWf)

                remaining = remaining.toMutableMap().apply {
                    set(attr, getValue(attr).let { if (lt) it.greaterThan(value - 1) else it.lessThan(value + 1) })
                }
            }
            sum
        }
    }

    println("xmas".associate { it.toString() to listOf(1..4000) }.simulate("in"))
}