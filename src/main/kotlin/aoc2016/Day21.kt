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

private fun scramble(rule: String, password: MutableList<Char>): MutableList<Char> {
    return when {
        rule.startsWith("rotate right ") -> {
            val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
            rotateRight(steps, password)
        }
        rule.startsWith("rotate based ") -> {
            val letter = rule.substring(rule.lastIndexOf(" ") + 1).first()
            val letterIndex = password.indexOf(letter)
            var steps = 1 + letterIndex
            if (steps >= 5) steps++
            rotateRight(steps, password)
        }
        rule.startsWith("rotate left ") -> {
            val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
            rotateLeft(steps, password)
        }
        rule.startsWith("swap position ") -> { swap(rule, password) }
        rule.startsWith("swap letter ") -> {
            swapLetter(rule, password)
            password
        }
        rule.startsWith("move position ") -> {
            val (from, to) = rule.split(" ").mapNotNull(String::toIntOrNull)
            password.add(to, password.removeAt(from))
            password
        }
        rule.startsWith("reverse ") -> {
            val (from, to) = rule.split(" ").mapNotNull(String::toIntOrNull)
            val scrambled2 = password.toMutableList()
            for (i in from..to) {
                scrambled2[i] = password[to - (i - from)]
            }
            scrambled2
        }
        else -> throw IllegalStateException(rule)
    }
}

private fun unscramble(rule: String, input: MutableList<Char>) = when {
    rule.startsWith("rotate left ") -> {
        val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
        rotateRight(steps, input)
    }
    rule.startsWith("rotate based ") -> {
        val letter = rule.substring(rule.lastIndexOf(" ") + 1).first()
        val steps = when (input.indexOf(letter)) {
            0 -> 9
            1 -> 1
            2 -> 6
            3 -> 2
            4 -> 7
            5 -> 3
            6 -> 8
            7 -> 4
            else -> throw IllegalStateException(input.indexOf(letter).toString())
        }
        rotateLeft(steps, input)
    }
    rule.startsWith("rotate right ") -> {
        val (steps) = rule.split(" ").mapNotNull(String::toIntOrNull)
        rotateLeft(steps, input)
    }
    rule.startsWith("swap position ") -> {
        swap(rule, input)
    }
    rule.startsWith("swap letter ") -> {
        val (a, b) = rule.split(" ").mapNotNull { if (it.length == 1) it[0] else null }
        val x = input.indexOf(a)
        val y = input.indexOf(b)
        val cx = input[x]
        input[x] = input[y]
        input[y] = cx
        input
    }
    rule.startsWith("move position ") -> {
        val (to, from) = rule.split(" ").mapNotNull(String::toIntOrNull)
        input.add(to, input.removeAt(from))
        input
    }
    rule.startsWith("reverse ") -> {
        val (from, to) = rule.split(" ").mapNotNull(String::toIntOrNull)
        val scrambled2 = input.toMutableList()
        for (i in from..to) {
            scrambled2[i] = input[to - (i - from)]
        }
        scrambled2
    }
    else -> throw IllegalStateException(rule)
}

private fun swap(rule: String, password: MutableList<Char>): MutableList<Char> {
    val (x, y) = rule.split(" ").mapNotNull(String::toIntOrNull)
    val cx = password[x]
    password[x] = password[y]
    password[y] = cx
    return password
}

private fun swapLetter(rule: String, scrambled: MutableList<Char>) {
    val (a, b) = rule.split(" ").mapNotNull { if (it.length == 1) it[0] else null }
    val x = scrambled.indexOf(a)
    val y = scrambled.indexOf(b)
    val cx = scrambled[x]
    scrambled[x] = scrambled[y]
    scrambled[y] = cx
}

private fun rotateLeft(steps: Int, input: MutableList<Char>): MutableList<Char> {
    var steps1 = steps
    var scrambled = input
    steps1 %= scrambled.size
    val pos = steps1
    scrambled = scrambled.subList(pos, scrambled.size).plus(scrambled.subList(0, pos)).toMutableList()
    return scrambled
}

private fun rotateRight(steps: Int, input: MutableList<Char>): MutableList<Char> {
    var actualSteps = steps
    var scrambled = input
    actualSteps %= scrambled.size
    val pos = scrambled.size - actualSteps
    scrambled = scrambled.subList(pos, scrambled.size).plus(scrambled.subList(0, pos)).toMutableList()
    return scrambled
}