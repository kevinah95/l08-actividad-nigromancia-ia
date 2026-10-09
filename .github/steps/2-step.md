# Paso 2: Choque de Tropas y Combate Melee ⚔️

¡Gran trabajo con la marcha! Ahora conectaremos el sistema de colisiones para que las unidades que colisionan en el campo de batalla intercambien daño cuerpo a cuerpo.

---

### 🧠 ¿Cómo funciona el comportamiento `Attack`?
Cuando dos unidades marchan en direcciones opuestas y se encuentran:
1. Se dispara `onCollision(target)` en ambos objetos.
2. Cada atacante calcula el daño infligido basado en su salud restante: `val dealDamage = gameObject.hp`.
3. Se invoca `session.damage(target, dealDamage, gameObject)` y viceversa.
4. Si la vida de una unidad llega a 0, se activa el evento de muerte.

---

### 🎯 Tu Misión en este Paso

1. Abre el archivo:
   `app/shared/src/commonMain/kotlin/io/github/kevinah95/l08_activity/behaviours/Behaviours.kt`

2. Revisa la clase `Attack(gameObject: GameObject)`.
   Asegúrate de que en `onCollision(target)` se obtenga la sesión de juego y se aplique el daño mutuo entre atacante y defensor.

---

### 🧪 Validación Local
Ejecuta la prueba unitaria del Paso 2:

```bash
cd app
./gradlew :shared:jvmTest --tests "*Step2AttackDamageTest*"
```

---

### 🚀 Para avanzar
Haz un commit y push a la rama `main`:
```bash
git add .
git commit -m "paso-2: choque de tropas y combate melee"
git push origin main
```
El bot verificará tu solución y desbloqueará el **Paso 3**.
