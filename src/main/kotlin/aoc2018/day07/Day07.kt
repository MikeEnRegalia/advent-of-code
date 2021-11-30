package aoc2018.day07

fun main() {
    val rules = generateSequence(::readLine)
        .map { Regex("""^Step (\w) must be finished before step (\w) can begin.$""").find(it) }
        .map { it!!.destructured.toList() }.toList()

    val done = mutableListOf<String>()
    fun String.isOpen() = !done.contains(this)
    fun String.isAvailable() = isOpen() && rules.none { (dep, step) -> dep.isOpen() && step == this }

    val steps = rules.flatten().distinct().sorted()
    while (true) steps.firstOrNull { it.isAvailable() }?.let { done.add(it) } ?: break
    done.joinToString("").also { println(it) }
    done.clear()

    var workingOn = listOf<Pair<String, Int>>()
    var second = 0
    while (done.size < steps.size) {
        workingOn = workingOn.run {
            steps.filter { step -> step.isAvailable() && none { step == it.first } }
                .take(5 - size)
                .map { Pair(it, 61 + (it[0] - 'A')) }
                .let { plus(it) }
                .mapNotNull { (step, remainingSeconds) ->
                    if (remainingSeconds == 1) null.also { done.add(step) }
                    else step to remainingSeconds - 1
                }
        }
        second++
    }
    done.joinToString("").also { println("$it: $second") }
}

