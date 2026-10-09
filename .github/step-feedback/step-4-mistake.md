<img src="https://octodex.github.com/images/spidertocat.png" align="right" height="100px" alt="Spidertocat indicando error" />

### ⚠️ Las pruebas no pasaron en el Paso 4 (Nigromancia y Ejército de Esqueletos)

La prueba unitaria `Step4SkeletonArmyTest` no superó todas las verificaciones.

> 🔍 **Pistas para encontrar el error en `Resurrect`:**
> - Al invocar la nigromancia sobre un `Corpse`, remueve o desactiva el cadáver (`corpse.isAlive = false`).
> - Instancia un nuevo esqueleto aliado (`Skeleton`) con las etiquetas de aliado (`Tags.ALLY`), marchando hacia los enemigos.
> - Agrega el nuevo esqueleto a la sesión de juego.

💡 Puedes ver el reporte detallado ejecutando la prueba localmente:
```bash
cd app
./gradlew :shared:jvmTest --tests "*Step4SkeletonArmyTest*"
```

¡Revisa la habilidad de nigromancia y vuelve a subir tus cambios con `git push`! 🧐
