package io.github.kevinah95.l08_activity.core

import kotlin.math.cos
import kotlin.math.sin

data class Point2D(val x: Double, val y: Double)

data class Rect2D(
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double
) {
    fun overlaps(other: Rect2D): Boolean {
        return x < other.x + other.width &&
               x + width > other.x &&
               y < other.y + other.height &&
               y + height > other.y
    }
}

fun vectorFromAngle(radians: Double): Point2D = Point2D(cos(radians), sin(radians))

fun clamp(value: Double, min: Double, max: Double): Double =
    if (value < min) min else if (value > max) max else value

fun clamp(value: Int, min: Int, max: Int): Int =
    if (value < min) min else if (value > max) max else value
