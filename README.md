# space-x

### Pasos para correr levantar el servicio

1. Clonar el proyecto dentro del directorio de trabajo (ejemplo '/workspace')
2. Abrir una terminal en el directorio seleccionado
3. Asegurarse de estar utilizando una SDK compatible con java 17 o posterior. Para chequear, usar comando 'java -version'
4. Buildear proyecto con el siguiente comando './gradlew clean build'
5. Levantar servicio con el siguiente comando './gradlew bootRun'

El servicio se levantará por defecto en el puerto 8080

### Pasos para probar el servicio

1. Abrir una nueva terminal
2. Realizar curl con los parametros deseados o importar curl y pegarle con Postman

curl -X POST http://localhost:8080/api/trello/createCard \
-H "Content-Type: application/json" \
-d '{"type":"task", "title": "Clean the Rocket", "category": "Maintenance"}'

curl -X POST http://localhost:8080/api/trello/createCard \
-H "Content-Type: application/json" \
-d '{"type":"issue", "title": "Send message", "description":"Let pilots send messages to Central"}'

curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"type": "bug", "description": "Cockpit is not depressurizing correctly"}' \
  http://localhost:8080/api/trello/createCard

  ![image](https://github.com/ghoyos95/space-x/assets/32983207/0463491f-d305-4019-9a17-fd4823096b65)

En caso de haberse creado exitosamente la tarjeta recibirá el siguiente string como se muestra en imagen







