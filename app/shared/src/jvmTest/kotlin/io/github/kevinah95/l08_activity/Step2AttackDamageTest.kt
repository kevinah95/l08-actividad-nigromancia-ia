package io.github.kevinah95.l08_activity

import io.github.kevinah95.l08_activity.behaviours.Attack
import io.github.kevinah95.l08_activity.core.Game
import io.github.kevinah95.l08_activity.core.GameObject
import kotlin.test.Test
import kotlin.test.assertEquals

class Step2AttackDamageTest {

    @Test
    fun testAttackInflictsCrossDamageOnCollision() {
        val game = Game()

        val ally = GameObject().apply {
            hp = 3
            maxHp = 3
        }
        ally.addBehaviour(Attack(ally))
        game.spawn(ally)

        val enemy = GameObject().apply {
            hp = 2
            maxHp = 2
        }
        enemy.addBehaviour(Attack(enemy))
        game.spawn(enemy)

        // Simulamos la colisión directa del aliado contra el enemigo
        ally.onCollision(enemy)

        // ally tenía 3 hp, recibe 2 de daño -> le queda 1
        assertEquals(1, ally.hp, "El atacante debe recibir daño igual a los HP del objetivo")
        // enemy tenía 2 hp, recibe 3 de daño -> le queda 0 y muere
        assertEquals(0, enemy.hp, "El objetivo debe recibir daño igual a los HP del atacante")
    }
}
