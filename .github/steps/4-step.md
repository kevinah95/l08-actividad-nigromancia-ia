# Paso 4: El Gran Hechizo de Resurrección 🧙‍♂️

¡Llegamos al clímax de la práctica! Implementarás la habilidad insignia de Norman: levantar un ejército de muertos vivientes a partir de los cadáveres acumulados en el campo de batalla.

---

### 🧠 ¿Cómo funciona la resurrección masiva?
1. **Verificación de Cooldown:** Si `ability.timer < ability.cooldown`, la habilidad aún está recargándose y no se puede usar. Retorna `false`.
2. **Reinicio de Cooldown:** Se resetea `ability.timer = 0.0`.
3. **Conversión de Cadáveres:**
   - Se buscan todos los objetos con `Tags.CORPSE`: `val corpses = objects.filter { it.isTagged(Tags.CORPSE) }`.
   - Por cada cadáver, se retira con `despawn(corpse)`.
   - Se instancia un esqueleto aliado con `createSkeleton()`.
   - Se spawnea en la misma coordenada horizontal del cadáver a nivel de suelo (`spawn(skeleton, corpse.x, 0.0)`).
4. **Comportamiento del Esqueleto:** El esqueleto posee `March(step = 15.0)` hacia la derecha y `Attack()` para combatir contra los aldeanos.

---

### 🎯 Tu Misión en este Paso

1. Abre el archivo:
   `app/shared/src/commonMain/kotlin/io/github/kevinah95/l08_activity/entities/Entities.kt`
   Revisa `createSkeleton()` para asegurar que tenga los comportamientos `March(step = 15.0)` y `Attack()`, con `collisionMask = Tags.LIVING`.

2. Abre el archivo:
   `app/shared/src/commonMain/kotlin/io/github/kevinah95/l08_activity/core/Game.kt`
   Revisa la función `resurrect(): Boolean`.
   Verifica que gestione el enfriamiento y transforme cada cadáver en un esqueleto combatiente.

---

### 🧪 Validación Local
Ejecuta la prueba unitaria del Paso 4:

```bash
cd app
./gradlew :shared:jvmTest --tests "*Step4SkeletonArmyTest*"
```

Para verificar todas las pruebas de la práctica:
```bash
cd app
./gradlew :shared:jvmTest
```

---

### 🚀 Para finalizar
Haz un commit y push a la rama `main`:
```bash
git add .
git commit -m "paso-4: gran hechizo de resurrección y esqueletos"
git push origin main
```
El bot verificará tu solución, publicará la felicitación final y cerrará el issue.
