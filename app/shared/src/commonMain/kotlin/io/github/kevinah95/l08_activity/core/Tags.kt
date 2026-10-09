package io.github.kevinah95.l08_activity.core

/**
 * Máscaras de bits para identificar tipos de objetos en el juego.
 */
object Tags {
    const val NONE: Int = 0
    const val CORPSE: Int = 1 shl 0
    const val LIVING: Int = 1 shl 1
    const val SPELL: Int = 1 shl 2
    const val MOBILE: Int = 1 shl 3
    const val PLAYER: Int = 1 shl 4
    const val UNDEAD: Int = 1 shl 5
}
