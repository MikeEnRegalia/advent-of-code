package day15

import kotlin.math.max

fun main() {
    val ingredients = input()
    println("max score: ${ingredients.score()}, limited to 500 calories: ${ingredients.score(500)}")
}

private fun List<Ingredient>.score(calorieLimit: Int? = null): Int {
    var max = 0

    for (a in 0..100) {
        for (b in 0..100) {
            for (c in 0..100) {
                for (d in 0..100) {
                    if (a + b + c + d > 100) continue
                    mapOf(get(0) to a, get(1) to b, get(2) to c, get(3) to d)
                        .score()
                        .let { (score, calories) ->
                            if (score > max && (calorieLimit == null || calories <= calorieLimit)) max = score
                        }
                }
            }
        }
    }
    return max
}


private fun Map<Ingredient, Int>.score(): Pair<Int, Int> {
    val capacity = entries.sumOf { it.key.capacity * it.value }
    val durability = entries.sumOf { it.key.durability * it.value }
    val flavor = entries.sumOf { it.key.flavor * it.value }
    val texture = entries.sumOf { it.key.texture * it.value }
    val calories = entries.sumOf { it.key.calories * it.value }
    return Pair(max(capacity, 0) * max(durability, 0) * max(flavor, 0) * max(texture, 0), calories)
}

data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
)

fun input() = """Frosting: capacity 4, durability -2, flavor 0, texture 0, calories 5
Candy: capacity 0, durability 5, flavor -1, texture 0, calories 8
Butterscotch: capacity -1, durability 0, flavor 5, texture 0, calories 6
Sugar: capacity 0, durability 0, flavor -2, texture 2, calories 1"""
    .split("\n")
    .map { it.split(": ").let { tokens -> Pair(tokens[0], tokens[1]) } }
    .map { (name, rest) ->
        with(rest.split(", ".toRegex()).associate { it.split(" ").let { p -> Pair(p[0], p[1].toInt()) } }) {
            Ingredient(
                name,
                get("capacity")!!,
                get("durability")!!,
                get("flavor")!!,
                get("texture")!!,
                get("calories")!!
            )
        }
    }
