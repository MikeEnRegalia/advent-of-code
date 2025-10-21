package ec2024

fun main() {
    val squires = generateSequence(::readLine).map {
        it.split(":").let { (squire, plan) -> squire to plan.split(",") }
    }.toList()

    fun List<String>.score(rounds: Int, track: List<String>? = null): Int {
        var power = 10
        var sum = 0

        repeat((track?.size ?: 1) * rounds) { i ->
            val track = track?.get(i % track.size).takeIf { it == "-" || it == "+" }
            when (track ?: this[i % size]) {
                "+" -> power += 1
                "-" -> power -= 1
            }
            sum += power
        }
        return sum
    }

    println(squires.map { (squire, plan) -> squire to plan.score(10) }.sortedByDescending { it.second }
        .joinToString("") { it.first })

    val knightsTrack = "-=++=-==++=++=-=+=-=+=+=--=-=++=-==++=-+=-=+=-=+=+=++=-+==++=++=-=-=--" +
            "-=++==-" +
            "--==++++==+=+++-=+=-=+=-+-=+-=+-=+=-=+=--=+++=++=+++==++==--=+=++==+++-".reversed() +
            "-=+=+=-="

    println(squires.map { (squire, plan) -> squire to plan.score(10, knightsTrack.map { it.toString() }) }
        .sortedByDescending { it.second }.joinToString("") { it.first })

    val finalTrack = buildList {

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
            add(finalTrackMap[curr.y][curr.x].toString())
        }
    }

    fun String.permute(result: String = ""): List<String> =
        if (isEmpty()) listOf(result) else flatMapIndexed { i, c -> removeRange(i, i + 1).permute(result + c) }

    var winningPlans = 0

    for (plan in "+++++---===".permute().toSet()) {
        val result = (squires + ("@" to plan.map { it.toString() }))
            .associate { (squire, plan) -> squire to plan.score(2024, finalTrack) }
        val score = result.getValue("@")
        if (result.all { it.key == "@" || it.value < score }) winningPlans++
    }
    println(winningPlans)
}
