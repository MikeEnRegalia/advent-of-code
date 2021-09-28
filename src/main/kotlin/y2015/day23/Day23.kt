package y2015.day23

fun main() {
    val program = input()

    compute(mutableMapOf("a" to 0, "b" to 0), program).also { println(it) }
    compute(mutableMapOf("a" to 1, "b" to 0), program).also { println(it) }
}

private fun compute(
    r: MutableMap<String, Int>,
    program: List<Pair<String, List<String>>>
): Int {
    fun get(reg: String) = r[reg] ?: throw IllegalArgumentException(reg)

    var pos = 0
    while (pos in program.indices) {
        val (instruction, args) = program[pos]
        when (instruction) {
            "hlf" -> {
                r.set(args[0], get(args[0]) / 2).also { pos++ }
            }
            "tpl" -> {
                r[args[0]] = get(args[0]) * 3; pos++
            }
            "inc" -> {
                r[args[0]] = get(args[0]) + 1; pos++
            }
            "jmp" -> {
                pos += args[0].toInt()
            }
            "jie" -> {
                pos += if (get(args[0]) % 2 == 0) args[1].toInt() else 1
            }
            "jio" -> {
                pos += if (get(args[0]) == 1) args[1].toInt() else 1
            }
            else -> throw IllegalStateException()
        }
    }
    return get("b")
}

fun input() = """jio a, +19
inc a
tpl a
inc a
tpl a
inc a
tpl a
tpl a
inc a
inc a
tpl a
tpl a
inc a
inc a
tpl a
inc a
inc a
tpl a
jmp +23
tpl a
tpl a
inc a
inc a
tpl a
inc a
inc a
tpl a
inc a
tpl a
inc a
tpl a
inc a
tpl a
inc a
inc a
tpl a
inc a
inc a
tpl a
tpl a
inc a
jio a, +8
inc b
jie a, +4
tpl a
inc a
jmp +2
hlf a
jmp -7"""
    .split("\n")
    .map { Pair(it.substring(0, 3), it.substring(4)) }
    .map { Pair(it.first, it.second.split(", ")) }