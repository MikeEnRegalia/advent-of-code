package ec2024

fun main() {
    data class Node(val parent: String, val children: List<String>)

    val branches = generateSequence(::readLine)
        .map { line -> line.split(":").let { (p, c) -> Node(p, c.split(",")) } }
        .toList()

    val apples = branches.filter { "@" in it.children }.map { branch ->
        buildList {
            var branch = branch
            add("@")
            while (true) {
                add(0, branch.parent)
                branch = branches.singleOrNull { branch.parent in it.children } ?: break
            }
        }
    }

    val result = apples.groupBy { it.size }.values.minBy { it.size }.single()

    println(result.joinToString(""))
    println(result.joinToString("") { it.take(1) })
}
