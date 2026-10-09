package io.github.kevinah95.l08_activity.core

interface GameSession {
    fun spawn(gameObject: GameObject, x: Double = gameObject.x, y: Double = gameObject.y)
    fun despawn(gameObject: GameObject)
}

open class GameObject {
    // Físicas
    var x: Double = 0.0
    var y: Double = 0.0
    var vx: Double = 0.0
    var vy: Double = 0.0
    var mass: Double = 0.0
    var bounce: Double = 0.0
    var friction: Double = 0.0
    var hop: Double = 0.0 // Salto cosmético al caminar

    // Display
    var spriteName: String = ""
    var spriteWidth: Int = 16
    var spriteHeight: Int = 16

    // Lógica
    var tags: Int = 0
    var collisionMask: Int = 0
    var hp: Int = 0
    var maxHp: Int = 0
    var corpseChance: Double = 0.0 // Probabilidad de soltar calavera al morir (0.0 a 1.0)
    var despawnOnCollision: Boolean = false
    var despawnOnBounce: Boolean = false

    // Componentes / Behaviours
    val behaviours: MutableList<Behaviour> = mutableListOf()
    var updateSpeed: Double = 0.0
    var updateClock: Double = 0.0

    // Callbacks
    var onCollisionAction: ((GameObject) -> Unit)? = null
    var onDeathAction: ((Death) -> Unit)? = null
    var onDamageAction: ((Damage) -> Unit)? = null
    var onBounceAction: (() -> Unit)? = null

    var gameSession: GameSession? = null

    fun isTagged(mask: Int): Boolean = (tags and mask) != 0

    fun canCollideWith(target: GameObject): Boolean =
        this !== target && (collisionMask and target.tags) != 0

    fun bounds(): Rect2D = Rect2D(x, y, spriteWidth.toDouble(), spriteHeight.toDouble())

    fun center(): Point2D = Point2D(x + spriteWidth / 2.0, y + spriteHeight / 2.0)

    fun addBehaviour(b: Behaviour) {
        behaviours.add(b)
        b.onAttach()
    }

    fun removeBehaviour(b: Behaviour) {
        if (behaviours.remove(b)) {
            b.onDetach()
        }
    }

    fun update(dtMs: Double) {
        for (b in behaviours.toList()) {
            b.onFrame(dtMs)
        }

        updateClock -= dtMs
        if (updateClock <= 0 && updateSpeed > 0) {
            updateClock = updateSpeed
            for (b in behaviours.toList()) {
                val cancel = b.onUpdate()
                if (cancel) break
            }
        }
    }

    open fun onCollision(target: GameObject) {
        for (b in behaviours.toList()) {
            b.onCollision(target)
        }
        onCollisionAction?.invoke(target)
        if (despawnOnCollision) {
            gameSession?.despawn(this)
        }
    }

    open fun onBounce() {
        onBounceAction?.invoke()
        if (despawnOnBounce) {
            gameSession?.despawn(this)
        }
    }

    open fun onDamage(damage: Damage) {
        for (b in behaviours.toList()) {
            b.onDamage(damage)
        }
        onDamageAction?.invoke(damage)
    }

    open fun onDeath(death: Death) {
        for (b in behaviours.toList()) {
            b.onDeath(death)
        }
        onDeathAction?.invoke(death)
    }
}
