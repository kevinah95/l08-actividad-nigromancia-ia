package io.github.kevinah95.l08_activity.core

import io.github.kevinah95.l08_activity.entities.createCorpse
import io.github.kevinah95.l08_activity.entities.createSkeleton
import kotlin.math.abs

data class Stage(
    var width: Double = 800.0,
    var height: Double = 400.0,
    var floor: Double = 0.0,
    var ceiling: Double = 400.0
)

data class AbilityState(
    var cooldown: Double = 2000.0,
    var timer: Double = 2000.0
)

class Game : GameSession {
    val stage: Stage = Stage()
    val objects: MutableList<GameObject> = mutableListOf()
    lateinit var player: GameObject
    val ability: AbilityState = AbilityState()

    override fun spawn(gameObject: GameObject, x: Double, y: Double) {
        gameObject.x = x
        gameObject.y = y
        gameObject.gameSession = this
        objects.add(gameObject)
    }

    override fun despawn(gameObject: GameObject) {
        for (b in gameObject.behaviours.toList()) {
            gameObject.removeBehaviour(b)
        }
        objects.remove(gameObject)
    }

    fun update(dtMs: Double) {
        ability.timer += dtMs

        for (obj in objects.toList()) {
            obj.update(dtMs)
        }

        updatePhysics(dtMs)
    }

    fun updatePhysics(dtMs: Double) {
        val d = dtMs / 1000.0
        val currentObjects = objects.toList()

        for (obj in currentObjects) {
            obj.x += obj.vx * d
            obj.y += obj.vy * d

            val lower = stage.floor
            val upper = stage.ceiling - obj.spriteHeight

            if (obj.y < lower || obj.y > upper) {
                obj.y = clamp(obj.y, lower, upper)
                if (abs(obj.vy) >= 10.0) {
                    obj.onBounce()
                }
                obj.vy *= -obj.bounce
            }

            if (obj.y == lower || obj.y == upper) {
                obj.vx *= (1.0 - obj.friction)
            }

            if (obj.mass > 0 && obj.y > 0) {
                obj.vy -= obj.mass * d
            }
        }

        for (obj in currentObjects) {
            for (target in currentObjects) {
                if (obj.canCollideWith(target) && obj.bounds().overlaps(target.bounds())) {
                    obj.onCollision(target)
                }
            }
        }
    }

    fun damage(target: GameObject, amount: Int, dealer: GameObject? = null) {
        val dmg = Damage(amount, dealer)
        target.onDamage(dmg)
        target.hp = clamp(target.hp - dmg.amount, 0, target.maxHp)
        if (target.hp <= 0) {
            die(target, dealer)
        }
    }

    /**
     * TODO Paso 3.2: Gestiona la muerte de un objetivo:
     * 1. Si el objetivo tiene etiquetas Tags.LIVING o Tags.MOBILE y su corpseChance > 0:
     *    - Instanciar un cadáver con createCorpse().
     *    - Spawnear el cadáver en la posición (target.x, target.y).
     * 2. Notificar callback target.onDeath(death).
     * 3. Eliminar el objetivo del juego con despawn(target).
     */
    fun die(target: GameObject, killer: GameObject? = null) {
        val death = Death(target, killer)

        // TODO: Spawnear cadáver si corresponde antes del despawn

        target.onDeath(death)
        despawn(target)
    }

    /**
     * TODO Paso 4.2: Ejecuta el hechizo de resurrección de Norman:
     * 1. Verificar si la habilidad está lista: si ability.timer < ability.cooldown, retornar false.
     * 2. Reiniciar el temporizador: ability.timer = 0.0.
     * 3. Obtener todos los cadáveres en el juego (filtrando objects con isTagged(Tags.CORPSE)).
     * 4. Para cada cadáver:
     *    - Eliminarlo con despawn(corpse).
     *    - Crear un esqueleto con createSkeleton().
     *    - Spawnear el esqueleto en la posición X del cadáver a nivel de suelo (corpse.x, 0.0).
     * 5. Retornar true.
     */
    fun resurrect(): Boolean {
        // TODO: Implementar cooldown y resurrección masiva
        return false
    }
}
