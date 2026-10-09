package io.github.kevinah95.l08_activity

import io.github.kevinah95.l08_activity.core.Game
import io.github.kevinah95.l08_activity.core.Tags
import io.github.kevinah95.l08_activity.entities.createCorpse
import io.github.kevinah95.l08_activity.entities.createVillager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Step3CorpseDropTest {

    @Test
    fun testCreateCorpseProperties() {
        val corpse = createCorpse()
        assertEquals(Tags.CORPSE, corpse.tags, "El cadáver debe tener la etiqueta Tags.CORPSE")
        assertEquals(Tags.NONE, corpse.collisionMask, "El cadáver no debe colisionar físicamente")
        assertTrue(corpse.mass > 0, "El cadáver debe tener masa para posarse sobre el suelo")
    }

    @Test
    fun testDyingEnemyDropsCorpse() {
        val game = Game()
        val villager = createVillager().apply {
            x = 250.0
            y = 0.0
            corpseChance = 1.0
        }
        game.spawn(villager)
        assertTrue(game.objects.contains(villager))

        // Aplicamos daño letal
        game.damage(villager, 10)

        // El aldeano debe haber desaparecido
        assertFalse(game.objects.contains(villager), "El aldeano muerto debe ser retirado con despawn()")

        // En su lugar debe existir un cadáver
        val corpse = game.objects.firstOrNull { it.isTagged(Tags.CORPSE) }
        assertTrue(corpse != null, "Debe spawnear un objeto con Tags.CORPSE al morir el enemigo")
        assertEquals(250.0, corpse.x, 0.1, "El cadáver debe spawnear en las mismas coordenadas del enemigo")
    }
}
