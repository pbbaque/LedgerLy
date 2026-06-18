# LedgerLy

LedgerLy is a full-stack web application for invoice and business operations management. It combines an Angular frontend with a Spring Boot REST API to manage companies, clients, employees, products, users and invoices in a role-based environment.

This repository is organized as a monorepo:

```text
frontend/  Angular application
backend/   Spring Boot API
```

## Tech Stack

| Layer | Technologies |
| --- | --- |
| Frontend | Angular 20, TypeScript, RxJS, SCSS, Chart.js, ng2-charts, Swiper |
| Backend | Java 23, Spring Boot 3.5, Spring Security, JWT, Spring Data JPA, Maven |
| Database | MySQL |
| Tooling | npm, Maven Wrapper |

## Main Features

- JWT authentication and role-based access control.
- Dashboard with invoice KPIs and business summaries.
- CRUD flows for companies, clients, employees, products, users and invoices.
- Multi-company data model with permission checks in the backend.
- Modular Angular structure with feature modules, guards, interceptors and reusable UI components.
- Layered backend structure with controllers, services, repositories, validators and orchestrators.

## Project Status

LedgerLy is a portfolio demo project, not a production SaaS product. The main application flow is implemented and the project is suitable for local review, technical evaluation and recorded demos after configuring the database and environment variables.

The current demo setup includes frontend and backend tests covering core UI behavior, service access rules, JWT handling, permission checks and utility classes. Production deployment automation, payment integrations and advanced enterprise hardening are intentionally out of scope.

## Requirements

- Node.js 22 or compatible version.
- npm 10 or compatible version.
- Java 23.
- MySQL 8.
- Maven Wrapper, included in `backend/`.

## Backend Setup

Create a local Spring configuration from the example file:

```bash
cd backend
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

Configure the required values:

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=invoice_manager
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_base64_encoded_secret
SPRING_PROFILES_ACTIVE=invoice
JPA_SHOW_SQL=true
JPA_HIBERNATE_DDL_AUTO=validate
```

`application.properties` is intentionally ignored by Git. Keep real local credentials and JWT secrets out of version control.

Run the API:

```bash
cd backend
./mvnw spring-boot:run
```

For the local portfolio demo, the API runs at:

```text
http://localhost:8081/api
```

## Frontend Setup

Install dependencies and start the Angular app:

```bash
cd frontend
npm install
npm start
```

The frontend runs at:

```text
http://localhost:4200
```

The frontend expects the backend API at:

```text
http://localhost:8081/api
```

## Useful Commands

Frontend:

```bash
npm start
npm run build
npm test -- --watch=false --browsers=ChromeHeadless
```

Backend:

```bash
./mvnw spring-boot:run
./mvnw test
./mvnw package
```

## Testing

Current verification status:

| Area | Command | Current Result |
| --- | --- | --- |
| Frontend tests | `npm test -- --watch=false --browsers=ChromeHeadless` | 39 passing tests |
| Frontend build | `npm run build` | Builds successfully |
| Backend tests | `./mvnw test` | 14 passing tests |
| Backend package | `./mvnw package` | Builds the Spring Boot JAR successfully |

The test suite focuses on portfolio-relevant behavior: Angular component rendering, password visibility toggle, company access by user role, invoice subtotal/tax calculations, JWT generation/validation, permission checks and backend utility classes.

## Environment Variables

The backend requires database credentials and a JWT secret. See:

```text
backend/.env.example
backend/src/main/resources/application-example.properties
```

Email and OAuth settings are optional for local demo unless password recovery or Google login are being tested.

## Deployment Recommendation

For a free portfolio demo:

- Frontend: Vercel or Netlify.
- Backend: Render, Railway or Fly.io.
- Database: a managed MySQL-compatible service supported by the selected backend host.

For public deployment, configure HTTPS, production CORS origins, a strong JWT secret and real environment variables instead of local files.

## Demo Checklist

- Backend starts with local database configuration on port `8081`.
- Frontend builds successfully.
- Login flow works with a demo user.
- Dashboard loads without console errors.
- Main CRUD screens open correctly.
- Empty states and error states are acceptable for a public demo.
- README reflects the actual setup and limitations.

## License

This project is distributed under the MIT License. See `LICENSE` for details.
