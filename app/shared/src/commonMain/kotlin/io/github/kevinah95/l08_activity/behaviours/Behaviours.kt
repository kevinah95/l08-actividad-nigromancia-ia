package io.github.kevinah95.l08_activity.behaviours

import io.github.kevinah95.l08_activity.core.Behaviour
import io.github.kevinah95.l08_activity.core.Game
import io.github.kevinah95.l08_activity.core.GameObject

/**
 * Comportamiento de Marcha:
 * Desplaza la unidad horizontalmente por pasos en cada ciclo de actualización.
 *
 * TODO Paso 1.1:
 * - En `onUpdate()`, si el objeto está en el suelo (`y <= 0`), desplaza su coordenada X: `gameObject.x += step`.
 * - Establece `gameObject.hop = 2.0` para simular el salto del paso.
 * - En `onFrame(dt)`, si `hop > 0`, reduce gradualmente `hop` hacia 0.
 */
class March(gameObject: GameObject, var step: Double) : Behaviour(gameObject) {
    override fun onFrame(dt: Double) {
        // TODO: Reducir suavemente gameObject.hop hacia 0
    }

    override fun onUpdate(): Boolean {
        // TODO: Incrementar gameObject.x según step y asignar hop = 2.0
        return false
    }
}

/**
 * Comportamiento de Ataque Cuerpo a Cuerpo:
 * Al chocar con un objetivo hostil, ambas unidades se infligen daño mutuo equivalente a su salud restante.
 *
 * TODO Paso 2.1:
 * - En `onCollision(target)`, obtiene la sesión de juego: val session = gameObject.gameSession as? Game ?: return
 * - Inflige daño al objetivo igual a la vida del atacante: `session.damage(target, gameObject.hp, gameObject)`.
 * - Inflige daño al atacante igual a la vida del objetivo: `session.damage(gameObject, target.hp, target)`.
 */
class Attack(gameObject: GameObject) : Behaviour(gameObject) {
    override fun onCollision(target: GameObject) {
        // TODO: Aplicar daño mutuo entre atacante y objetivo
    }
}

class Damaging(gameObject: GameObject, var amount: Int = 1) : Behaviour(gameObject) {
    override fun onCollision(target: GameObject) {
        val session = gameObject.gameSession as? Game ?: return
        session.damage(target, amount, gameObject)
    }
}
