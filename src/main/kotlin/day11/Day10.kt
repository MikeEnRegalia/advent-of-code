package day11

fun main() {
    val oldPassword = "cqjxjnds"
    val newPassword = oldPassword.incAndCheck()
    val evenNewerPassword = newPassword.incAndCheck()
    println("new: $newPassword and even newer: $evenNewerPassword")
}

fun String.incAndCheck(): String {
    var s = this
    while (true) {
        s = s.inc()
        if (s.hasStraight() && s.hasNoBadLetters() && s.countTwins() > 1) return s
    }
}

fun String.inc(): String {
    var carry = true
    return reversed().map {
        if (!carry) it else {
            if (it == 'z') 'a' else (it + 1).also { carry = false }
        }
    }.reversed().let { String(it.toCharArray()) }
}

fun String.hasStraight(): Boolean {
    for ((i, c) in withIndex().take(length - 3)) {
        if (c == this[i + 1] - 1 && c == this[i + 2] - 2)
            return true
    }
    return false
}

fun String.hasNoBadLetters() = none { "iol".contains(it) }

fun String.countTwins(): Int {
    val result = mutableListOf<Char>()
    for ((i, c) in withIndex().take(length - 1)) {
        if (c == this[i + 1] && result.lastOrNull() != c) {
            result += c
        }
    }
    return result.size
}