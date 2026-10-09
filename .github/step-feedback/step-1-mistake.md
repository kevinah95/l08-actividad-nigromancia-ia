<img src="https://octodex.github.com/images/spidertocat.png" align="right" height="100px" alt="Spidertocat indicando error" />

### ⚠️ Las pruebas no pasaron en el Paso 1 (Patrón Behaviour y Marcha con Salto)

La prueba unitaria `Step1BehaviourTest` no superó todas las verificaciones.

> 🔍 **Pistas para encontrar el error en `Behaviours.kt`:**
> - En `March`: Al llamar a `onUpdate(dtMs)`, la posición horizontal del objeto debe avanzar según el paso configurado: `gameObject.x += step`.
> - En `Jump`: Comprueba que al ejecutarse el salto se configure la velocidad vertical: `gameObject.vy = jumpVelocity`.

💡 Puedes ver el reporte detallado ejecutando la prueba localmente:
```bash
cd app
./gradlew :shared:jvmTest --tests "*Step1BehaviourTest*"
```

¡Revisa `Behaviours.kt`, corrige el componente y vuelve a subir tus cambios con `git push`! 🧐
