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
        move(password, from, to)
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
        move(password, from, to)
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
            else -> throw IllegalStateException()
        }
        rotateLeft(steps, password)
    }
    else -> throw IllegalStateException(rule)
}

private fun move(password: MutableList<Char>, from: Int, to: Int) = password.apply { add(to, removeAt(from)) }

private fun reverse(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (from, to) = rule.split(" ").mapNotNull(String::toIntOrNull)
    val scrambled = password.toMutableList()
    for (i in from..to) {
        scrambled[i] = password[to - (i - from)]
    }
    return scrambled
}

private fun swap(rule: String, password: MutableList<Char>) = password.also {
    val (x, y) = rule.split(" ").mapNotNull(String::toIntOrNull)
    swap(it, x, y)
}

private fun swap(it: MutableList<Char>, x: Int, y: Int) {
    val cx = it[x]
    it[x] = it[y]
    it[y] = cx
}

private fun swapLetter(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (a, b) = rule.split(" ").mapNotNull { if (it.length == 1) it[0] else null }
    swap(password, password.indexOf(a), password.indexOf(b))
    return password
}

private fun rotateLeft(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
    return rotateLeft(steps, password)
}

private fun rotateLeft(steps: Int, password: MutableList<Char>) =
    password.subList(steps, password.size).plus(password.subList(0, steps)).toMutableList()

private fun rotateRight(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
    return rotateRight(steps, password)
}

private fun rotateRight(steps: Int, password: MutableList<Char>): MutableList<Char> {
    val actualSteps = steps % password.size
    val pos = password.size - actualSteps
    return password.subList(pos, password.size).plus(password.subList(0, pos)).toMutableList()
}