package aoc2024

fun main() {
    val initialNumbers = generateSequence(::readLine).map { it.toLong() }.toList()

    fun Long.mix(n: Long) = xor(n)
    fun Long.prune() = this % 16777216
    fun Long.next(): Long {
        var secret = this
        secret = secret.mix(secret * 64).prune()
        secret = secret.mix(secret / 32).prune()
        secret = secret.mix(secret * 2048).prune()
        return secret
    }

    val numbers = initialNumbers.map { init ->
        val numbers = mutableListOf(init)
        for (i in 0..<2000) {
            numbers += numbers.last().next()
        }
        numbers
    }
    println(numbers.sumOf { it.last() })

    val changes = numbers.map {
        val res = mutableMapOf<List<Int>, Int>()
        it.map { it.toString().last().digitToInt() }.windowed(2).map { it[1] to it[1] - it[0] }
            .reversed().windowed(4).forEach {
                val price = it[0].first
                val changes = it.map { it.second }.take(4)
                res[changes] = price
            }
        res
    }

    println(changes.flatMap { it.keys }.toSet().maxOf { change ->
        changes.sumOf { it[change] ?: 0 }
    })

}
