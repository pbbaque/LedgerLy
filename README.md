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

LedgerLy is a portfolio demo project. The main application flow is implemented and the project is suitable for local review or demo deployment after configuring the database and environment variables. Some areas, such as automated test coverage and production deployment automation, are intentionally kept minimal.

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
DB_NAME=ledgerly
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_base64_encoded_secret
```

Run the API:

```bash
cd backend
./mvnw spring-boot:run
```

The API runs at:

```text
http://localhost:8080/api
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
http://localhost:8080/api
```

## Useful Commands

Frontend:

```bash
npm start
npm run build
npm test
```

Backend:

```bash
./mvnw spring-boot:run
./mvnw test
```

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

- Backend starts with a clean database configuration.
- Frontend builds successfully.
- Login flow works with a demo user.
- Dashboard loads without console errors.
- Main CRUD screens open correctly.
- Empty states and error states are acceptable for a public demo.
- README reflects the actual setup and limitations.

## License

This project is distributed under the MIT License. See `LICENSE` for details.
