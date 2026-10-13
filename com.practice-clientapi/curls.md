# Client API - cURL de prueba

Base URL: `http://localhost:8080`

## GET /api/clients
Lista todos los clientes.

```bash
curl -X GET http://localhost:8080/api/clients
```

## POST /api/client
Crea un cliente.

```bash
curl -X POST http://localhost:8080/api/client \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Perez",
    "surname": "Gomez",
    "phone": "3001234567"
  }'
```

## DELETE /api/clients/{id}
Elimina un cliente por id (devuelve 204 si existe, 404 si no).

```bash
curl -X DELETE http://localhost:8080/api/clients/1 -i
```
