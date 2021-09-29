package y2018.day05

fun main() {
    val polymer = readLine()!!
    println(polymer.react().length)

    ('a'..'z')
        .minOf { polymer.replace(it.toString(), "", ignoreCase = true).react().length }
        .also { println(it) }
}

fun String.react(): String {
    var result = this
    while (true) {
        result.reactOne().let {
            if (result == it) return result
            result = it
        }
    }
}

fun String.reactOne(): String {
    if (length < 2) return this
    for (i in 0..length - 2) {
        if (this[i] == this[i + 1].otherCase()) {
            return substring(0, i) + substring(i + 2)
        }
    }
    return this
}

fun Char.otherCase() = if (isUpperCase()) lowercaseChar() else uppercaseChar()
