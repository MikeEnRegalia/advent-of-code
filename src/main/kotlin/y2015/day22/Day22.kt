package y2015.day22

import kotlin.math.max
import kotlin.math.min

fun main() {
    val wizard = Player(health = 50, mana = 500)
    val boss = Player(health = 51, damage = 9)
    val firstTurn = Turn(wizard, boss)
    println("${Game().play(firstTurn)}, hard: ${Game(hard = true).play(firstTurn)}")
}

data class Turn(val wizard: Player, val boss: Player, val move: Int = 0, val history: List<Spell> = listOf()) {
    val wizardsTurn = move % 2 == 0
    fun next(wizard: Player = this.wizard, boss: Player = this.boss, spell: Spell? = null) =
        copy(
            move = move + 1,
            wizard = wizard,
            boss = boss,
            history = if (spell != null) history.plus(spell) else history
        )

    fun withWizard(t: (Player) -> Player) = copy(wizard = t(this.wizard))

    fun afterEffects(): Turn {
        var p1 = wizard
        var p2 = boss
        for (spell in p1.spells) {
            if (spell.damage > 0) {
                p2 = p1.hit(p2, damage = spell.damage)
            }
            if (spell.recharge > 0) {
                p1 = p1.copy(mana = p1.mana + spell.recharge)
            }
        }
        val newSpells = p1.spells
            .mapNotNull { if (it.duration == null || it.duration == 1) null else it.copy(duration = it.duration - 1) }

        return copy(wizard = p1.copy(spells = newSpells.toSet()), boss = p2)
    }

}

fun Game.play(turn: Turn): Int? =
    with(turn) {
        if (hard && wizardsTurn) withWizard { it.healed(-1) } else turn
    }.run {
        ifGameOver(this) { return it }
        afterEffects().apply { ifGameOver(this) { return it } }
    }.run {
        if (!turn.wizardsTurn) {
            // boss's turn (can only hit)
            with(withWizard { boss.hit(it) }) {
                ifGameOver(this) { return it }
                play(next())
            }
        } else {
            // player's turn (only casts spells)
            spells
                .filter { wizard.spells.none { s -> it.name == s.name } }
                .filter { it.cost <= wizard.mana }
                .filter { !isTooBig(turn.history.sumOf { it.cost }) }
                .mapNotNull { spell ->
                    wizard.cast(spell, boss)
                        .let { (wizard, boss) ->
                            val nextTurn = turn.next(wizard = wizard, boss = boss, spell)
                            ifGameOver(nextTurn, cost = spell.cost) { return it }
                            play(nextTurn)
                        }
                        ?.let { spell.cost + it }
                }
                .minOfOrNull { it }
        }
    }

data class Game(val hard: Boolean = false) {
    private var minMana: Int? = null
    fun registerWinningMana(mana: Int) {
        minMana = min(minMana ?: mana, mana)
    }

    fun isTooBig(cost: Int) = cost > (minMana ?: cost)

    val spells = listOf(
        Spell("m", cost = 53, duration = null, damage = 4),
        Spell("d", cost = 73, duration = null, damage = 2, heal = 2),
        Spell("s", cost = 113, duration = 6, armor = 7),
        Spell("p", cost = 173, duration = 6, damage = 3),
        Spell("r", cost = 229, duration = 5, recharge = 101)
    )
}

inline fun Game.ifGameOver(
    turn: Turn,
    cost: Int = 0,
    callback: (Int?) -> Unit
) {
    turn.result()?.let { wizardWon ->
        if (wizardWon) registerWinningMana(turn.history.sumOf { it.cost })
        callback(if (wizardWon) cost else null)
    }
}

fun Turn.result() = when {
    wizard.mana <= 0 || wizard.health <= 0 -> false
    boss.health <= 0 -> true
    else -> null
}

data class Spell(
    val name: String,
    val cost: Int,
    val duration: Int? = null,
    val damage: Int = 0,
    val recharge: Int = 0,
    val heal: Int = 0,
    val armor: Int = 0
) {
    override fun toString() = name
}

data class Player(
    val health: Int = 0,
    val damage: Int = 0,
    val mana: Int = 0,
    val armor: Int = 0,
    val spells: Set<Spell> = setOf()
) {
    override fun toString() = "$health/${actualArmor()} $spells"

    fun plus(spell: Spell) = copy(spells = spells.plus(spell))
    fun healed(amount: Int) = copy(health = health + amount)

    fun actualArmor() = armor + spells.sumOf { it.armor }

    fun hit(opponent: Player, damage: Int = this.damage) = with(opponent) {
        copy(health = health - (max(1, damage - actualArmor())))
    }

    fun cast(spell: Spell, opponent: Player) = with(copy(mana = mana - spell.cost)) {
        if (spell.duration != null) {
            plus(spell) to opponent
        } else {
            var p1 = this
            var p2 = opponent
            with(spell) {
                if (damage > 0) p2 = p1.hit(p2, damage)
                if (heal > 0) p1 = p1.healed(heal)
            }
            p1 to p2
        }
    }
}
