package aoc2021b

fun main() {
    data class Image(val data: List<String>, val infinitePixel: Char = '.')
    val (mapping, originalImage) = with(generateSequence(::readLine).toList()) { first() to Image(drop(2)) }

    fun Image.enhancePixel(px: Int, py: Int) = sequence {
        for (y in py - 1..py + 1)
            for (x in px - 1..px + 1) yield(data.getOrNull(y)?.getOrNull(x) ?: infinitePixel)
    }.joinToString("") { if (it == '#') "1" else "0" }.toInt(2).let { mapping[it] }

    sequence {
        var image = originalImage
        repeat(50) { n ->
            if (n == 2) yield(image)
            image = image.run {
                (-1..data.size)
                    .map { y -> (-1..data.first().length).map { x -> enhancePixel(x, y) }.joinToString("") }
                    .let { Image(it, infinitePixel = mapping[if (infinitePixel == '.') 0 else mapping.length - 1]) }
            }
        }
        yield(image)
    }.map { img -> img.data.sumOf { it.count { c -> c == '#' } } }.forEach(::println)
}