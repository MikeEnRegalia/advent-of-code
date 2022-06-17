package aoc2021b

fun main() = Regex("""(.+)\.\.(.+)""".let { "x=$it, y=$it" }).find(readln())!!.destructured.toList().map(String::toInt)
    .let { (tLeft, tRight, tBottom, tTop) ->
        (1..tRight).flatMap { vx -> (tBottom..-tBottom).map { vx to it } }.mapNotNull { (vx0, vy0) ->
            var maxY = 0
            var hitTarget = false
            var x = 0
            var y = 0
            var vx = vx0
            var vy = vy0
            while (x <= tRight && y >= tBottom) {
                x += vx
                y += vy
                vx += 0.compareTo(vx)
                vy += -1
                if (y > maxY) maxY = y
                if (x in tLeft..tRight && y in tBottom..tTop) hitTarget = true
            }
            maxY.takeIf { hitTarget }
        }
    }.apply { println(maxOf { it }) }.run { println(size) }