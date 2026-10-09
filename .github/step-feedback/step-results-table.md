{%- set all_passed = (results_table | selectattr("passed") | length) == (results_table | length) %}

{%- if all_passed %}

## Paso {{ step_number }} — Aprobado ✅

{%- else %}

## Paso {{ step_number }} — Requiere corrección ❌

{%- endif %}

{%- if all_passed %}
<img src="https://octodex.github.com/images/inflatocat.png" align="right" height="150px" alt="Inflatocat indicando que el paso fue aprobado" />
{%- else %}
<img src="https://octodex.github.com/images/spidertocat.png" align="right" height="150px" alt="Spidertocat indicando que hubo un error" />
{%- endif %}

| Prueba | Estado | Detalle |
| :--- | :---: | :--- |
{%- for result in results_table %}
| `{{ result.name }}` | {% if result.passed %} ✅ Pasó {% else %} ❌ Falló {% endif %} | {{ result.error }} |
{%- endfor %}
