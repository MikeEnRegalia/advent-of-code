@file:OptIn(ExperimentalStdlibApi::class)

package aoc2020

fun main() {
    val input = generateSequence(::readLine).toList()
    val rules = buildMap {
        input.subList(0, input.indexOf("")).map { it.split(":") }.forEach { (key, value) ->
            set(key.trim(), value.trim())
        }
    }
    println(rules)
    val messages = input.drop(rules.size + 1)

    fun Map<String, String>.resolve(rule: String): String {
        val instructions = get(rule)!!
        return when {
            instructions.startsWith("\"") -> instructions.substring(1, instructions.length - 1)
            else -> {
                val alternatives = instructions.split("|").map {
                    it.trim().split(" ").joinToString("") { rule -> resolve(rule) }
                }
                alternatives.singleOrNull() ?: "(${alternatives.joinToString("|")})"
            }
        }
    }

    val regex = Regex("""^${rules.resolve("0")}$""")
    println(regex)

    val part1 = messages.count { regex.matches(it) }
    println(part1)
}