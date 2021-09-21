package day15

import kotlin.math.max

fun main() {
    with(input()) {
        println("max score: ${maxPoints()}, limited to 500 calories: ${maxPoints(calorieLimit = 500)}")
    }
}

private fun List<Ingredient>.maxPoints(calorieLimit: Int? = null) =
    candidates()
        .map { it.score() }
        .filter { calorieLimit == null || it.calories <= calorieLimit }
        .maxOf { it.points }

private fun List<Ingredient>.candidates() = sequence {
    for (a in 0..100) {
        for (b in 0..100) {
            for (c in 0..100) {
                for (d in 0..100) {
                    if (a + b + c + d <= 100)
                        yield(mapOf(get(0) to a, get(1) to b, get(2) to c, get(3) to d))
                }
            }
        }
    }
}

private fun Map<Ingredient, Int>.score() =
    (0..3)
        .map { n -> entries.sumOf { (i, amount) -> i.propertyValues[n] * amount } }
        .map { max(it, 0) }
        .reduce { a, b -> a * b }
        .let { Score(points = it, calories = entries.sumOf { (i, amount) -> i.calories * amount }) }

data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
) {
    val propertyValues = listOf(capacity, durability, flavor, texture)
}

data class Score(val points: Int, val calories: Int)

fun input() =
    """Frosting: capacity 4, durability -2, flavor 0, texture 0, calories 5
Candy: capacity 0, durability 5, flavor -1, texture 0, calories 8
Butterscotch: capacity -1, durability 0, flavor 5, texture 0, calories 6
Sugar: capacity 0, durability 0, flavor -2, texture 2, calories 1"""
        .split("\n")
        .map { it.split(": ") }
        .map { tokens ->
            tokens[1]
                .split(", ".toRegex())
                .associate { it.split(" ").let { p -> Pair(p[0], p[1].toInt()) } }
                .let { props ->
                    Ingredient(
                        tokens[0],
                        props["capacity"]!!,
                        props["durability"]!!,
                        props["flavor"]!!,
                        props["texture"]!!,
                        props["calories"]!!
                    )
                }
        }