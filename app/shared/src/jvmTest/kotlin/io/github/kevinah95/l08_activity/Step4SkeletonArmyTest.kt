package io.github.kevinah95.l08_activity

import io.github.kevinah95.l08_activity.behaviours.March
import io.github.kevinah95.l08_activity.core.Game
import io.github.kevinah95.l08_activity.core.Tags
import io.github.kevinah95.l08_activity.entities.createCorpse
import io.github.kevinah95.l08_activity.entities.createPlayer
import io.github.kevinah95.l08_activity.entities.createSkeleton
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Step4SkeletonArmyTest {

    @Test
    fun testCreateSkeletonPropertiesAndMarchRight() {
        val skeleton = createSkeleton()
        assertTrue(skeleton.isTagged(Tags.UNDEAD), "El esqueleto debe tener etiqueta Tags.UNDEAD")
        assertTrue(skeleton.isTagged(Tags.MOBILE), "El esqueleto debe tener etiqueta Tags.MOBILE")
        assertEquals(Tags.LIVING, skeleton.collisionMask, "El esqueleto debe buscar colisión con Tags.LIVING")

        val march = skeleton.behaviours.filterIsInstance<March>().firstOrNull()
        assertTrue(march != null, "El esqueleto debe poseer el comportamiento March")
        assertTrue(march.step > 0, "El esqueleto debe marchar hacia la derecha (step positivo)")
    }

    @Test
    fun testResurrectConsumesCorpsesAndSpawnsSkeletons() {
        val game = Game()
        val player = createPlayer()
        game.player = player
        game.spawn(player, 10.0, 0.0)

        // Spawneamos 3 cadáveres
        game.spawn(createCorpse(), 100.0, 0.0)
        game.spawn(createCorpse(), 200.0, 0.0)
        game.spawn(createCorpse(), 300.0, 0.0)
        assertEquals(3, game.objects.count { it.isTagged(Tags.CORPSE) })

        // Ejecutamos resurrección
        val success = game.resurrect()
        assertTrue(success, "resurrect() debe retornar true si el cooldown está listo")

        // No deben quedar cadáveres
        assertEquals(0, game.objects.count { it.isTagged(Tags.CORPSE) }, "Todos los cadáveres deben desaparecer tras resucitar")

        // Deben existir 3 esqueletos (excluyendo a Norman que también es undead)
        val skeletons = game.objects.filter { it.isTagged(Tags.UNDEAD) && it !== player }
        assertEquals(3, skeletons.size, "Deben haberse creado 3 esqueletos a partir de los 3 cadáveres")
    }

    @Test
    fun testResurrectRespectsCooldown() {
        val game = Game()
        val player = createPlayer()
        game.player = player
        game.spawn(player, 10.0, 0.0)

        // Primer casteo exitoso
        assertTrue(game.resurrect())

        // Inmediatamente después debe fallar por cooldown
        assertFalse(game.resurrect(), "No debe permitir resucitar durante el tiempo de recarga")

        // Simulamos el paso del tiempo hasta completar el cooldown
        game.update(game.ability.cooldown)
        assertTrue(game.resurrect(), "Debe permitir volver a resucitar tras cumplirse el cooldown")
    }
}
