package day20

fun main() {
    val presents = (1..1000000)
        .map { i ->
            val sum = (1..i)
                .filter { i % it == 0 }
                .sumOf { it * 10 }
            println("$i $sum")
            IndexedValue(i, sum)
        }

    val presentsLazy = sequence {
        var i = 850000
        while (true) {
            val sum = (1..i)
                .filter { i % it == 0 && i / it <= 50 }
                .sumOf { it * 11 }
            yield(IndexedValue(i, sum))
            i++
        }
    }

    presents.first { it.value >= input() }.also { println(it.index) }
    presentsLazy.first { it.value >= input() }.also { println(it.index) }
}

fun input() = 36000000