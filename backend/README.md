# 🧾 Backend Ledgerly

![Java](https://img.shields.io/badge/Java-23-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Build](https://img.shields.io/badge/Build-Maven-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)
![Status](https://img.shields.io/badge/Status-Stable-success)

---

**Backend Ledgerly** es la capa de servidor del sistema de facturación multiempresa **Ledgerly**, desarrollada con **Java 23** y **Spring Boot 3.5.7**.  
Está diseñado para gestionar de forma segura y escalable **empresas, clientes, empleados, productos e invoices**, bajo una arquitectura modular y con autenticación basada en **JWT**.

---

## 📘 Tabla de Contenidos

- [Tecnologías principales](#-tecnologías-principales)
- [Arquitectura del proyecto](#-arquitectura-del-proyecto)
- [Seguridad y autenticación](#-seguridad-y-autenticación)
- [Gestión multiempresa](#-gestión-multiempresa)
- [Endpoints principales](#-endpoints-principales)
- [Ejemplo de respuesta](#-ejemplo-de-respuesta)
- [Instalación y configuración](#️-instalación-y-configuración)
- [Ejecución y despliegue](#️-ejecución-y-despliegue)
- [Convenciones y estilo](#-convenciones-y-estilo)
- [Autor y licencia](#-autor-y-licencia)

---

## 🚀 Tecnologías principales

| Componente | Tecnología |
|-------------|-------------|
| Lenguaje | **Java 23** |
| Framework | **Spring Boot 3.5.7** |
| Seguridad | **Spring Security + JWT** |
| Persistencia | **Spring Data JPA / Hibernate** |
| Base de datos | **MySQL 8** |
| Construcción | **Maven** |
| Logging | **SLF4J + Logback** |

---

## 🧩 Arquitectura del proyecto

El backend sigue una arquitectura por capas **modular y mantenible**, con separación clara de responsabilidades.  

```
src
└── main
    ├── java
    │   └── com.backend.api.invoice_manager
    │       ├── controllers/
    │       ├── dtos/
    │       ├── entities/
    │       ├── exceptions/
    │       ├── repositories/
    │       ├── responses/
    │       ├── security/
    │       ├── services/
    │       ├── utils/
    │       └── validators/
    └── resources/
```

El detalle completo de la estructura se encuentra en el repositorio e incluye controladores REST, servicios, orquestadores, validadores y repositorios JPA.

---

## 🔐 Seguridad y autenticación

El sistema utiliza **JWT (JSON Web Tokens)** para autenticar usuarios.  
Cada petición protegida requiere incluir el header:

```
Authorization: Bearer <token>
```

### 🔄 Flujo de autenticación

1. El usuario inicia sesión o se registra.
2. El backend genera un token JWT firmado.
3. El cliente guarda el token localmente.
4. Spring Security valida el token en cada petición protegida.

### 👥 Roles

| Rol | Descripción |
|------|-------------|
| `ROLE_SUPER_ADMIN` | Acceso total al sistema |
| `ROLE_ADMIN` | Gestión de aspectos generales del sistema |
| `ROLE_COMPANY_SUPER_ADMIN` | Gestión total dentro de su empresa |
| `ROLE_COMPANY_ADMIN` | Gestión limitada dentro de su empresa |
| `ROLE_USER` | Acceso limitado a sus propios recursos |

---

## 🏢 Gestión multiempresa

Cada entidad (Cliente, Empleado, Factura, Producto) está vinculada a una **Company**, y las operaciones se filtran según el usuario autenticado.

```java
if (!userPermissionValidator.requireCompanyOwnershipOrAdmin(companyId)) {
    throw new AccessDeniedException("User not authorized to access this company");
}
```

Además, los **Orchestrators** coordinan operaciones complejas que involucran múltiples entidades o servicios.

---

## 🌐 Endpoints principales

### 🔑 Autenticación
- `POST /api/auth/login`
- `POST /api/auth/register`
- `POST /api/auth/forgot-password`

### 🧾 Invoices
- `GET /api/invoices`
- `POST /api/invoices`
- `PUT /api/invoices/{id}`
- `DELETE /api/invoices/{id}`
- `GET /api/invoices/search-by-company/{companyId}`
- `GET /api/invoices/search-by-date`
- `GET /api/invoices/this-month`
- `GET /api/invoices/today`

### 🏢 Companies
- `GET /api/companies`
- `POST /api/companies`
- `PUT /api/companies`
- `DELETE /api/companies/{id}`
- `GET /api/companies/search/all`

### 👥 Employees
- `GET /api/employees`
- `POST /api/employees`
- `PUT /api/employees`
- `DELETE /api/employees/{id}`
- `PUT /api/employees/assign-company/{companyId}/{employeeId}`

### 🧍 Users
- `GET /api/users`
- `POST /api/users`
- `PUT /api/users`
- `DELETE /api/users/{id}`
- `GET /api/users/current`
- `GET /api/users/roles`

### 🛍️ Products
- `GET /api/products`
- `POST /api/products`
- `PUT /api/products`
- `DELETE /api/products/{id}`
- `GET /api/products/company/{companyId}`

### 💼 Clients
- `GET /api/clients`
- `POST /api/clients/{companyId}`
- `PUT /api/clients/{companyId}`
- `DELETE /api/clients/{id}`
- `GET /api/clients/search/company/{companyId}`

---

## 🧩 Ejemplo de respuesta

El backend utiliza una estructura estándar para todas las respuestas HTTP:

```java
public class ApiResponse<T> {
    private String message;
    private T data;
    private Integer statusCode;
    private Boolean success;
}
```

### 🧠 Ejemplo JSON

```json
{
  "message": "Invoice retrieved successfully",
  "data": {
    "id": 42,
    "companyName": "Ledgerly Labs",
    "clientName": "Tech Innovations SL",
    "totalAmount": 2340.50,
    "status": "PAID",
    "issueDate": "2025-11-01T10:00:00"
  },
  "statusCode": 200,
  "success": true
}
```

---

## ⚙️ Instalación y configuración

1. **Clonar el repositorio**
   ```bash
   git clone git@github.com:pbbaque/InvoiceSpringBackend.git
   cd InvoiceSpringBackend
   ```

2. **Configurar la base de datos**  
   En `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/invoice_manager
   spring.datasource.username=root
   spring.datasource.password=tu_password
   spring.jpa.hibernate.ddl-auto=update
   jwt.secret=your_secret_key
   jwt.expiration=86400000
   ```

---

## ▶️ Ejecución y despliegue

### 🧩 Ejecución local
```bash
mvn clean install
mvn spring-boot:run
```

La API estará disponible en:
👉 http://localhost:8080/api

### ☁️ Despliegue
Para producción, asegúrate de:
- Configurar variables de entorno seguras (DB y JWT).
- Usar un servidor con Java 23 y MySQL 8.
- Habilitar HTTPS y CORS adecuados.

---

## 🧱 Convenciones y estilo

- Los **servicios base** implementan operaciones CRUD genéricas.
- Los **orchestrators** coordinan flujos entre servicios.
- Los **validators** aseguran la consistencia y permisos.
- Las **excepciones personalizadas** heredan de `RuntimeException`.

---

## 👨‍💻 Autor y licencia

**Backend Ledgerly**  
Desarrollado por **Pablo Barreda**  
📧 pbbaque@gmail.com  
🌍 [GitHub - pbbaque](https://github.com/pbbaque)

Distribuido bajo la **MIT License**.  
Consulta el archivo `LICENSE` para más información.
