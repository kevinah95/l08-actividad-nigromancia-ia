<img src="https://octodex.github.com/images/spidertocat.png" align="right" height="100px" alt="Spidertocat indicando que hubo un error" />

### ⚠️ Parece que las pruebas no pasaron en el {{ step_name }}

{{ feedback_message }}

> 💡 **Consejo:** Recuerda que puedes ejecutar y depurar las pruebas en tu terminal localmente antes de hacer push:
> ```bash
> cd app
> ./gradlew :shared:jvmTest --tests "*{{ test_name }}*"
> ```

¡Revisa el código, corrige el error y vuelve a subir tus cambios con `git push`! 🧐
