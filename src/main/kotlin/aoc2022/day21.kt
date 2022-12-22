package aoc2022

fun main() {
    val monkeys = generateSequence(::readlnOrNull).map { it.split(": ").map { it.split(" ") } }.associate { it[0].first() to it[1] }

    fun lookup(name: String, part2: Boolean = false): Long? {
        if (part2 && name == "humn") return null
        val number = name.toLongOrNull()
        if (number != null) return number

        val op = monkeys.getValue(name)
        if (op.size == 1) return op.first().toLong()

        val a = lookup(op.first(), part2)
        val o = op[1]
        val b = lookup(op.last(), part2)
        if (a == null || b == null) return null
        return when (o) {
            "+" -> a + b
            "*" -> a * b
            "-" -> a - b
            "/" -> a / b
            else -> throw IllegalArgumentException(o)
        }
    }

    fun part2(): Long {
        var curr = "root"
        var currVal = 0L
        while (true) {
            if (curr == "humn") return currVal
            val (m1, origOp, m2) = monkeys.getValue(curr)
            val v1 = lookup(m1, part2 = true)
            val v2 = lookup(m2, part2 = true)
            val op = if (curr == "root") "=" else origOp
            if (v1 == null && v2 != null) {
                curr = m1
                currVal = when (op) {
                    "=" -> v2
                    "+" -> currVal - v2
                    "-" -> currVal + v2
                    "*" -> currVal / v2
                    else -> currVal * v2
                }
            }
            if (v2 == null && v1 != null) {
                curr = m2
                currVal = when (op) {
                    "=" -> v1
                    "+" -> currVal - v1
                    "-" -> v1 - currVal
                    "*" -> currVal / v1
                    else -> v1 / currVal
                }
            }
        }
    }

    println(lookup("root"))
    println(part2())
}

