<img src="https://octodex.github.com/images/spidertocat.png" align="right" height="100px" alt="Spidertocat indicando error" />

### ⚠️ Las pruebas no pasaron en el Paso 2 (Comportamiento Attack y Daño)

La prueba unitaria `Step2AttackDamageTest` no superó todas las verificaciones.

> 🔍 **Pistas para encontrar el error en `Attack`:**
> - En `onCollision(other)`: Verifica que el atacante pueda colisionar/hacer daño al objetivo según sus máscaras (`gameObject.canCollideWith(other)`).
> - Si puede colisionar, resta la vida del objetivo: `other.hp -= damage`.
> - Si `other.hp <= 0`, marca al objetivo como inactivo: `other.isAlive = false`.

💡 Puedes ver el reporte detallado ejecutando la prueba localmente:
```bash
cd app
./gradlew :shared:jvmTest --tests "*Step2AttackDamageTest*"
```

¡Revisa `Behaviours.kt`, corrige la lógica de ataque y vuelve a subir tus cambios con `git push`! 🧐
