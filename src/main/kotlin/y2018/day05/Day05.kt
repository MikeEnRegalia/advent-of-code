package y2018.day05

import util.otherCase
import util.remove
import util.withoutSubstring

fun main() {
    readLine()!!.react().let { reacted ->
        reacted.length.also { println(it) }
        'a'.lazyRangeTo('z')
            .map { reacted.remove(it, ignoreCase = true) }
            .minOf { it.react().length }.also { println(it) }
    }
}

fun Int.lazyRangeTo(t: Int) = (this..t).asSequence()
fun Char.lazyRangeTo(t: Char) = (this..t).asSequence()

private fun String.react(): String {
    var result = this
    while (true) {
        result.reactOne().let {
            if (result == it) return result
            result = it
        }
    }
}

private fun String.reactOne() = when {
    length < 2 -> this
    else -> 0.lazyRangeTo(length - 2)
        .filter { this[it] == this[it + 1].otherCase() }
        .map { withoutSubstring(it, 2) }
        .firstOrNull() ?: this
}
