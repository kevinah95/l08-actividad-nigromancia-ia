# Paso 3: El Ciclo de la Muerte y los Cadáveres 💀

¡Excelente! Las unidades ya combaten y se destruyen. Pero para que Norman pueda resucitar esqueletos, los enemigos derrotados deben dejar atrás **sus huesos en el suelo** en lugar de simplemente desaparecer.

---

### 🧠 El Ciclo de Muerte en Norman
1. **La Calavera (`createCorpse()`):** Es un `GameObject` estático con `Tags.CORPSE` y `mass = 100.0`. Al caer un enemigo, este objeto queda en reposo sobre el suelo como recurso recolectable.
2. **El Evento `die()`:** Cuando la vida de un enemigo llega a 0 en `Game.kt`:
   - Se verifica si `target.corpseChance > 0.0`.
   - Si se cumple, se crea y spawnea un `createCorpse()` en las coordenadas del enemigo caído.
   - El enemigo se retira de la escena con `despawn(target)`.

---

### 🎯 Tu Misión en este Paso

1. Abre el archivo:
   `app/shared/src/commonMain/kotlin/io/github/kevinah95/l08_activity/entities/Entities.kt`
   Revisa la función `createCorpse()` y comprueba que esté configurada con `tags = Tags.CORPSE` y masa para permanecer en el suelo.

2. Abre el archivo:
   `app/shared/src/commonMain/kotlin/io/github/kevinah95/l08_activity/core/Game.kt`
   Revisa la función `die(target: GameObject, killer: GameObject?)`.
   Asegúrate de que genere el cadáver antes de llamar a `despawn(target)`.

---

### 🧪 Validación Local
Ejecuta la prueba unitaria del Paso 3:

```bash
cd app
./gradlew :shared:jvmTest --tests "*Step3CorpseDropTest*"
```

---

### 🚀 Para avanzar
Haz un commit y push a la rama `main`:
```bash
git add .
git commit -m "paso-3: ciclo de muerte y generación de cadáveres"
git push origin main
```
El bot verificará tu solución y desbloqueará el **Paso 4**.
