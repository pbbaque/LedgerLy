# LedgerLy — Invoice Manager App (Frontend)

Aplicación web desarrollada en **Angular** para la gestión integral de facturación multiempresa.  
Forma parte del ecosistema **LedgerLy**, junto con su backend [LedgerLy API (Spring Boot)](https://github.com/pbbaque/InvoiceSpringBackend).

---

## 🧭 Descripción general

LedgerLy es una aplicación moderna e intuitiva que permite a empresas y profesionales gestionar sus **facturas, clientes, productos, empleados y usuarios** desde un entorno centralizado y seguro.

El frontend está construido en **Angular 20**, con autenticación basada en **JWT**, comunicación directa con el backend vía **REST API**, y una interfaz modular y escalable pensada para entornos multiempresa.

---

## 👤 Autor

**Pablo Barreda**  
📧 [pbbaque@gmail.com](mailto:pbbaque@gmail.com)

Licencia: **MIT**

Repositorio:  
🔗 [git@github.com:pbbaque/invoice-manager-app.git](git@github.com:pbbaque/invoice-manager-app.git)

---

## ⚙️ Tecnologías y versiones principales

| Tecnología / Librería | Versión |
|------------------------|---------|
| Angular | 20.3.7 |
| Node.js | 22.21.0 |
| npm | 10.9.0 |
| TypeScript | 5.9.3 |
| RxJS | 7.8.2 |
| Zone.js | 0.15.1 |
| Chart.js | 4.5.1 |
| ng2-charts | 8.0.0 |
| Swiper | 12.0.3 |
| jwt-decode | 4.0.0 |

---

## 🧱 Arquitectura del proyecto

La estructura sigue una arquitectura modular basada en **módulos por dominio** (feature modules), acompañada de **componentes reutilizables** y **servicios centralizados** para comunicación con el backend.

```
C:.
|   index.html
|   main.ts
|
+---app
|   |   app-routing.module.ts
|   |   app.module.ts
|   |
|   +---components
|   |   +---alert/
|   |   +---breadcrumb/
|   |   +---confirm/
|   |   +---empty-state/
|   |   +---entity-detail/
|   |   +---footer/
|   |   +---header/
|   |   +---loading-spinner/
|   |   +---modal/
|   |   +---pagination/
|   |   +---sidebar/
|   |   \---table/
|   |
|   +---guards/
|   +---interceptors/
|   +---layouts/
|   +---models/
|   +---services/
|   +---styles/
|   \---views/
|       +---auth/
|       +---clients/
|       +---companies/
|       +---dashboard/
|       +---employees/
|       +---invoices/
|       +---products/
|       +---profiles/
|       \---users/
|
+---assets/
|   \---icons/
|
\---environments/
        environments.ts
        environments.prod.ts
```

---

## 🔐 Autenticación y seguridad

LedgerLy utiliza un sistema de autenticación **JWT (JSON Web Token)**:

- El **AuthService** gestiona el login, registro y refresh de tokens.  
- El token se almacena de forma segura en **localStorage**.  
- Los **guards (`auth.guard.ts`, `role.guard.ts`)** restringen el acceso según permisos y roles.  
- El **AuthInterceptor** añade automáticamente el token en cada petición HTTP.

### Roles disponibles

| Rol | Descripción |
|------|-------------|
| `ROLE_SUPER_ADMIN` | Acceso total a todas las empresas y usuarios. |
| `ROLE_COMPANY_SUPER_ADMIN` | Administración global dentro de su propia empresa (usuarios, empleados, facturas, etc.). |
| `ROLE_COMPANY_ADMIN` | Gestión operativa de facturas, productos y clientes dentro de su empresa. |
| `ROLE_ADMIN` | Usuario administrativo con acceso a funciones específicas. |
| `ROLE_USER` | Usuario estándar con acceso limitado a sus recursos y datos propios. |

---

## 🌍 Integración con el Backend

El frontend se comunica con el backend de LedgerLy mediante la variable de entorno:

```typescript
// environments.ts
export const environment = {
  apiUrl: 'http://localhost:8080/api'
};
```

> 📡 Backend principal: [LedgerLy API (Spring Boot)](https://github.com/pbbaque/InvoiceSpringBackend)

---

## 🧩 Principales módulos y vistas

| Módulo | Descripción |
|--------|--------------|
| **Auth** | Autenticación, login, registro y recuperación de contraseña. |
| **Dashboard** | Panel principal con estadísticas y métricas. |
| **Clients / Companies / Employees / Products / Invoices** | CRUD completo de cada entidad con vistas de lista, detalle y formulario. |
| **Profiles** | Gestión de perfiles de usuario y empresa. |
| **Users** | Administración de usuarios (solo accesible por roles con permisos). |

---

## 🎨 Estilos y diseño

Los estilos se organizan mediante **SCSS modularizado** y el uso de `@use` en todos los archivos parciales.  
La estructura está basada en una arquitectura **atomic/modular CSS**:

```
styles/
│   main.scss
│   _variables.scss
│   _mixins.scss
│   _global.scss
│   _typography.scss
│
└── ui/
    ├── _buttons.scss
    ├── form/
    ├── list/
    └── table/
```

Esto permite una gestión clara y mantenible de la UI, con un diseño responsive y adaptable.

---

## 🧰 Comandos principales

### 🔧 Desarrollo local
```bash
npm install
ng serve
```
Ejecuta la aplicación en modo desarrollo.  
📍 Disponible en: [http://localhost:4200](http://localhost:4200)

### 🏗️ Build de producción
```bash
ng build
```
Genera la carpeta `dist/` con la versión optimizada para despliegue.

---

## 🚀 Despliegue

Actualmente, el proyecto no utiliza Docker ni despliegue automatizado.  
Está pensado para entorno local o despliegues manuales (por ejemplo, en Netlify o Vercel).

---

## 🧩 Dependencias destacadas

- **ng2-charts** y **chart.js** → visualización de datos contables y de facturación.  
- **jwt-decode** → lectura de payloads JWT.  
- **Swiper** → carruseles y elementos interactivos en el dashboard.  
- **SCSS modularizado** → consistencia visual en toda la aplicación.

---

## 🧱 Integración con el backend

El frontend se comunica con el backend en tiempo real para:
- Autenticación y validación de sesión.
- CRUD completo de entidades (clientes, empresas, empleados, productos, facturas, usuarios).
- Estadísticas y métricas del dashboard.

Ejemplo de respuesta del backend:

```json
{
  "message": "Client retrieved successfully",
  "data": {
    "id": 1,
    "name": "ACME Corp",
    "email": "info@acme.com"
  },
  "statusCode": 200,
  "success": true
}
```

---

## 🧾 Licencia

Este proyecto está bajo la licencia **MIT**.  
Consulta el archivo [`LICENSE`](LICENSE) para más información.

---

## 🧠 Próximas mejoras

- Implementación de pruebas unitarias (Jasmine/Karma).  
- Internacionalización (i18n).  
- Integración de temas dinámicos (modo claro/oscuro).  
- Dashboard con más métricas interactivas.

---

## 🧾 Manual de Usuario - Ledgerly

Ledgerly es una aplicación web desarrollada en Angular para la gestión integral de empresas, clientes, productos, empleados y facturación.
El acceso y las funcionalidades disponibles varían según el rol asignado al usuario.

## 🔐 Acceso a la aplicación
Login

Desde la pantalla de inicio de sesión, el usuario puede autenticarse mediante su correo electrónico y contraseña.

<img width="850" height="651" alt="image" src="https://github.com/user-attachments/assets/6cda472a-4f4e-4352-bb46-77a99add0df4" />

Campos:

Email

Contraseña

Botones:

“Iniciar Sesión”

“¿Olvidó su contraseña?” → redirige a la sección Recuperar contraseña.

Roles que pueden acceder: Todos los usuarios registrados.

Registro de Empresa

El registro crea automáticamente una nueva empresa en el sistema y genera un usuario administrador principal.
Las credenciales del nuevo usuario se envían al correo electrónico proporcionado durante el registro.

<img width="788" height="805" alt="image" src="https://github.com/user-attachments/assets/c2624a37-0a04-40f2-969f-9266862c9725" />

Campos:

Nombre de la empresa

NIF/CIF de la empresa

Telefono de la empresa

Email de la empresa

Direccion de empresa (Calle, codigo postal, ciudad y pais)

Rol asignado automáticamente: ROLE_COMPANY_SUPER_ADMIN

## 🧭 Interfaz principal

Una vez autenticado, el usuario accede al panel principal, compuesto por dos áreas principales:

Sidebar (barra lateral)

Contiene los accesos a las secciones principales:

Dashboard

Usuarios (solo visible para roles administrativos)

Empresas (solo visible para roles administrativos del sistema)

Empleados (solo visible para roles administrativos)

Facturas

Clientes

Productos

<img width="192" height="460" alt="image" src="https://github.com/user-attachments/assets/0fca4e2c-1262-4860-9237-d1daa397738c" />

Header (barra superior)

Desde el Header se puede:

Acceder al Perfil de Usuario

Acceder al Perfil de Empresa (Solo presente en los roles de empresa)

Cerrar sesión (Logout)

<img width="1835" height="86" alt="image" src="https://github.com/user-attachments/assets/f749f56f-0787-4ce3-82c0-690182e6c24e" />

## 📊 Dashboard

Muestra un resumen general con:

Gráfica de facturación mensual

Top 5 clientes

Top 5 productos

Últimas facturas

<img width="1845" height="862" alt="image" src="https://github.com/user-attachments/assets/08d9e6fd-f266-4eeb-a894-000406fe70f2" />

Visibilidad según rol:

ROLE_SUPER_ADMIN → Datos generales del sistema

ROLE_ADMIN → Datos globales con permisos limitados

ROLE_COMPANY_SUPER_ADMIN / ROLE_COMPANY_ADMIN → Datos de su propia empresa

ROLE_USER → Datos de su propia empresa

## 👤 Usuarios

Gestión de usuarios del sistema (solo visible para roles administrativos).

<img width="1833" height="779" alt="image" src="https://github.com/user-attachments/assets/edf5cc2e-348a-4823-88e8-0fa619e0068c" />

<img width="1828" height="750" alt="image" src="https://github.com/user-attachments/assets/38b22315-928a-49ce-b585-9c4324790188" />

Roles con acceso:

ROLE_SUPER_ADMIN: Acceso total al sistema

ROLE_ADMIN: Gestión parcial (sin modificar superadmins)

ROLE_COMPANY_SUPER_ADMIN: Gestión de usuarios dentro de su empresa

ROLE_COMPANY_ADMIN: Lectura y asignación de roles internos

ROLE_USER: Sin acceso


## 🧑‍💼 Empresas

Gestión de empresas registradas en el sistema.

<img width="1838" height="927" alt="image" src="https://github.com/user-attachments/assets/070a0d2f-1ac2-4548-a0c9-f3c89efbb250" />

<img width="1818" height="761" alt="image" src="https://github.com/user-attachments/assets/7c130239-1929-4170-a0f1-aaafee0c63c5" />

Roles con acceso:

ROLE_SUPER_ADMIN, ROLE_ADMIN: Acceso total

ROLE_COMPANY_SUPER_ADMIN: Sin acceso

ROLE_COMPANY_ADMIN: Sin acceso

ROLE_USER: Sin acceso

## 🧑‍💼 Empleados

Gestión de empleados vinculados a cada empresa.

<img width="1841" height="787" alt="image" src="https://github.com/user-attachments/assets/814a4dd4-b168-48fc-a9ca-f073779cbc57" />

<img width="1678" height="698" alt="image" src="https://github.com/user-attachments/assets/8067b7a0-b5d0-4247-9815-0a77314debe1" />

Roles con acceso:

ROLE_SUPER_ADMIN, ROLE_ADMIN: Acceso total

ROLE_COMPANY_SUPER_ADMIN: Gestión completa dentro de su empresa

ROLE_COMPANY_ADMIN: Puede crear, editar o eliminar empleados de su empresa

ROLE_USER: Sin acceso

## 🧾 Facturas

Permite listar, crear, visualizar y anular facturas.

<img width="1833" height="795" alt="image" src="https://github.com/user-attachments/assets/d0cff080-efe4-424c-8721-7d94ba4a5074" />

<img width="1836" height="858" alt="image" src="https://github.com/user-attachments/assets/2b016b29-fece-4148-a3b9-90c42f1abb6e" />

<img width="1690" height="717" alt="image" src="https://github.com/user-attachments/assets/257a5f18-dbd8-4f6e-bc4c-6a9f7a23d2f6" />

Visibilidad y permisos:

ROLE_SUPER_ADMIN / ROLE_ADMIN: Acceso total a todas las facturas del sistema

ROLE_COMPANY_SUPER_ADMIN / ROLE_COMPANY_ADMIN: Gestión completa de facturas de su empresa

ROLE_USER: Solo puede ver o crear sus propias facturas

## 👥 Clientes

Permite gestionar la información de los clientes asociados a una empresa.

<img width="1826" height="740" alt="image" src="https://github.com/user-attachments/assets/b4c9f9df-4116-4b50-911c-019c48f77d0e" />

<img width="1685" height="668" alt="image" src="https://github.com/user-attachments/assets/39a7bc6f-db6b-46a4-881d-3f563bb3a24b" />

Roles con acceso:

ROLE_SUPER_ADMIN, ROLE_ADMIN: Visualizan todos los clientes

ROLE_COMPANY_SUPER_ADMIN, ROLE_COMPANY_ADMIN: Gestionan clientes de su empresa

ROLE_USER: Solo lectura


📦 Productos

Módulo para gestionar el catálogo de productos.

<img width="1825" height="814" alt="image" src="https://github.com/user-attachments/assets/190aaeff-4d14-4b11-8733-b3cef78eaa06" />

<img width="1839" height="702" alt="image" src="https://github.com/user-attachments/assets/b80b25e8-0c49-4e08-80be-5124aa46bd41" />

Roles con acceso:

ROLE_SUPER_ADMIN, ROLE_ADMIN: Acceso total

ROLE_COMPANY_SUPER_ADMIN, ROLE_COMPANY_ADMIN: Gestión de productos de su empresa

ROLE_USER: Solo lectura

## 🏢 Perfil de Empresa

Muestra y permite editar los datos de la empresa activa.

<img width="1819" height="728" alt="image" src="https://github.com/user-attachments/assets/d2b1b578-510b-45fb-9835-944a2d966b82" />

Roles con acceso:

ROLE_COMPANY_SUPER_ADMIN: Edición completa

ROLE_COMPANY_ADMIN: Lectura o edición parcial

ROLE_SUPER_ADMIN: Visualización global

ROLE_USER: Sin acceso

## 👤 Perfil de Usuario

Permite al usuario modificar su información personal y cambiar su contraseña.

<img width="1825" height="685" alt="image" src="https://github.com/user-attachments/assets/e5adbfe9-f491-4dee-b0cc-d7c5c9327663" />

Roles con acceso: Todos los usuarios autenticados.

## 🚪 Cierre de sesión

El cierre de sesión se realiza desde el menú del Header.
Al cerrar sesión, el token JWT se elimina del localStorage y el usuario es redirigido al login.

<img width="89" height="96" alt="image" src="https://github.com/user-attachments/assets/fdd9d274-76f2-4bde-b176-42e74d1429ee" />

<img width="728" height="343" alt="image" src="https://github.com/user-attachments/assets/a01ecb1e-0bb5-4d2a-a2cb-88b74dd589f0" />

---

© 2025 **LedgerLy** — Proyecto personal desarrollado por *Pablo Barreda*.
