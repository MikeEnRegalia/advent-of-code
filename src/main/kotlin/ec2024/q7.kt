package ec2024

fun main() {
    data class Runner(val name: String, val plan: String)

    val runners = generateSequence(::readLine).map {
        it.split(":").let { (name, plan) -> Runner(name, plan.split(",").joinToString("")) }
    }.toList()

    fun String.score(rounds: Int, track: String? = null): Int {
        var power = 10
        var sum = 0

        repeat((track?.length ?: 1) * rounds) { i ->
            val track = track?.get(i % track.length).takeIf { it == '-' || it == '+' }
            when (track ?: this[i % length]) {
                '+' -> power += 1
                '-' -> power -= 1
            }
            sum += power
        }
        return sum
    }

    data class Result(val name: String, val score: Int)

    println(runners.map { Result(it.name, it.plan.score(10)) }.sortedByDescending { it.score }
        .joinToString("") { it.name })

    val knightsTrack = "-=++=-==++=++=-=+=-=+=+=--=-=++=-==++=-+=-=+=-=+=+=++=-+==++=++=-=-=--" +
            "-=++==-" +
            "--==++++==+=+++-=+=-=+=-+-=+-=+-=+=-=+=--=+++=++=+++==++==--=+=++==+++-".reversed() +
            "-=+=+=-="

    println(runners.map { Result(it.name, it.plan.score(10, knightsTrack)) }
        .sortedByDescending { it.score }.joinToString("") { it.name })

    val finalTrack = buildString {

        val finalTrackMap = """
        S+= +=-== +=++=     =+=+=--=    =-= ++=     +=-  =+=++=-+==+ =++=-=-=--
        - + +   + =   =     =      =   == = - -     - =  =         =-=        -
        = + + +-- =-= ==-==-= --++ +  == == = +     - =  =    ==++=    =++=-=++
        + + + =     +         =  + + == == ++ =     = =  ==   =   = =++=
        = = + + +== +==     =++ == =+=  =  +  +==-=++ =   =++ --= + =
        + ==- = + =   = =+= =   =       ++--          +     =   = = =--= ==++==
        =     ==- ==+-- = = = ++= +=--      ==+ ==--= +--+=-= ==- ==   =+=    =
        -               = = = =   +  +  ==+ = = +   =        ++    =          -
        -               = + + =   +  -  = + = = +   =        +     =          -
        --==++++==+=+++-= =-= =-+-=  =+-= =-= =--   +=++=+++==     -=+=++==+++-
        """.trimIndent().split("\n")

        data class Pos(val x: Int, val y: Int)

        val visited = mutableSetOf<Pos>()
        var curr = Pos(1, 0)
        append("+")

        fun Pos.next(): Pos? = if (this == Pos(1, 0)) Pos(2, 0) else sequenceOf(
            copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1)
        ).singleOrNull {
            it !in visited &&
                    it.y in finalTrackMap.indices &&
                    it.x in finalTrackMap[it.y].indices &&
                    finalTrackMap[it.y][it.x] !in " "
        }

        while (true) {
            visited += curr
            curr = curr.next() ?: break
            append(finalTrackMap[curr.y][curr.x].toString())
        }
    }

    fun String.permute(result: String = ""): Set<String> = when {
        isEmpty() -> setOf(result)
        else -> mapIndexed { i, c -> c to removeRange(i, i + 1) }.toSet()
            .flatMap { (c, rest) -> rest.permute(result + c) }
            .toSet()
    }

    val maxOtherScores = runners.map { it.plan }.maxOf { it.score(2024, finalTrack) }

    "+++++---===".permute().parallelStream()
        .filter { it.score(2024, finalTrack) > maxOtherScores }
        .count()
        .also(::println)
}
