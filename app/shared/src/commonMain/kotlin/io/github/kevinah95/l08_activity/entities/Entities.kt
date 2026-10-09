package io.github.kevinah95.l08_activity.entities

import io.github.kevinah95.l08_activity.behaviours.Attack
import io.github.kevinah95.l08_activity.behaviours.Damaging
import io.github.kevinah95.l08_activity.behaviours.March
import io.github.kevinah95.l08_activity.core.*

fun createPlayer(): GameObject {
    return GameObject().apply {
        x = 20.0
        y = 0.0
        spriteName = "norman_idle"
        spriteWidth = 16
        spriteHeight = 15
        tags = Tags.PLAYER or Tags.UNDEAD
        collisionMask = Tags.LIVING
        hp = 5
        maxHp = 5
    }
}

fun createSpell(): GameObject {
    val obj = GameObject().apply {
        spriteName = "spell_skull"
        spriteWidth = 4
        spriteHeight = 4
        tags = Tags.SPELL
        collisionMask = Tags.LIVING
        mass = 100.0
        friction = 0.1
        despawnOnCollision = true
        despawnOnBounce = true
    }
    obj.addBehaviour(Damaging(obj, 1))
    return obj
}

fun createVillager(): GameObject {
    val obj = GameObject().apply {
        x = 500.0
        y = 0.0
        spriteName = "villager"
        spriteWidth = 14
        spriteHeight = 15
        tags = Tags.LIVING or Tags.MOBILE
        collisionMask = Tags.PLAYER or Tags.UNDEAD or Tags.SPELL
        hp = 2
        maxHp = 2
        corpseChance = 1.0 // En las prácticas garantizamos calaveras para probar la resurrección
        updateSpeed = 800.0
    }
    obj.addBehaviour(March(obj, step = -15.0))
    obj.addBehaviour(Attack(obj))
    return obj
}

/**
 * TODO Paso 3.1: Crea un cadáver de calavera dejado en el suelo tras la muerte de un enemigo:
 * - spriteName = "skull"
 * - spriteWidth = 8, spriteHeight = 7
 * - mass = 100.0 (para caer al suelo si el enemigo muere en el aire)
 * - tags = Tags.CORPSE
 * - collisionMask = Tags.NONE (los cadáveres no chocan con proyectiles ni unidades)
 */
fun createCorpse(): GameObject {
    return GameObject().apply {
        spriteName = "skull"
        spriteWidth = 8
        spriteHeight = 7
        mass = 100.0
        tags = Tags.CORPSE
        collisionMask = Tags.NONE
    }
}

/**
 * TODO Paso 4.1: Crea un guerrero esqueleto resucitado por la magia de Norman:
 * - spriteName = "skeleton"
 * - spriteWidth = 13, spriteHeight = 15
 * - tags = Tags.UNDEAD or Tags.MOBILE
 * - collisionMask = Tags.LIVING
 * - hp = 2, maxHp = 2
 * - updateSpeed = 600.0
 * - Comportamientos:
 *   - March(obj, step = 15.0) -> Avanza hacia la DERECHA al encuentro de los invasores.
 *   - Attack(obj) -> Inflige y recibe daño al impactar con aldeanos vivos.
 */
fun createSkeleton(): GameObject {
    val obj = GameObject().apply {
        spriteName = "skeleton"
        spriteWidth = 13
        spriteHeight = 15
        tags = Tags.UNDEAD or Tags.MOBILE
        collisionMask = Tags.LIVING
        hp = 2
        maxHp = 2
        updateSpeed = 600.0
    }
    obj.addBehaviour(March(obj, step = 15.0))
    obj.addBehaviour(Attack(obj))
    return obj
}
