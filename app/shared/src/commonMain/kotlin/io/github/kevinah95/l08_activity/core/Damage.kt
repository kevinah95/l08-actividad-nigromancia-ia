package io.github.kevinah95.l08_activity.core

data class Damage(
    val amount: Int,
    val dealer: GameObject? = null
)

data class Death(
    val target: GameObject,
    val killer: GameObject? = null,
    val souls: Int = 0
)
