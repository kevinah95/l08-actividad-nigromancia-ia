<img src="https://octodex.github.com/images/spidertocat.png" align="right" height="100px" alt="Spidertocat indicando error" />

### ⚠️ Las pruebas no pasaron en el Paso 3 (Muerte y Generación de Cadáveres)

La prueba unitaria `Step3CorpseDropTest` no superó todas las verificaciones.

> 🔍 **Pistas para encontrar el error en `Die`:**
> - En `onCollision(other)` o al morir: Si `gameObject.hp <= 0`, marca `gameObject.isAlive = false`.
> - Al activarse la muerte, si genera cadáver, crea una instancia de `Corpse` en las mismas coordenadas `(gameObject.x, gameObject.y)` y agrégala a la lista de objetos de la sesión.

💡 Puedes ver el reporte detallado ejecutando la prueba localmente:
```bash
cd app
./gradlew :shared:jvmTest --tests "*Step3CorpseDropTest*"
```

¡Revisa el comportamiento de muerte y generación de cadáveres y vuelve a subir tus cambios con `git push`! 🧐
