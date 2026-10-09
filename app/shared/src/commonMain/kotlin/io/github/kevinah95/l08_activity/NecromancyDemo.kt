package io.github.kevinah95.l08_activity

import io.github.kevinah95.l08_activity.core.*
import io.github.kevinah95.l08_activity.entities.*
import korlibs.event.Key
import korlibs.image.bitmap.Bitmap
import korlibs.image.bitmap.Bitmap32
import korlibs.image.bitmap.sliceWithSize
import korlibs.image.color.Colors
import korlibs.image.format.readBitmap
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.Korge
import korlibs.korge.input.keys
import korlibs.korge.input.mouse
import korlibs.korge.view.*
import korlibs.math.geom.ScaleMode
import korlibs.math.geom.Size
import korlibs.render.GameWindow
import korlibs.time.*

suspend fun launchNecromancyDemo() {
    Korge(
        virtualSize = Size(800, 600),
        windowSize = Size(800, 600),
        title = "Práctica 08: Nigromancia y Comportamientos de IA",
        backgroundColor = Colors["#1e1e2e"],
        quality = GameWindow.Quality.QUALITY,
        scaleMode = ScaleMode.SHOW_ALL
    ) {
        val bitmap: Bitmap = try {
            resourcesVfs["sprites.png"].readBitmap()
        } catch (_: Throwable) {
            Bitmap32(160, 80)
        }

        val normanSlice = bitmap.sliceWithSize(16, 18, 16, 15)
        val villagerSlice = bitmap.sliceWithSize(32, 18, 14, 15)
        val skeletonSlice = bitmap.sliceWithSize(11, 33, 13, 15)
        val skullSlice = bitmap.sliceWithSize(0, 33, 8, 8)

        val game = Game()
        val player = createPlayer()
        game.player = player
        game.spawn(player, 60.0, 0.0)

        // Dejamos 2 cadáveres listos para resucitar
        val c1 = createCorpse()
        game.spawn(c1, 150.0, 0.0)
        val c2 = createCorpse()
        game.spawn(c2, 220.0, 0.0)

        // Spawneamos aldeanos marchando
        val v1 = createVillager()
        game.spawn(v1, 600.0, 0.0)
        val v2 = createVillager()
        game.spawn(v2, 700.0, 0.0)

        // UI Header
        solidRect(800.0, 60.0, Colors["#181825"])
        text("PRÁCTICA 08: NIGROMANCIA Y COMPORTAMIENTOS", textSize = 20.0, color = Colors["#f9e2af"]) {
            xy(20.0, 15.0)
        }

        // Arena
        val arena = solidRect(760.0, 420.0, Colors["#11111b"]) {
            xy(20.0, 80.0)
        }
        game.stage.width = 760.0
        game.stage.height = 420.0
        game.stage.floor = 0.0

        val objectsContainer = container()

        // UI Footer
        solidRect(760.0, 75.0, Colors["#181825"]) {
            xy(20.0, 510.0)
        }
        val infoText = text("", textSize = 14.0, color = Colors["#cdd6f4"]) {
            xy(35.0, 520.0)
        }
        val controlsText = text("Controles: [Espacio / Click] RESUCITAR ESQUELETOS (Cooldown: 2s)", textSize = 13.0, color = Colors["#a6e3a1"]) {
            xy(35.0, 550.0)
        }

        fun triggerResurrect() {
            game.resurrect()
        }

        keys {
            down(Key.SPACE) { triggerResurrect() }
            down(Key.ENTER) { triggerResurrect() }
        }

        mouse {
            down { triggerResurrect() }
        }

        addUpdater { dt ->
            game.update(dt.milliseconds)

            objectsContainer.removeChildren()
            for (obj in game.objects.toList()) {
                val slice = when {
                    obj.isTagged(Tags.CORPSE) -> skullSlice
                    obj.isTagged(Tags.UNDEAD) && obj !== player -> skeletonSlice
                    obj.isTagged(Tags.PLAYER) -> normanSlice
                    else -> villagerSlice
                }

                val screenX = arena.x + obj.x
                // Aplicamos obj.hop en el render para ver el salto del personaje
                val screenY = (arena.y + arena.height) - obj.y - (obj.spriteHeight + obj.hop) * 3.0

                objectsContainer.image(slice) {
                    xy(screenX, screenY)
                    scale(3.0)
                    // Si el esqueleto marcha a la derecha o aldeano a la izquierda
                    if (obj.isTagged(Tags.LIVING)) {
                        scaleX = -3.0
                    }
                }
            }

            val corpsesCount = game.objects.count { it.isTagged(Tags.CORPSE) }
            val skeletonsCount = game.objects.count { it.isTagged(Tags.UNDEAD) && it !== player }
            val villagersCount = game.objects.count { it.isTagged(Tags.LIVING) }
            val cdRatio = (game.ability.timer / game.ability.cooldown).coerceIn(0.0, 1.0)
            val readyStr = if (cdRatio >= 1.0) "¡LISTO!" else "${((game.ability.cooldown - game.ability.timer) / 1000.0).toString().take(3)}s"

            infoText.text = "Cadáveres: $corpsesCount  |  Esqueletos Aliados: $skeletonsCount  |  Aldeanos: $villagersCount  |  Resurrección: $readyStr"
        }
    }
}
