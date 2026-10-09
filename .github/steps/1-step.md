# Paso 1: Patrón Behaviour y Marcha con Salto 👣

¡Bienvenido a la **Práctica 08**! En esta práctica implementarás el corazón de **Norman The Necromancer**: la arquitectura de comportamientos desacoplados de IA y la mecánica de nigromancia.

---

### 🧠 ¿Por qué usamos el Patrón Behaviour (Componente)?
En videojuegos tradicionales, la herencia rígida (`class Skeleton extends MovingEnemy extends Enemy`) genera jerarquías complejas y difíciles de mantener.
Con el **Patrón Behaviour**, desacoplamos las habilidades en componentes reutilizables:
- Un objeto puede tener `March` (se desplaza).
- Puede tener `Attack` (inflige daño al chocar).
- Y podemos añadir o remover comportamientos dinámicamente en tiempo de ejecución.

---

### 🎯 Tu Misión en este Paso

1. Abre el archivo:
   `app/shared/src/commonMain/kotlin/io/github/kevinah95/l08_activity/core/Behaviour.kt`
   Observa los métodos del ciclo de vida (`onAttach`, `onUpdate`, `onFrame`, `onCollision`, etc.).

2. Abre el archivo:
   `app/shared/src/commonMain/kotlin/io/github/kevinah95/l08_activity/behaviours/Behaviours.kt`
   Revisa la clase `March(gameObject, step)`:
   - En `onUpdate()`, incrementa la posición horizontal: `gameObject.x += step`.
   - Modifica `gameObject.hop = 2.0` para crear el saltito visual de cada paso.
   - En `onFrame(dt)`, disminuye suavemente `gameObject.hop` hacia 0.

---

### 🧪 Validación Local
Ejecuta la prueba unitaria del Paso 1:

```bash
cd app
./gradlew :shared:jvmTest --tests "*Step1BehaviourTest*"
```

---

### 🚀 Para avanzar
Haz un commit y push a la rama `main`:
```bash
git add .
git commit -m "paso-1: patrón behaviour y marcha con salto"
git push origin main
```
El bot verificará tu solución y desbloqueará el **Paso 2**.
