# Lección 08: Patrón Behaviour, IA de Combate y Mecánica de Nigromancia
> **Curso:** Desarrollo de Videojuegos 2D con Kotlin Multiplatform & KorGE  
> **Proyecto:** Norman The Necromancer  
> **Documento Fuente para Google NotebookLM** (Presentaciones, Estudio y Audio Overview)

---

## 📋 Ficha Técnica de la Lección

- **Lección:** 08 de 12
- **Tema:** Arquitectura basada en componentes (Patrón `Behaviour` / *Composición sobre Herencia*), cinemática de marcha por turnos con interpolación de salto (`hop`), combate cuerpo a cuerpo bidireccional (`Attack`), ciclo de muerte con generación de cadáveres (`CORPSE`) y resurrección masiva de esqueletos aliados (`UNDEAD`) con tiempo de enfriamiento (*cooldown*).
- **Prerrequisito:** Lección 07 (Físicas 2D por integración de Euler, detección geométrica AABB, máscaras de bits y combate balístico).
- **Siguiente Lección:** Lección 09 (Tienda de rituales, efectos de estado, progresión de 10 oleadas y batalla contra El Rey).
- **Pila Tecnológica:** Kotlin Multiplatform (KMP), KorGE, Gradle, JVM Desktop.
- **Objetivo Central:** Que los estudiantes evolucionen el motor de físicas de la Lección 07 hacia una arquitectura modular de componentes de IA reutilizables, dotando a las entidades de marcha autónoma, choque de tropas cuerpo a cuerpo, persistencia de cadáveres en el escenario y la mecánica insignia del juego: levantar un ejército de esqueletos a partir de los enemigos caídos.

---

## 🎯 Objetivos de Aprendizaje (Taxonomía de Bloom)

1. **Comprender (Composición sobre Herencia):** Explicar por qué los motores de videojuegos modernos desacoplan las capacidades de una entidad en componentes independientes (`Behaviour`) en lugar de crear jerarquías de herencia rígidas y profundas.
2. **Aplicar (Ciclo de Vida Dual Frame vs. Turno):** Distinguir y programar la diferencia entre actualizaciones continuas por fotograma (`onFrame(dt)`) y actualizaciones discretas por reloj de acción (`onUpdate()` gobernado por `updateClock` y `updateSpeed`).
3. **Construir (Combate Melee y Persistencia de Muerte):** Implementar el intercambio de daño simultáneo en colisiones cuerpo a cuerpo (`Attack`) y la transición de una unidad viva (`Tags.LIVING`) hacia un recurso recolectable en el suelo (`Tags.CORPSE`) al morir (`die()`).
4. **Integrar (Mecánica de Nigromancia y Cooldown):** Programar el hechizo `resurrect()` gestionando su temporizador de enfriamiento (`AbilityState`), filtrando los cadáveres activos en escena y transmutándolos en guerreros esqueletos aliados (`Tags.UNDEAD`) que marchan en dirección contraria.

---

## 🗺️ Mapa de Arquitectura del Motor (Lección 08)

```
app/shared/src/commonMain/kotlin/.../
├── core/
│   ├── Behaviour.kt     ← Paso 1: Clase base abstracta para componentes de IA
│   ├── GameObject.kt    ← Paso 1: Contenedor de behaviours, updateClock y hop
│   ├── Game.kt          ← Paso 3 & 4: die() (drop de cadáver) y resurrect() (nigromancia)
│   ├── Tags.kt          ← Etiquetas binarias (CORPSE, LIVING, UNDEAD, MOBILE, PLAYER)
│   ├── Geometry.kt      ← Cajas AABB (Rect2D) heredadas de la Lección 07
│   └── Damage.kt        ← Eventos estructurados Damage y Death
├── behaviours/
│   └── Behaviours.kt    ← Paso 1 & 2: March (avance + hop) y Attack (daño mutuo)
├── entities/
│   └── Entities.kt      ← Paso 3 & 4: createCorpse(), createSkeleton(), createVillager()
└── NecromancyDemo.kt    ← Escena interactiva en KorGE con choque de tropas y resurrección
```

---

## 📽️ Estructura Completa de la Presentación (Diapositiva por Diapositiva)

A continuación se detalla la presentación completa lista para ser explicada en clase o utilizada por NotebookLM para generar diapositivas en Google Slides / PowerPoint.

---

### Diapositiva 1: Portada y Gancho
- **Título:** Lección 08: Patrón Behaviour, IA de Combate y Nigromancia
- **Subtítulo:** Dándole mente, combate cuerpo a cuerpo y artes oscuras a *Norman The Necromancer*
- **Puntos Clave:**
  - En la Lección 07 dimos leyes físicas al mundo: gravedad, colisiones AABB y proyectiles.
  - Hoy daremos **autonomía e inteligencia** a las criaturas del campo de batalla.
  - Aprenderemos 4 pilares fundamentales del diseño de videojuegos:
    1. El Patrón `Behaviour` (*Composición sobre Herencia*).
    2. Cinemática de marcha por pasos (`March`) y feedback visual (`hop`).
    3. Combate cuerpo a cuerpo bidireccional (`Attack`).
    4. El ciclo de la Nigromancia: de aldeano vivo a calavera (`CORPSE`) y a esqueleto aliado (`UNDEAD`).
- **Notas del Orador (Speaker Notes):**
  > "Bienvenidos a la Lección 8. En la clase pasada logramos que Norman disparara cráneos mágicos y derribara aldeanos. Pero esos aldeanos eran blancos estáticos y, cuando morían, simplemente desaparecían. Un nigromante sin ejército no es un verdadero nigromante. Hoy vamos a programar la arquitectura de inteligencia artificial por componentes que permite que los aldeanos marchen hacia Norman, que al morir dejen sus huesos en el campo de batalla, y que Norman pueda invocar un ejército de esqueletos que marche y luche por él."

---

### Diapositiva 2: El Problema de la Herencia Rígida en Videojuegos
- **Título:** ¿Por qué fracasa la Herencia Clásica en los Videojuegos?
- **Puntos Clave:**
  - **El enfoque ingenuo (Herencia):**
    `GameObject` $\rightarrow$ `Enemy` $\rightarrow$ `MovingEnemy` $\rightarrow$ `MeleeMovingEnemy`
  - **¿Qué pasa cuando el diseño crece?**
    - ¿Y si queremos un esqueleto aliado que se mueva y ataque igual que un enemigo?
    - ¿Y si una unidad queda congelada (`Frozen`) por 3 segundos y no debe caminar?
    - ¿Y si un proyectil también necesita hacer daño al chocar, pero no camina?
  - **El problema del Diamante y la Explosión Combinatoria:** La herencia acopla quién *es* el objeto con qué *hace* el objeto.
  - **La solución de la industria (Unity `MonoBehaviour`, Godot `Node`):** **Composición sobre Herencia**. Un solo `GameObject` genérico al que le conectamos piezas de comportamiento (`Behaviour`).
- **Diagrama Comparativo:**
  ```
  ❌ Herencia Rígida:                 ✅ Composición (Patrón Behaviour):
     GameObject                          GameObject ("Aldeano" o "Esqueleto")
       └── MovingUnit                      ├── March(step = ±15.0)
             └── MeleeUnit                 └── Attack()
  ```
- **Notas del Orador:**
  > "Pregúntenle a cualquier desarrollador de videojuegos qué pasa cuando intentan modelar enemigos con herencia tradicional: a la tercera semana tienen un árbol de clases imposible de mantener. Con composición, un Aldeano y un Esqueleto son exactamente la misma clase `GameObject`; la única diferencia son sus etiquetas binarias y la dirección del componente `March` que llevan adjunto."

---

### Diapositiva 3: Pilar 1 - La Arquitectura `Behaviour` y su Ciclo de Vida
- **Título:** Diseñando el Componente `Behaviour`
- **Puntos Clave:**
  - Cada `GameObject` posee una lista dinámica: `val behaviours = mutableListOf<Behaviour>()`.
  - La clase abstracta `Behaviour` define *hooks* (ganchos) para cada evento del motor:
    - `onAttach()` / `onDetach()`: Al agregar o quitar el comportamiento dinámicamente.
    - `onFrame(dt: Double)`: Se ejecuta en **cada fotograma** (60 veces por segundo) para animaciones suaves o temporizadores continuos.
    - `onUpdate(): Boolean`: Se ejecuta por **pulsos o turnos** cada vez que `updateClock` llega a 0 (ej. dar un paso cada `600 ms` u `800 ms`).
    - `onCollision(target)`: Al solaparse con una entidad válida según su `collisionMask`.
    - `onDamage(damage)` / `onDeath(death)`: Para reaccionar al daño recibido o a la muerte.
- **Código Clave en `GameObject.kt`:**
  ```kotlin
  fun update(dtMs: Double) {
      for (b in behaviours.toList()) {
          b.onFrame(dtMs)
      }
      updateClock -= dtMs
      if (updateClock <= 0 && updateSpeed > 0) {
          updateClock = updateSpeed
          for (b in behaviours.toList()) {
              if (b.onUpdate()) break // Permite cancelar comportamientos posteriores (ej. Frozen)
          }
      }
  }
  ```
- **Notas del Orador:**
  > "Observen dos detalles brillantes en `GameObject.update()`: primero, iteramos sobre `behaviours.toList()` (una copia superficial) para evitar un `ConcurrentModificationException` si un comportamiento se remueve a sí mismo o despawnea la unidad durante la iteración. Segundo, si `b.onUpdate()` devuelve `true`, interrumpe la cadena: así es como en la Lección 9 implementaremos el estado Congelado (`Frozen`) sin tocar una sola línea de `March`."

---

### Diapositiva 4: Pilar 1 (Cont.) - El Comportamiento `March` y el Salto Estético (`hop`)
- **Título:** Paso 1: Caminar con Estilo (`March` y `hop`)
- **Puntos Clave:**
  - En *Norman The Necromancer*, las unidades no se deslizan como patinadores sobre hielo: avanzan dando pequeños pasos rítmicos cada vez que su `updateClock` expira.
  - **En `onUpdate()` (cada pulso de marcha):**
    1. Si la unidad está en el aire (`gameObject.y > 0`), no puede marchar.
    2. Avanza horizontalmente: `gameObject.x += step` (`step < 0` hacia la izquierda para aldeanos; `step > 0` hacia la derecha para esqueletos).
    3. Eleva el desplazamiento cosmético `gameObject.hop = 2.0` para simular el saltito del paso.
    4. Si sale de los límites del escenario (`x < 0` o `x > stageWidth`), hace `despawn`.
  - **En `onFrame(dt)` (cada fotograma):**
    - Reduce suavemente `hop` de vuelta a `0.0`:
      `gameObject.hop = (gameObject.hop - (dt / 100.0)).coerceAtLeast(0.0)`
- **Código Clave (`Behaviours.kt`):**
  ```kotlin
  class March(gameObject: GameObject, var step: Double) : Behaviour(gameObject) {
      override fun onFrame(dt: Double) {
          if (gameObject.hop > 0) {
              gameObject.hop = (gameObject.hop - (dt / 100.0)).coerceAtLeast(0.0)
          }
      }
      override fun onUpdate(): Boolean {
          if (gameObject.y > 0) return false
          gameObject.x += step
          gameObject.hop = 2.0
          return false
      }
  }
  ```
- **Notas del Orador:**
  > "¿Por qué separamos `hop` de la coordenada física `y`? Porque si usáramos `y` para el saltito visual de caminar, la unidad dejaría de tocar el suelo a cada paso y podría esquivar proyectiles por accidente o alterar su hitbox. `hop` es puramente estético: el renderizador lo suma al dibujar el sprite, manteniendo la física 100% predecible."

---

### Diapositiva 5: Pilar 2 - Choque de Tropas y Combate Cuerpo a Cuerpo (`Attack`)
- **Título:** Paso 2: Combate Melee y Daño Mutuo Simultáneo
- **Puntos Clave:**
  - Cuando un Esqueleto aliado (`UNDEAD`) y un Aldeano (`LIVING`) se encuentran en el campo de batalla, sus cajas AABB se solapan y se dispara `onCollision(target)`.
  - **Regla de diseño táctico:** En el choque cuerpo a cuerpo, ambas unidades se golpean mutuamente con una fuerza equivalente a sus puntos de vida actuales (`hp`).
    - Ejemplo: Si un aliado con `3 HP` choca contra un enemigo de `2 HP`, el enemigo recibe `3` de daño (queda en `0 HP` y muere) y el aliado recibe `2` de daño (sobrevive con `1 HP`).
  - **¡Cuidado con el orden de ejecución (Bug clásico)!**
    - Si llamamos primero a `session.damage(target, gameObject.hp, gameObject)`, ¡la vida `target.hp` baja inmediatamente a `0`!
    - Si en la línea siguiente usamos `target.hp`, el atacante recibiría `0` de daño.
    - **Solución:** Capturar `val dealDamage = gameObject.hp` y `val takeDamage = target.hp` **antes** de aplicar el daño.
- **Código Clave (`Behaviours.kt`):**
  ```kotlin
  class Attack(gameObject: GameObject) : Behaviour(gameObject) {
      override fun onCollision(target: GameObject) {
          val dealDamage = gameObject.hp
          val takeDamage = target.hp
          val session = gameObject.gameSession as? Game ?: return
          session.damage(target, dealDamage, gameObject)
          session.damage(gameObject, takeDamage, target)
      }
  }
  ```
- **Notas del Orador:**
  > "Este es uno de los errores de estado mutable más instructivos en programación de videojuegos. En una colisión simultánea, debemos tomar una 'fotografía' de los puntos de vida de ambos combatientes antes de ejecutar las mutaciones de daño."

---

### Diapositiva 6: Pilar 3 - El Ciclo de la Muerte y los Cadáveres (`CORPSE`)
- **Título:** Paso 3: De Enemigo Derrotado a Recurso Nigromántico
- **Puntos Clave:**
  - En la Lección 07, cuando `target.hp <= 0`, el método `die()` simplemente hacía `despawn(target)`.
  - Ahora, la muerte de un enemigo alimenta la economía principal del jugador: **los cadáveres**.
  - **Anatomía de un Cadáver (`createCorpse()` en `Entities.kt`):**
    - `spriteName = "skull"` ($8 \times 7$ px).
    - `tags = Tags.CORPSE` (bit 0).
    - `collisionMask = Tags.NONE` (es inerte: no bloquea proyectiles ni frena a otras unidades).
    - `mass = 100.0` (si el enemigo muere en el aire, la calavera cae por gravedad hasta posarse en el suelo).
  - **Lógica en `Game.die(target, killer)`:**
    1. Verificar si la víctima es una criatura (`Tags.LIVING` o `Tags.MOBILE`) y tiene probabilidad de dejar cadáver (`target.corpseChance > 0.0`).
    2. Instanciar `createCorpse()` y hacer `spawn(corpse, target.x, target.y)` en las coordenadas exactas donde cayó.
    3. Notificar `target.onDeath(death)` y retirar al enemigo con `despawn(target)`.
- **Diagrama de Transición de Entidad:**
  ```
  [Aldeano: Tags.LIVING | hp = 2]
         │
         ▼ (Recibe daño letal -> hp == 0)
     Game.die()
         ├──> spawn(createCorpse(), target.x, target.y)  ──> [Calavera: Tags.CORPSE en suelo]
         └──> despawn(villager)
  ```
- **Notas del Orador:**
  > "Noten cómo reutilizamos todo lo que construimos en la Lección 07: la calavera tiene `mass = 100.0` y `collisionMask = Tags.NONE`. Eso significa que nuestro motor de físicas ya sabe cómo hacerla caer al suelo sin que tengamos que escribir código especial para cadáveres."

---

### Diapositiva 7: Pilar 4 - El Gran Hechizo de Resurrección (`resurrect()`)
- **Título:** Paso 4: ¡Levantaos, Esqueletos! (`resurrect()` y Cooldown)
- **Puntos Clave:**
  - Norman posee una habilidad especial gobernada por `AbilityState(cooldown = 2000.0, timer = 2000.0)`.
  - En cada frame (`Game.update(dtMs)`), el temporizador se recarga: `ability.timer += dtMs`.
  - **Algoritmo de `resurrect(): Boolean` en `Game.kt`:**
    1. **Validar Cooldown:** Si `ability.timer < ability.cooldown`, la habilidad sigue en enfriamiento $\rightarrow$ retorna `false`.
    2. **Consumir Cooldown:** Reinicia `ability.timer = 0.0`.
    3. **Buscar Cadáveres:** Filtra todas las entidades en escena con la máscara `Tags.CORPSE`:
       `val corpses = objects.filter { it.isTagged(Tags.CORPSE) }`
    4. **Transmutación Masiva:** Por cada `corpse` en `corpses`:
       - Lo retira del suelo con `despawn(corpse)`.
       - Crea un guerrero esqueleto con `createSkeleton()`.
       - Lo invoca en la misma coordenada horizontal a nivel de suelo: `spawn(skeleton, corpse.x, 0.0)`.
    5. Retorna `true`.
- **Código Clave (`Game.kt`):**
  ```kotlin
  fun resurrect(): Boolean {
      if (ability.timer < ability.cooldown) return false
      ability.timer = 0.0

      val corpses = objects.filter { it.isTagged(Tags.CORPSE) }
      for (corpse in corpses) {
          despawn(corpse)
          val skeleton = createSkeleton()
          spawn(skeleton, corpse.x, 0.0)
      }
      return true
  }
  ```
- **Notas del Orador:**
  > "Aquí se cierra el bucle de juego (*Core Gameplay Loop*) de Norman The Necromancer: derrotas invasores, acumulas calaveras en el campo de batalla y, en el momento táctico oportuno, activas la resurrección para levantar a todos los esqueletos a la vez e invertir el curso de la batalla."

---

### Diapositiva 8: Espejo Táctico - Aldeano vs. Esqueleto
- **Título:** Simetría de Datos: Cómo dos bandos opuestos comparten el mismo código
- **Puntos Clave:**
  - Comparación lado a lado en `Entities.kt`:
    | Propiedad / Componente | Aldeano (`createVillager`) | Esqueleto (`createSkeleton`) |
    | :--- | :--- | :--- |
    | **Etiquetas (`tags`)** | `Tags.LIVING or Tags.MOBILE` | `Tags.UNDEAD or Tags.MOBILE` |
    | **Objetivo (`collisionMask`)** | `PLAYER or UNDEAD or SPELL` | `Tags.LIVING` |
    | **Salud (`hp` / `maxHp`)** | `2 / 2` | `2 / 2` |
    | **Velocidad de turno (`updateSpeed`)** | `800.0 ms` | `600.0 ms` (más ágil) |
    | **Marcha (`March`)** | `step = -15.0` (Izquierda $\leftarrow$) | `step = +15.0` (Derecha $\rightarrow$) |
    | **Combate (`Attack`)** | `Attack(obj)` | `Attack(obj)` |
    | **Deja cadáver (`corpseChance`)** | `1.0` (Sí deja calavera) | `0.0` (No deja calavera al morir) |
- **Notas del Orador:**
  > "Miren esta tabla: ¡no existe una clase `Villager` ni una clase `Skeleton`! Ambos son instancias de `GameObject` ensambladas con distintas propiedades y los mismos componentes `March` y `Attack`. Además, el esqueleto tiene `corpseChance = 0.0` para evitar un bucle infinito donde un esqueleto muerto deje otra calavera resucitable."

---

### Diapositiva 9: Demostración Interactiva en Vivo (`NecromancyDemo.kt`)
- **Título:** Live Demo: El Ejército No Muerto en Acción
- **Puntos Clave:**
  - Ejecutar la demo de escritorio:
    ```bash
    task desktop
    # o directamente con Gradle:
    cd app && ./gradlew :desktopApp:run
    ```
  - **Lo que veremos en pantalla:**
    - Norman aguarda a la izquierda del campo de batalla (`x = 60`).
    - Hay 2 calaveras (`CORPSE`) en el suelo listas para ser resucitadas y 2 aldeanos marchando desde la derecha (`x = 600`, `x = 700`).
    - Al presionar **Barra Espaciadora**, **Enter** o **Clic izquierdo**, se ejecuta `game.resurrect()`.
    - Las calaveras se transforman en esqueletos aliados que marchan hacia la derecha dando saltitos (`hop`), chocan contra los aldeanos y los convierten en nuevas calaveras listas para la siguiente resurrección.
  - **Validación con Pruebas Unitarias (4 tests):**
    ```bash
    task test
    # Step1BehaviourTest, Step2AttackDamageTest, Step3CorpseDropTest, Step4SkeletonArmyTest
    ```
- **Notas del Orador:**
  > "En la barra inferior de la demo pueden observar en tiempo real los contadores de Cadáveres, Esqueletos Aliados, Aldeanos y el estado del Cooldown de 2 segundos de la habilidad de resurrección."

---

### Diapositiva 10: Conclusiones y Próximos Pasos (Hacia la Lección 09)
- **Título:** Resumen y Lo Que Viene: ¡Rituales, Oleadas y El Rey!
- **Puntos Clave:**
  - **Lo que logramos hoy:**
    - ✅ Desacoplamos la lógica en componentes reutilizables con el Patrón `Behaviour`.
    - ✅ Implementamos marcha por turnos con interpolación estética (`March` + `hop`).
    - ✅ Programamos combate cuerpo a cuerpo bidireccional (`Attack`).
    - ✅ Cerramos el bucle de Nigromancia: muerte (`die`), calaveras (`CORPSE`) y resurrección con cooldown (`resurrect`).
  - **Próxima parada (Lección 09):**
    - Nuevos comportamientos avanzados: `Seeking` (proyectiles teledirigidos), `Summon` (nigromantes invocadores), `Bleeding`, `Frozen` e `Invulnerable`.
    - Sistema de **10 oleadas progresivas**, tienda de rituales mágicos y el enfrentamiento final contra **El Rey**.
- **Notas del Orador:**
  > "Con la arquitectura `Behaviour` que construyeron hoy, agregar nuevos poderes, estados alterados y jefes en la Lección 09 será tan sencillo como crear nuevas subclases de `Behaviour` y enchufarlas a nuestras entidades."

---

## ❓ Preguntas Frecuentes y Casos Límite para el Aula (FAQ)

Ideal para generar sesiones de preguntas y respuestas en clase o para el **Audio Overview** de NotebookLM:

1. **¿Por qué `GameObject` tiene tanto `onFrame(dt)` como `onUpdate()` en sus `Behaviour`?**
   - *Respuesta:* Porque en muchos juegos combinamos dos escalas de tiempo distintas: `onFrame(dt)` corre en cada fotograma (~16.6 ms) para efectos visuales continuos (como bajar suavemente `hop` a 0 o mover partículas), mientras que `onUpdate()` corre al ritmo del reloj interno de cada criatura (`updateSpeed`, por ejemplo cada 600 ms en el esqueleto o cada 800 ms en el aldeano) para ejecutar decisiones tácticas o pasos discretos.

2. **¿Por qué iteramos sobre `behaviours.toList()` o `objects.toList()` en lugar de iterar directamente sobre la `MutableList`?**
   - *Respuesta:* En Kotlin/JVM, si mientras recorres una `MutableList` con un `for` uno de los elementos llama a `despawn()` o `removeBehaviour()`, la lista original cambia de tamaño a mitad del recorrido y lanza `ConcurrentModificationException`. Al llamar a `.toList()`, iteramos sobre una instantánea segura de ese instante.

3. **¿Qué ocurre en `updatePhysics` cuando dos unidades con `Attack` chocan entre sí en el mismo frame?**
   - *Respuesta:* En el doble bucle de colisiones, cuando `ally.onCollision(enemy)` se ejecuta, `Attack` aplica el daño en **ambas direcciones** (`damage(enemy, ...)` y `damage(ally, ...)`). Si uno de los dos muere en ese choque, `die()` llama inmediatamente a `despawn()`, retirándolo de `objects` y removiendo sus `behaviours` para que no vuelva a atacar como "fantasma" en ese mismo frame.

4. **¿Por qué los esqueletos tienen `corpseChance = 0.0` mientras los aldeanos tienen `corpseChance = 1.0`?**
   - *Respuesta:* Por balance de juego y diseño de recursos: los cadáveres provienen de los enemigos vivos derrotados (`Tags.LIVING`). Si los propios esqueletos (`Tags.UNDEAD`) dejaran otra calavera al morir, un solo cadáver inicial permitiría resucitar esqueletos infinitamente cada 2 segundos.

---

## 🚀 Guía de Uso en Google NotebookLM

Sigue estos sencillos pasos para crear tu presentación y materiales interactivos con **Google NotebookLM**:

### Paso 1: Acceder a NotebookLM
1. Ingresa a [notebooklm.google.com](https://notebooklm.google.com/).
2. Haz clic en **"Nueva libreta"** (o *New Notebook*).
3. Nómbrala: **"Lección 08 - Patrón Behaviour, IA y Nigromancia"**.

### Paso 2: Subir esta Fuente
1. Selecciona la opción **Subir fuentes** (*Add sources*).
2. Elige **"Archivo local"** (*Upload file*) y sube el archivo `LECCION_08_PRESENTACION_NOTEBOOKLM.md` (o copia y pega su contenido directamente si prefieres la opción de texto).

### Paso 3: Generar el "Audio Overview" (Podcast de la Lección)
1. En la parte superior derecha de NotebookLM, verás la tarjeta **"Resumen de audio"** (*Audio Overview / Deep Dive*).
2. Haz clic en **Generar** (*Generate*).
3. ¡Dos presentadores de IA conversarán explicando cómo funciona el patrón Behaviour frente a la herencia tradicional, el bug clásico del daño mutuo en `Attack`, y cómo se conectan los cadáveres con el hechizo de resurrección de Norman!

### Paso 4: Prompts Sugeridos para Chatear con la Libreta

Copia y pega cualquiera de estos prompts en el chat de NotebookLM:

#### Para Diapositivas en Google Slides o PowerPoint:
> *"A partir del documento de la Lección 08, formatea una presentación de 10 diapositivas para Google Slides. Para cada diapositiva proporciona: Título, 3 viñetas concisas y de alto impacto, el fragmento de código en Kotlin relevante, y notas de orador para el profesor."*

#### Para una Guía de Estudio / Hoja de Referencia rápida:
> *"Genera una 'Cheat Sheet' de una sola página en Markdown que resuma el ciclo de vida de `Behaviour` (`onFrame` vs `onUpdate`), la tabla comparativa entre Aldeano y Esqueleto, y el flujo completo desde `Attack` hasta `die()` y `resurrect()`."*

#### Para un Examen Corto / Quiz interactivo:
> *"Crea un quiz de 5 preguntas de opción múltiple para evaluar a los alumnos al final de la Lección 08. Incluye preguntas sobre Composición vs Herencia, por qué se guarda `dealDamage` antes de aplicar daño, y cómo funciona el cooldown en `resurrect()`. Incluye la justificación de cada respuesta correcta."*

#### Para el Guión del Profesor (Live Coding):
> *"Dame un guión de Live Coding paso a paso de 30 minutos para que el profesor programe junto con los alumnos los 4 pasos de la Práctica 08 (`March`, `Attack`, `die` y `resurrect`) explicando cómo probar cada paso con Gradle."*
