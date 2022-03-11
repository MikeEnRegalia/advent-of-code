package aoc2016

fun main() = generateSequence { readLine() }.toList().let {
    part1(it)
    part2(it)
}

private fun part1(rules: List<String>) {
    var scrambled = "abcdefgh".toMutableList()
    rules.forEach { scrambled = scramble(it, scrambled) }
    println(scrambled.joinToString(""))
}

private fun part2(rules: List<String>) {
    var unscrambled = "fbgdceah".toMutableList()
    rules.reversed().forEach { unscrambled = unscramble(it, unscrambled) }
    println(unscrambled.joinToString(""))
}

private fun scramble(rule: String, password: MutableList<Char>) = when {
    rule.startsWith("swap position ") -> swap(rule, password)
    rule.startsWith("swap letter ") -> swapLetter(rule, password)
    rule.startsWith("reverse ") -> reverse(rule, password)
    rule.startsWith("rotate left ") -> rotateLeft(rule, password)
    rule.startsWith("rotate right ") -> rotateRight(rule, password)
    rule.startsWith("move position ") -> {
        val (from, to) = rule.split(" ").mapNotNull(String::toIntOrNull)
        move(password, to, from)
    }
    rule.startsWith("rotate based ") -> {
        val letter = rule.substring(rule.lastIndexOf(" ") + 1).first()
        val letterIndex = password.indexOf(letter)
        val steps = (1 + letterIndex).let { if (it >= 5) it + 1 else it }
        rotateRight(steps, password)
    }
    else -> throw IllegalStateException(rule)
}

private fun unscramble(rule: String, password: MutableList<Char>) = when {
    rule.startsWith("swap position ") -> swap(rule, password)
    rule.startsWith("swap letter ") -> swapLetter(rule, password)
    rule.startsWith("reverse ") -> reverse(rule, password)
    rule.startsWith("rotate left ") -> rotateRight(rule, password)
    rule.startsWith("rotate right ") -> rotateLeft(rule, password)
    rule.startsWith("move position ") -> {
        val (to, from) = rule.split(" ").mapNotNull(String::toIntOrNull)
        move(password, to, from)
    }
    rule.startsWith("rotate based ") -> {
        val letter = rule.substring(rule.lastIndexOf(" ") + 1).first()
        val steps = when (password.indexOf(letter)) {
            0 -> 9
            1 -> 1
            2 -> 6
            3 -> 2
            4 -> 7
            5 -> 3
            6 -> 8
            7 -> 4
            else -> throw IllegalStateException(password.indexOf(letter).toString())
        }
        rotateLeft(steps, password)
    }
    else -> throw IllegalStateException(rule)
}

private fun move(
    password: MutableList<Char>,
    to: Int,
    from: Int
): MutableList<Char> {
    password.add(to, password.removeAt(from))
    return password
}

private fun reverse(
    rule: String,
    password: MutableList<Char>
): MutableList<Char> {
    val (from, to) = rule.split(" ").mapNotNull(String::toIntOrNull)
    val scrambled2 = password.toMutableList()
    for (i in from..to) {
        scrambled2[i] = password[to - (i - from)]
    }
    return scrambled2
}

private fun swap(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (x, y) = rule.split(" ").mapNotNull(String::toIntOrNull)
    val cx = password[x]
    password[x] = password[y]
    password[y] = cx
    return password
}

private fun swapLetter(rule: String, scrambled: MutableList<Char>): MutableList<Char> {
    val (a, b) = rule.split(" ").mapNotNull { if (it.length == 1) it[0] else null }
    val x = scrambled.indexOf(a)
    val y = scrambled.indexOf(b)
    val cx = scrambled[x]
    scrambled[x] = scrambled[y]
    scrambled[y] = cx
    return scrambled
}

private fun rotateLeft(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
    return rotateLeft(steps, password)
}

private fun rotateLeft(
    steps: Int,
    password: MutableList<Char>
): MutableList<Char> {
    var actualSteps = steps
    var scrambled = password
    actualSteps %= scrambled.size
    val pos = actualSteps
    scrambled = scrambled.subList(pos, scrambled.size).plus(scrambled.subList(0, pos)).toMutableList()
    return scrambled
}

private fun rotateRight(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
    return rotateRight(steps, password)
}

private fun rotateRight(steps: Int, password: MutableList<Char>): MutableList<Char> {
    var actualSteps = steps
    var scrambled = password
    actualSteps %= scrambled.size
    val pos = scrambled.size - actualSteps
    scrambled = scrambled.subList(pos, scrambled.size).plus(scrambled.subList(0, pos)).toMutableList()
    return scrambled
}