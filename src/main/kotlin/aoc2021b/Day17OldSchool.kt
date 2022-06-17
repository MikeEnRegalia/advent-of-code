package aoc2021b

fun main(vararg args: String) {
    val (xTarget, yTarget) = args.map { it.toInt() }.let { it[0]..it[1] to it[2]..it[3] }
    var totalMaxY = 0
    var count = 0
    for (startVx in 1..xTarget.last) for (startVy in yTarget.first.let { it..-it }) {
        var maxY = 0
        var hitTarget = false
        var x = 0
        var y = 0
        var vx = startVx
        var vy = startVy
        while (x <= xTarget.last && y >= yTarget.first) {
            x += vx
            y += vy
            vx += 0.compareTo(vx)
            vy += -1
            if (y > maxY) maxY = y
            if (x in xTarget && y in yTarget) hitTarget = true
        }
        if (hitTarget) {
            count++
            if (maxY > totalMaxY) totalMaxY = maxY
        }
    }
    println(totalMaxY)
    println(count)
}