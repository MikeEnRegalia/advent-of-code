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
}

