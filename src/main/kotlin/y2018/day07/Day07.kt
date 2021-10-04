package y2018.day07

fun main() {
    val rules = generateSequence(::readLine)
        .map { Regex("""^Step (\w) must be finished before step (\w) can begin.$""").find(it) }
        .map { it!!.destructured.toList() }.toList()

    val done = mutableListOf<String>()
    fun String.isOpen() = !done.contains(this)
    fun String.isAvailable() = isOpen() && rules.none { (dep, step) -> dep.isOpen() && step == this }

    with(rules.flatten().sorted()) {
        while (true) firstOrNull { it.isAvailable() }?.let { done.add(it) } ?: break
    }

    done.joinToString("").also { println(it) }

    // part 2
    done.clear()

    fun duration(step: String) = 61 + (step[0] - 'A')
    val workers = 5
    var workingOn = listOf<Pair<String, Int>>()

    var second = 0
    with(rules.flatten().distinct().sorted()) {
        while (true) {
            filter { step -> step.isAvailable() && workingOn.none { step == it.first }}
                .take(workers - workingOn.size)
                .map { Pair(it, duration(it)) }
                .let { workingOn = workingOn.plus(it) }
            workingOn = workingOn.mapNotNull { (step, remainingSeconds) ->
                if (remainingSeconds == 1) null.also { done.add(step) }
                else step to remainingSeconds - 1
            }
            second++
            if (done.size == size) break
        }
    }
    done.joinToString("").also { println("$it: $second") }
}

