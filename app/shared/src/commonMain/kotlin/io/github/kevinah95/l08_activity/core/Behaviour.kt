package io.github.kevinah95.l08_activity.core

/**
 * Clase base para componentes de comportamiento (Patrón Strategy / Componente).
 *
 * Desacopla la lógica de movimiento, IA y ataque de la clase GameObject.
 * Permite que cualquier entidad adquiera habilidades agregando comportamientos.
 */
abstract class Behaviour(val gameObject: GameObject) {
    open fun onAttach() {}
    open fun onDetach() {}
    open fun onFrame(dt: Double) {}
    open fun onUpdate(): Boolean = false
    open fun onCollision(target: GameObject) {}
    open fun onDamage(damage: Damage) {}
    open fun onDeath(death: Death) {}
}
