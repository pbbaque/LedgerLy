# LedgerLy — InvoiceManager

[![Frontend](https://img.shields.io/badge/Frontend-Angular-red)](frontend/README.md)
[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot-green)](backend/README.md)
[![License](https://img.shields.io/badge/License-MIT-blue)](#license)

**LedgerLy** es un sistema de facturación multiempresa completo, que combina un **backend con Spring Boot** y un **frontend con Angular**, integrando la gestión de empresas, clientes, empleados, productos, facturas y usuarios en un entorno seguro y escalable.

Este repositorio es un **monorepo**, lo que significa que el **frontend y backend se encuentran juntos**, manteniendo su historial de commits original y sus README independientes.

---

## 📂 Estructura del repositorio

/frontend → Código y documentación del frontend (Angular)
/backend → Código y documentación del backend (Spring Boot)

yaml
Copiar código

- **frontend/**: Aplicación web desarrollada en Angular 20.  
  Modular y escalable, con autenticación JWT y comunicación vía REST con el backend.  
  Incluye módulos por dominio, componentes reutilizables y servicios centralizados.  
  [Ver README del frontend](frontend/README.md)

- **backend/**: API REST desarrollada en Spring Boot 3.5.7 y Java 23.  
  Gestiona la lógica de negocio, persistencia (MySQL), seguridad (JWT) y arquitectura modular por capas.  
  [Ver README del backend](backend/README.md)

---

## ⚙️ Tecnologías principales

| Parte      | Tecnologías |
|------------|------------|
| Frontend   | Angular 20, TypeScript, RxJS, Chart.js, SCSS modular |
| Backend    | Spring Boot 3.5.7, Java 23, Spring Security + JWT, Spring Data JPA/Hibernate, MySQL 8, Maven |
| Herramientas | Node.js 22, npm 10, jwt-decode, Swiper, ng2-charts |

---

## 🔐 Autenticación y roles

- El sistema utiliza **JWT** para autenticar usuarios.  
- Los roles disponibles incluyen:
  - `ROLE_SUPER_ADMIN` — Acceso total
  - `ROLE_COMPANY_SUPER_ADMIN` — Administración completa dentro de su empresa
  - `ROLE_COMPANY_ADMIN` — Gestión operativa de facturas, clientes y productos
  - `ROLE_ADMIN` — Acceso administrativo limitado
  - `ROLE_USER` — Acceso estándar a sus recursos

---

## 🌐 Integración Frontend – Backend

- El frontend se comunica con el backend vía REST API (`environment.apiUrl = 'http://localhost:8080/api'`).  
- Funcionalidades cubiertas:
  - CRUD completo de clientes, productos, empleados, empresas, facturas y usuarios
  - Autenticación y autorización
  - Estadísticas y dashboards
- Cada carpeta (`frontend` y `backend`) mantiene su historial de commits y README específico para detalles de instalación y configuración.

---

## 🚀 Ejecución del proyecto

### Backend (Spring Boot)
```bash
cd backend
mvn clean install
mvn spring-boot:run
La API estará disponible en: http://localhost:8080/api

Frontend (Angular)
bash
Copiar código
cd frontend
npm install
ng serve
La aplicación Angular estará disponible en: http://localhost:4200

🧩 Estructura y Arquitectura
Backend
Arquitectura modular por capas (controllers, services, repositories, security, validators)

Autenticación JWT

Gestión multiempresa con validación de permisos por usuario

Orchestrators para operaciones complejas

Frontend
Arquitectura modular basada en feature modules

Componentes reutilizables y layouts centralizados

SCSS modularizado y atomic CSS

Guards y interceptors para control de acceso y JWT

💡 Próximas mejoras
Implementación de pruebas unitarias (JUnit, Jasmine/Karma)

Internacionalización (i18n)

Temas dinámicos (modo claro/oscuro)

Dashboards más completos e interactivos

🧾 Licencia
Este proyecto está bajo la licencia MIT.
Consulta el archivo LICENSE para más información.
