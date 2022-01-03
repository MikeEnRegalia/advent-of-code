package aoc2020

fun main() {
    val input = generateSequence(::readLine).toList()
    val rules = buildMap {
        input.subList(0, input.indexOf("")).map { it.split(":") }.forEach { (key, value) ->
            set(key.trim(), value.trim())
        }
    }
    val messages = input.drop(rules.size + 1)

    fun Map<String, String>.resolve(rule: String, part2: Boolean): String {
        if (part2) {
            if (rule == "8") {
                return "(${resolve("42", true)})+"
            }
            if (rule == "11") {
                val a = resolve("42", false)
                val b = resolve("31", false)
                return "($a$b|$a$a$b$b|$a$a$a$b$b$b|$a$a$a$a$b$b$b$b)"
            }
        }

        val instructions = get(rule)!!
        return when {
            instructions.startsWith("\"") -> instructions.substring(1, instructions.length - 1)
            else -> {
                val alternatives = instructions.split("|").map {
                    it.trim().split(" ").joinToString("") { rule -> resolve(rule, part2) }
                }
                alternatives.singleOrNull() ?: "(${alternatives.joinToString("|")})"
            }
        }
    }

    val regexPart1 = Regex("""^${rules.resolve("0", part2 = false)}$""")
    val regexPart2 = Regex("""^${rules.resolve("0", part2 = true)}$""")
    println("42: ${rules.resolve("42", part2 = false)}")
    println("31: ${rules.resolve("31", part2 = false)}")

    println(messages.count { regexPart1.matches(it) })
    println(messages.count { regexPart2.matches(it) })
}