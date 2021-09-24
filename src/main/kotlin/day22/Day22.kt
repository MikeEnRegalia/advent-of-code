package day22

import kotlin.math.max

val spells = listOf(
    Spell("m", cost = 53, duration = null, damage = 4),
    Spell("d", cost = 73, duration = null, damage = 2, heal = 2),
    Spell("s", cost = 113, duration = 6, armor = 7),
    Spell("p", cost = 173, duration = 6, damage = 3),
    Spell("r", cost = 229, duration = 5, recharge = 101)
)

fun main() {
    val wizard = Player(health = 50, mana = 500)
    val boss = Player(health = 51, damage = 9)

    val min = play(wizard, boss)
    println(min)
    val minHard = play(wizard, boss, hard = true)
    println(minHard)
}

class Tracker {
    private var mana = 0
    fun registerWinningMana(mana: Int) {
        if (this.mana == 0 || mana < this.mana) {
            this.mana = mana
        }
    }

    fun isTooBig(cost: Int) = mana in 1..cost
}

fun play(
    wizard: Player,
    boss: Player,
    history: List<Spell> = listOf(),
    move: Int = 0,
    tracker: Tracker = Tracker(),
    hard: Boolean = false
): Int? {
    if (tracker.isTooBig(history.sumOf { it.cost })) return null

    val wizardsTurn = move % 2 == 0

    val wizardPre = with(wizard) { if (hard && wizardsTurn) copy(health = health - 1) else this }

    result(wizardPre, boss)?.let {
        if (it) tracker.registerWinningMana(history.sumOf { it.cost })
        return if (it) 0 else null
    }

    var (p1, p2) = wizardPre.applySpellEffects(boss)

    result(p1, p2)?.let {
        if (it) tracker.registerWinningMana(history.sumOf { it.cost })
        return if (it) 0 else null
    }

    return if (wizardsTurn) {
        // player's turn (only casts spells)
        val allowedSpells = spells
            .filter { !p1.spells.any { s -> it.name == s.name } }
            .filter { it.cost <= p1.mana }

        allowedSpells
            .mapNotNull { spell ->
                p1.cast(spell, p2)
                    .let { (w, b) ->
                        val newHistory = history.plus(spell)
                        result(w, b)?.let {
                            if (it) tracker.registerWinningMana(newHistory.sumOf { it.cost })
                            return if (it) spell.cost else null
                        }
                        play(w, b, newHistory, move + 1, tracker, hard)
                    }
                    ?.let { spell.cost + it }
            }
            .minOfOrNull { it }
    } else {
        // boss's turn (can only hit)
        p1 = p2.hit(p1)
        result(p1, p2)?.let {
            if (it) tracker.registerWinningMana(history.sumOf { it.cost })
            return if (it) 0 else null
        }
        play(p1, p2, history, move + 1, tracker, hard)
    }
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

    fun actualArmor() = armor + spells.sumOf { it.armor }

    fun hit(opponent: Player, damage: Int = this.damage) = with(opponent) {
        copy(health = health - (max(1, damage - actualArmor())))
    }

    fun applySpellEffects(opponent: Player): Pair<Player, Player> {
        var p1 = this
        var p2 = opponent
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

        return Pair(p1.copy(spells = newSpells.toSet()), p2)
    }

    fun cast(spell: Spell, opponent: Player) = with(copy(mana = mana - spell.cost)) {
        if (spell.duration != null) {
            copy(spells = spells.plus(spell)) to opponent
        } else {
            var p1 = this
            var p2 = opponent
            with(spell) {
                if (damage > 0) p2 = p1.hit(p2, damage)
                if (heal > 0) p1 = p1.copy(health = p1.health + heal)
            }
            p1 to p2
        }
    }
}

fun result(p1: Player, p2: Player) = when {
    p1.mana <= 0 || p1.health <= 0 -> false
    p2.health <= 0 -> true
    else -> null
}