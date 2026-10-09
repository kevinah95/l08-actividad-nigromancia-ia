package io.github.kevinah95.l08_activity

import io.github.kevinah95.l08_activity.behaviours.March
import io.github.kevinah95.l08_activity.core.Game
import io.github.kevinah95.l08_activity.core.GameObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Step1BehaviourTest {

    @Test
    fun testMarchBehaviourMovesObjectHorizontally() {
        val game = Game()
        val unit = GameObject().apply {
            x = 100.0
            y = 0.0
            updateSpeed = 500.0
        }
        val march = March(unit, step = -10.0)
        unit.addBehaviour(march)
        game.spawn(unit)

        // update() ejecuta onUpdate() si updateClock llega a 0
        unit.update(500.0)

        assertEquals(90.0, unit.x, 0.1, "March debe mover el objeto según su parámetro step")
    }

    @Test
    fun testMarchSetsHopWhenStepping() {
        val game = Game()
        val unit = GameObject().apply {
            x = 100.0
            y = 0.0
            updateSpeed = 500.0
        }
        unit.addBehaviour(March(unit, step = 15.0))
        game.spawn(unit)

        unit.update(500.0)

        assertTrue(unit.hop > 0.0, "March debe incrementar hop para generar el salto estético al caminar")
    }
}
