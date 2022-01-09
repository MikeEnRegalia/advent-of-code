package aoc2016

fun <T> Regex.finD(s: String, f: (MatchResult.Destructured) -> T) = f(find(s)!!.destructured)

fun main() {
    val bots = mutableMapOf<Int, Pair<Int?, Int?>>()
    val rules = mutableMapOf<Int, Pair<Pair<String, Int>, Pair<String, Int>>>()

    fun giveToBot(b: Int, v: Int) {
        val values = bots[b]
        if (values == null) {
            bots[b] = v to null
            return
        }
        val x = values.first!!
        bots[b] = if (x > v) v to x else x to v
    }

    generateSequence(::readLine).forEach { line ->
        if (line.startsWith("value ")) {
            val (b, v) = """^value (\d+) goes to bot (\d+)$""".toRegex()
                .finD(line) { (v, b) -> b.toInt() to v.toInt() }
            giveToBot(b, v)
        } else {
            """^bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)$""".toRegex()
                .finD(line) { (b, ml, l, mh, h) -> rules[b.toInt()] = Pair(ml, l.toInt()) to Pair(mh, h.toInt()) }
        }
    }
    val outputs = mutableMapOf<Int, Int>()

    while (bots.isNotEmpty()) {
        bots.toMutableMap().forEach { (b, v) ->
            val (l, h) = v
            if (l == null || h == null) return@forEach
            if (l == 17 && h == 61) println(b)

            rules[b]!!.toList().zip(listOf(l, h)).forEach { (rule, v) ->
                if (rule.first == "bot") giveToBot(rule.second, v) else outputs[rule.second] = v
            }
            bots.remove(b)
        }
    }
    println(outputs[0]!! * outputs[1]!! * outputs[2]!!)
}

