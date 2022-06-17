package aoc2021b

fun main() = with(generateSequence(::readLine).toList()) {
    sequenceOf(
        reduce(String::addSnum).toSnum().magnitude(),
        flatMap { n -> filter { it != n }.map(n::addSnum) }.maxOf { it.toSnum().magnitude() }
    ).forEach(::println)
}

private fun String.addSnum(n: String) = "[$this,$n]".toSnum().run {
    generateSequence { explodeOne() || splitOne() }.first { !it }
    toString()
}

fun String.toSnum() = SnumParser(this).parsePair()

class SnumParser(private val input: String, private var pos: Int = 0) {
    fun parsePair(): SnumPair {
        read() // [
        val left = parseChild()
        read() // ,
        val right = parseChild()
        read() // ]
        return SnumPair(left, right).also {
            it.parent = it
            left.parent = it
            right.parent = it
        }
    }

    private fun parseChild() = if (peek() == '[') parsePair() else parseRegular()
    private fun parseRegular() = SnumRegular(buildString { while (peek().isDigit()) append(read()) }.toInt())
    private fun peek() = input[pos]
    private fun read() = input[pos].also { pos++ }
}

sealed class Snum {
    lateinit var parent: SnumPair
    fun parents() = generateSequence(parent) { prev -> prev.parent.takeUnless { it === prev } }.toList()
    abstract fun magnitude(): Int
    abstract fun addToLeft(n: Int)
    abstract fun addToRight(n: Int)
    fun split() = when (this) {
        is SnumRegular -> splitIfLarge()
        is SnumPair -> takeIf { it.splitOne() }
    }
}

class SnumPair(private var left: Snum, private var right: Snum) : Snum() {
    override fun toString() = "[$left,$right]"
    override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
    private fun children() = sequenceOf(left, right)

    fun explodeOne(): Boolean = if (parents().size == 4) true.also { doExplode() } else
        children().any { it is SnumPair && it.explodeOne() }

    private fun doExplode() {
        val (leftN, rightN) = (left as SnumRegular).n to (right as SnumRegular).n
        if (this == parent.left) {
            parents().firstOrNull { it != parent && it.left !in parents() }?.left?.addToRight(leftN)
            parent.left = SnumRegular(0).also { it.parent = parent }
            parent.right.addToLeft(rightN)
        } else {
            parents().firstOrNull { it != parent && it.right !in parents() }?.right?.addToLeft(rightN)
            parent.right = SnumRegular(0).also { it.parent = parent }
            parent.left.addToRight(leftN)
        }
    }

    override fun addToLeft(n: Int) = left.addToLeft(n)
    override fun addToRight(n: Int) = right.addToRight(n)

    fun splitOne(): Boolean = left.split()?.also { left = it } != null || right.split()?.also { right = it } != null
}

class SnumRegular(var n: Int) : Snum() {
    override fun toString() = "$n"
    override fun magnitude() = n

    override fun addToLeft(n: Int) {
        this.n += n
    }

    override fun addToRight(n: Int) {
        this.n += n
    }

    fun splitIfLarge() = if (n < 10) null else "[${n / 2},${n - n / 2}]".toSnum().also { it.parent = parent }
}