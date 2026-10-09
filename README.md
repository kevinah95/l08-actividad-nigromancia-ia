<h1 align="center">Práctica 08: Nigromancia y Comportamientos de IA 💀</h1>
<h3 align="center">Patrón Behaviour · Cinemática de Marcha · Cadáveres · Resurrección de Esqueletos</h3>

<p align="center">
  <a href="https://github.com/new?template_owner=kevinah95&template_name=l08-actividad-nigromancia-ia&owner=%40me&name=l08-actividad-nigromancia-ia&description=Mi+ejercicio+de+nigromancia+e+IA&visibility=public">
    <img src="https://img.shields.io/badge/Copiar%20ejercicio-%232ea44f?style=for-the-badge&logo=github&logoColor=white" alt="Copiar ejercicio"/>
  </a>
</p>

---

## Bienvenido 👋

En esta práctica implementarás la mecánica central y más emocionante de **Norman The Necromancer**: el sistema de componentes desacoplados de comportamiento (IA) y la habilidad de levantar ejércitos de esqueletos a partir de los enemigos caídos.

> **¿Para quién es este ejercicio?**
> Estudiantes que cursaron la Lección 07 y desean aprender el patrón de diseño por componentes (Behaviour/Strategy) y la lógica de combate táctico en Kotlin Multiplatform.

---

## Lo que aprenderás y construirás

| # | Concepto | Lo que harás |
|---|---|---|
| 1 | **Patrón Behaviour y Marcha** | Crear componentes de IA desacoplados y simular pasos con saltito por interpolación |
| 2 | **Choque de Tropas (Attack)** | Programar daño cruzado en colisiones cuerpo a cuerpo entre unidades |
| 3 | **El Ciclo de Cadáveres** | Hacer que los enemigos derrotados dejen calaveras (`CORPSE`) en el campo de batalla |
| 4 | **El Gran Hechizo de Resurrección** | Crear la habilidad con cooldown que transforma cadáveres en un ejército de esqueletos |

---

## Prerrequisitos

Antes de comenzar necesitas:
- [ ] Haber completado la Lección 07 (Físicas 2D y Colisiones)
- [ ] [Android Studio](https://developer.android.com/studio) o IntelliJ IDEA con JDK 17+
- [ ] [Task](https://taskfile.dev/) (opcional, pero recomendado)

---

## Cómo empezar

1. Haz clic en **"Copiar ejercicio"** arriba para generar tu repositorio personal desde esta plantilla.
2. Espera unos segundos a que GitHub Actions cree el **issue del ejercicio** guiado.
3. Sigue las instrucciones paso a paso detalladas en ese issue.
4. Cada vez que hagas un `push` a la rama `main`, el flujo de trabajo verificará tu solución y desbloqueará el siguiente paso.

---

## Estructura del proyecto

```
l08-actividad-nigromancia-ia/
├── README.md
├── Taskfile.yml                               ← Comandos rápidos (task test, task desktop)
├── .github/
│   ├── steps/                                 ← Instrucciones de cada paso
│   └── workflows/                             ← Automatización con GitHub Skills
└── app/                                       ← Proyecto Gradle KMP
    ├── build.gradle.kts
    ├── settings.gradle.kts
    ├── desktopApp/                            ← Ejecutable de escritorio
    └── shared/
        └── src/
            ├── commonMain/kotlin/.../
            │   ├── core/
            │   │   ├── Behaviour.kt           ← Base de componentes
            │   │   ├── GameObject.kt          ← Entidad con behaviours
            │   │   └── Game.kt                ← die() y resurrect()
            │   ├── behaviours/
            │   │   └── Behaviours.kt          ← March y Attack
            │   └── entities/
            │       └── Entities.kt            ← createCorpse() y createSkeleton()
            └── jvmTest/kotlin/.../            ← Pruebas unitarias de validación
```

---

## Comandos Útiles

```bash
# Ejecutar todas las pruebas de validación
task test
# o directamente con Gradle:
cd app && ./gradlew :shared:jvmTest

# Ejecutar la demo interactiva en Desktop (JVM)
task desktop
# o con Gradle:
cd app && ./gradlew :desktopApp:run
```
