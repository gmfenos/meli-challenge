# Windows

### Requerimientos

* Docker Desktop
* Java 17

### Cómo levantar

Ejecutar levantar.bat

Otra alternativa es abrir una línea de comandos sobre el directorio raíz del proyecto y ejecutar:
* mvnw.cmd clean package
* mvnw.cmd spring-boot:build-image
* docker-compose up

Para verificar que haya levantado correctamente:
* localhost/actuator/health

### Uso

Nota: Si se agrega la siguiente línea al archivo C:\Windows\System32\drivers\etc\hosts se puede utilizar usando me.li en lugar de localhost como host.
* 127.0.0.1    me.li

Hay un redis insight disponible en http://localhost:8001/ para acceder al a base de datos redis.

> GET me.li/`<código-corto>`
> * Redirige a la `<url-larga>` asociada

> POST me.li/api/create-short-url?longURL=`<url-larga>`
> * Genera y devuelve un `<código-corto>` que referencia a la `<url-larga>`

> DELETE me.li/api/delete-short-url?shortURL=`<código-corto>`
> * Elimina la referencia a una `<url-larga>`
