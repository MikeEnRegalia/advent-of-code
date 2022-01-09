package aoc2016

fun main() {
    val bots = mutableMapOf<Int, Pair<Int?, Int?>>()
    val rules = mutableMapOf<Int, Pair<Pair<String, Int>, Pair<String, Int>>>()

    fun giveToBot(b: Int, v: Int) {
        val values = bots[b]
        val x = values?.first ?: values?.second
        bots[b] = if (x == null) v to null else if (x > v) v to x else x to v
    }

    generateSequence(::readLine).forEach { line ->
        if (line.startsWith("value ")) {
            val (v, b) = """^value (\d+) goes to bot (\d+)$""".toRegex()
                .find(line)!!.destructured.toList().let { it[0].toInt() to it[1].toInt() }
            giveToBot(b, v)
        } else {
            """^bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)$""".toRegex()
                .find(line)!!.destructured.toList().let {
                    rules[it[0].toInt()] =
                        Pair(it[1], it[2].toInt()) to Pair(it[3], it[4].toInt())
                }
        }
    }

    val outputs = mutableMapOf<Int, Int>()

    while (true) {
        val fullBots = bots.filterValues { it.first != null && it.second != null }
        if (fullBots.isEmpty()) break
        fullBots.forEach { (b, v) ->
            val (l, h) = v
            if (l == 17 && h == 61) {
                println(b)
            }
            val (ruleL, ruleH) = rules[b]!!

            if (ruleL.first == "bot") giveToBot(ruleL.second, l!!) else outputs[ruleL.second] = l!!
            if (ruleH.first == "bot") giveToBot(ruleH.second, h!!) else outputs[ruleH.second] = h!!
            bots -= b
        }
    }
    println(outputs[0]!! * outputs[1]!! * outputs[2]!!)
}

