# Ledgerly --- Project Roadmap

> **Project:** Ledgerly\
> **Description:** A secure, ledger-based digital wallet and
> payment-processing backend built with Java and Spring Boot.\
> **Current stack:** Java 27, Spring Boot 4.1.2, Maven, Spring MVC,
> Spring Data JPA, Hibernate, PostgreSQL, Spring Security, Bean
> Validation, Actuator, Lombok, JUnit 5, and Mockito.\
> **API documentation:** OpenAPI / Swagger UI\
> **Frontend:** Out of scope. Swagger UI and a Postman collection will
> be the primary ways to explore and test the API.

------------------------------------------------------------------------

## Project principles

1.  **Backend first:** Do not build a frontend. Spend the time on API
    design, correctness, security, reliability, testing, and deployment.
2.  **Build vertical slices:** Complete a small, working feature end to
    end before moving to the next one. Avoid creating every entity
    first, then every repository, then every service.
3.  **Understand before expanding:** Be able to explain each class,
    annotation, dependency, database relationship, and architectural
    decision.
4.  **Keep financial records trustworthy:** Use exact monetary
    representations, database transactions, immutable ledger entries,
    and explicit concurrency controls.
5.  **Test behaviour, not just implementation:** Include unit,
    integration, API, and failure-path tests.
6.  **Deploy incrementally:** Get a minimal application running early,
    then deploy meaningful slices as they become stable.
7.  **Keep scope honest:** This is a learning and portfolio project that
    simulates wallet/payment flows. It is not a real bank, card network,
    regulated payment processor, or production custody system.

## Target architecture

Use a modular, layered monolith initially. Keep modules separated by
business capability while avoiding premature microservices.

``` text
in.dhruvsood.ledgerly
├── config
├── common
│   ├── exception
│   └── response
├── auth
│   ├── controller
│   ├── service
│   └── dto
├── user
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
├── wallet
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
├── transaction
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── dto
└── ledger
    ├── service
    ├── repository
    └── entity
```

The exact package structure may evolve as the domain becomes clearer.
Keep the main application class in `in.dhruvsood.ledgerly` so component
scanning can discover the feature packages beneath it.

------------------------------------------------------------------------

# Phases

## Phase 0 --- Project foundation and environment

**Goal:** Confirm the generated project builds and understand how Spring
Boot starts.

### Tasks

-   [ ] Confirm JDK 27 is installed.
-   [ ] Set IntelliJ Project SDK, language level, Maven importer JDK,
    and Maven runner JRE to JDK 27.
-   [ ] Confirm `java -version` and `.\mvnw.cmd -version` report the
    intended JDK.
-   [ ] Review the generated `pom.xml` and understand the purpose of
    each dependency and plugin.
-   [ ] Run `.\mvnw.cmd clean test`.
-   [ ] Inspect `LedgerlyApplication.java`.
-   [ ] Learn `@SpringBootApplication`, `SpringApplication.run()`,
    component scanning, auto-configuration, the IoC container, beans,
    and dependency injection.
-   [ ] Review `application.properties` and configuration profiles.
-   [ ] Initialize Git and make the first meaningful commit.

### Deliverable

A clean project that builds and runs locally, with the startup flow
understood.

### Exit checks

-   [ ] Maven build succeeds.
-   [ ] Application starts without errors.
-   [ ] Can explain what happens when `main()` invokes
    `SpringApplication.run()`.

------------------------------------------------------------------------

## Phase 1 --- Health endpoint, PostgreSQL, Docker, and first deployment

**Goal:** Establish a running service and database before adding
business features.

### Tasks

-   [ ] Add a simple application health endpoint or use the Actuator
    health endpoint.
-   [ ] Understand Actuator health indicators and what `UP` does---and
    does not---guarantee.
-   [ ] Create a PostgreSQL service in `docker-compose.yml`.
-   [ ] Configure database connection settings outside source control
    where appropriate.
-   [ ] Connect Spring Boot to PostgreSQL.
-   [ ] Verify database connectivity.
-   [ ] Understand datasource configuration, connection pools, and JPA
    startup.
-   [ ] Add a basic integration test for application/database
    connectivity.
-   [ ] Add a Dockerfile for the application.
-   [ ] Deploy the minimal service and verify its health endpoint.

### Deliverable

A deployed Spring Boot application connected to PostgreSQL.

### Exit checks

-   [ ] Application starts locally and in the deployment environment.
-   [ ] Database connectivity is verified.
-   [ ] Secrets are not committed to Git.
-   [ ] Health checks are documented.

------------------------------------------------------------------------

## Phase 2 --- User registration and authentication

**Goal:** Create users safely and protect private API routes.

### Tasks

-   [ ] Define the user domain and user entity.
-   [ ] Understand entity mapping, primary keys, constraints, and
    database migrations.
-   [ ] Add a migration tool and create the initial schema migration.
-   [ ] Create registration request and response DTOs.
-   [ ] Add Bean Validation constraints to request DTOs.
-   [ ] Implement repository and registration service.
-   [ ] Hash passwords using an appropriate password encoder; never
    store plaintext passwords.
-   [ ] Enforce unique email addresses at the database level as well as
    in application logic.
-   [ ] Implement login.
-   [ ] Add JWT creation and verification.
-   [ ] Configure Spring Security's filter chain and request
    authorization.
-   [ ] Add an authenticated endpoint that returns the current user's
    profile.
-   [ ] Test registration, login, invalid credentials, duplicate email,
    and unauthorized access.

### Deliverable

Users can register and log in, and protected endpoints require valid
authentication.

### Exit checks

-   [ ] Passwords are stored as hashes.
-   [ ] JWT validation and expiration are tested.
-   [ ] Authorization is based on the authenticated user, not a user ID
    trusted from the request.
-   [ ] Validation and authentication errors have consistent responses.

------------------------------------------------------------------------

## Phase 3 --- Wallet creation and balance enquiry

**Goal:** Give each registered user a wallet and expose a safe balance
endpoint.

### Tasks

-   [ ] Define the wallet entity and its relationship to the user.
-   [ ] Decide and enforce the one-wallet-per-user rule.
-   [ ] Create a wallet automatically as part of registration.
-   [ ] Use database constraints to protect important invariants.
-   [ ] Decide the supported currency model. Start with one explicitly
    supported currency if multi-currency is not yet required.
-   [ ] Represent money with `BigDecimal` and an explicit currency;
    never use `float` or `double` for monetary values.
-   [ ] Define precision and scale rules and validate amounts.
-   [ ] Implement the authenticated balance endpoint.
-   [ ] Test wallet creation, ownership, and balance retrieval.

### Deliverable

Every registered user receives a wallet and can retrieve only their own
wallet balance.

### Exit checks

-   [ ] Wallet creation is atomic with registration.
-   [ ] A user cannot access another user's wallet by changing an
    identifier.
-   [ ] Monetary precision and currency behaviour are documented.

------------------------------------------------------------------------

## Phase 4 --- Deposits, withdrawals, and transfers

**Goal:** Implement the core wallet operations with correct transaction
boundaries.

### Tasks

-   [ ] Define transaction types and transaction statuses.
-   [ ] Design request/response DTOs for deposits, withdrawals, and
    transfers.
-   [ ] Define the rules for a simulated deposit source; do not imply
    that real money has moved.
-   [ ] Implement deposit processing.
-   [ ] Implement withdrawal processing.
-   [ ] Implement wallet-to-wallet transfers.
-   [ ] Use `@Transactional` at appropriate service boundaries.
-   [ ] Ensure a transfer either completes fully or has no financial
    effect.
-   [ ] Prevent negative amounts and insufficient-funds
    withdrawals/transfers.
-   [ ] Record transaction timestamps and useful references.
-   [ ] Implement transaction history with pagination and sorting.
-   [ ] Test successful operations and failure cases, including rollback
    behaviour.

### Deliverable

Authenticated users can perform simulated deposits, withdrawals, and
transfers and inspect their transaction history.

### Exit checks

-   [ ] Failed transfers do not partially debit or credit wallets.
-   [ ] Insufficient funds are rejected.
-   [ ] Transaction history is scoped to the authenticated user.
-   [ ] Monetary operations use exact decimal handling.

------------------------------------------------------------------------

## Phase 5 --- Double-entry ledger and reconciliation

**Goal:** Make the ledger the auditable record of financial movement.

### Tasks

-   [ ] Learn the accounting model behind double-entry bookkeeping.
-   [ ] Design immutable ledger entries.
-   [ ] Represent each transfer with balanced debit and credit entries.
-   [ ] Define how deposits and withdrawals are represented using
    appropriate system/clearing accounts in the simulation.
-   [ ] Enforce that each posted transaction balances to zero within its
    currency.
-   [ ] Link ledger entries to the business transaction/reference that
    created them.
-   [ ] Prevent normal application flows from editing or deleting posted
    ledger entries.
-   [ ] Decide how wallet balances are derived or maintained as a
    performance-oriented projection.
-   [ ] Implement reconciliation between wallet balances and ledger
    totals.
-   [ ] Test balanced entries, duplicate posting prevention, and
    reconciliation failures.

### Deliverable

Every completed financial operation has an auditable, balanced ledger
representation.

### Exit checks

-   [ ] Every posted operation creates the required balanced entries
    atomically.
-   [ ] Posted ledger entries cannot be silently modified or deleted.
-   [ ] Reconciliation can detect a mismatch.
-   [ ] The balance source of truth and any cached balance strategy are
    documented.

------------------------------------------------------------------------

## Phase 6 --- Concurrency, idempotency, and reliability

**Goal:** Protect financial operations against simultaneous requests and
retries.

### Tasks

-   [ ] Reproduce a race condition with concurrent transfers from the
    same wallet.
-   [ ] Understand database isolation levels and lost updates.
-   [ ] Compare optimistic locking (`@Version`) and pessimistic locking
    (`SELECT ... FOR UPDATE`).
-   [ ] Choose a locking strategy and document why it fits the transfer
    workflow.
-   [ ] Ensure transfers acquire locks in a consistent order to reduce
    deadlock risk.
-   [ ] Add an idempotency-key mechanism for retryable financial
    requests.
-   [ ] Store the idempotency key, authenticated owner, operation scope,
    request identity, and resulting outcome.
-   [ ] Define behaviour for repeated keys with the same request and
    repeated keys with a different request.
-   [ ] Ensure the idempotency record and financial operation are
    committed atomically.
-   [ ] Add concurrency and retry integration tests.
-   [ ] Consider bounded retry behaviour where the chosen concurrency
    strategy requires it.

### Deliverable

Concurrent requests cannot overspend a wallet, and retries do not create
duplicate financial operations.

### Exit checks

-   [ ] Concurrent transfer tests preserve wallet and ledger invariants.
-   [ ] Repeating an idempotent request does not execute it twice.
-   [ ] Reusing a key for a different request is rejected.
-   [ ] Locking and retry decisions are documented.

------------------------------------------------------------------------

## Phase 7 --- API quality, security hardening, and automated testing

**Goal:** Make the API predictable, documented, and demonstrably
reliable.

### Tasks

-   [ ] Implement a consistent API response approach where useful.
-   [ ] Implement global exception handling with
    `@RestControllerAdvice`.
-   [ ] Define stable error response fields and appropriate HTTP status
    codes.
-   [ ] Review validation at DTO, service, and database levels.
-   [ ] Add pagination, sorting, and appropriate filtering.
-   [ ] Review authorization and ownership checks on every private
    resource.
-   [ ] Review CORS, CSRF, security headers, secret handling, and
    actuator endpoint exposure.
-   [ ] Add unit tests for business rules.
-   [ ] Add repository and service integration tests using PostgreSQL.
-   [ ] Add API/security tests for successful and rejected requests.
-   [ ] Test transaction rollback, concurrency, idempotency, and ledger
    reconciliation.
-   [ ] Add OpenAPI documentation and Swagger UI.
-   [ ] Configure JWT bearer authentication in Swagger UI.
-   [ ] Export a Postman collection and environment template.

### Deliverable

A documented API with meaningful automated test coverage and consistent
error handling.

### Exit checks

-   [ ] Important business rules have automated tests.
-   [ ] Tests cover both success and failure paths.
-   [ ] Swagger documents request/response models and authentication.
-   [ ] Postman collection can demonstrate the main workflows.
-   [ ] Sensitive operational endpoints are not publicly exposed.

------------------------------------------------------------------------

## Phase 8 --- Deployment, observability, and portfolio polish

**Goal:** Make Ledgerly easy to run, inspect, and evaluate.

### Tasks

-   [ ] Finalize Docker image and Compose configuration for local
    development.
-   [ ] Configure production environment variables and secrets.
-   [ ] Deploy the application and managed or separately hosted
    PostgreSQL database.
-   [ ] Configure database migrations for deployment.
-   [ ] Configure health checks and useful Actuator metrics.
-   [ ] Add structured, useful application logging without logging
    passwords, tokens, or sensitive payment data.
-   [ ] Verify Swagger UI is reachable at the deployed URL.
-   [ ] Run an API smoke test against the deployed environment.
-   [ ] Perform a modest load/concurrency test and document the test
    setup and results honestly.
-   [ ] Write a README with project purpose, architecture,
    prerequisites, setup, environment variables, API documentation, and
    test instructions.
-   [ ] Add an architecture diagram and a database relationship diagram.
-   [ ] Document important engineering decisions and trade-offs.
-   [ ] Add screenshots or links to Swagger UI and example API
    workflows.
-   [ ] Prepare a concise, truthful resume description.

### Deliverable

A deployed, documented backend project that another developer can run
and evaluate.

### Exit checks

-   [ ] A clean setup works from the README.
-   [ ] Deployment health checks pass.
-   [ ] Swagger UI and Postman examples work.
-   [ ] Architecture and financial invariants are explained.
-   [ ] Performance claims include reproducible test context.

------------------------------------------------------------------------

# Core engineering decisions to preserve

These are non-negotiable design principles for the financial parts of
Ledgerly.

  -----------------------------------------------------------------------
  Concern                             Decision / principle
  ----------------------------------- -----------------------------------
  Money                               Use `BigDecimal`, explicit
                                      currency, and defined
                                      precision/scale. Never use
                                      floating-point types.

  Ledger                              Use balanced, immutable
                                      debit/credit entries.

  Atomicity                           A financial operation and its
                                      related ledger records must commit
                                      or roll back together.

  Concurrency                         Select and test an explicit locking
                                      strategy.

  Idempotency                         Support safe retries for operations
                                      that must not execute twice.

  Authorization                       Derive the acting user from the
                                      authenticated security context.

  Validation                          Validate request shape at the
                                      boundary and enforce business rules
                                      in the service/domain layer.

  Database integrity                  Use constraints as a final
                                      protection for critical invariants.

  Auditability                        Preserve transaction references,
                                      timestamps, and ledger history.

  Testing                             Test invariants and failure
                                      scenarios, not only happy paths.
  -----------------------------------------------------------------------

## Suggested API shape

The final routes may change as the domain evolves. This is an initial
guide, not a promise that every route must be implemented exactly as
written.

  ------------------------------------------------------------------------------
  Method                  Example endpoint               Purpose
  ----------------------- ------------------------------ -----------------------
  POST                    `/api/v1/auth/register`        Register a user

  POST                    `/api/v1/auth/login`           Authenticate a user

  GET                     `/api/v1/users/me`             Retrieve current user's
                                                         profile

  GET                     `/api/v1/wallets/me`           Retrieve current user's
                                                         wallet

  GET                     `/api/v1/wallets/me/balance`   Retrieve wallet balance

  POST                    `/api/v1/deposits`             Simulate a deposit

  POST                    `/api/v1/withdrawals`          Request a withdrawal

  POST                    `/api/v1/transfers`            Transfer between
                                                         wallets

  GET                     `/api/v1/transactions`         List current user's
                                                         transactions

  GET                     `/api/v1/transactions/{id}`    Retrieve an authorized
                                                         transaction
  ------------------------------------------------------------------------------

## Learning checklist

Use this checklist to track concepts you can explain, not merely topics
you have watched or read.

-   [ ] Maven lifecycle, dependencies, scopes, and plugins
-   [ ] Spring Boot startup and auto-configuration
-   [ ] IoC container, beans, component scanning, and dependency
    injection
-   [ ] REST controllers, HTTP methods, status codes, and DTOs
-   [ ] Bean Validation and global exception handling
-   [ ] Java records, enums, collections, generics, and exception
    handling
-   [ ] JPA entities, relationships, repositories, and Hibernate
-   [ ] SQL constraints, indexes, and database migrations
-   [ ] Spring Security filter chain and JWT
-   [ ] Password hashing and authorization
-   [ ] `@Transactional` and transaction propagation
-   [ ] `BigDecimal` and monetary precision
-   [ ] Double-entry bookkeeping and reconciliation
-   [ ] Isolation levels, locking, race conditions, and deadlocks
-   [ ] Idempotency and safe retries
-   [ ] Unit tests, integration tests, and API tests
-   [ ] OpenAPI / Swagger and Postman
-   [ ] Docker, deployment, health checks, and observability

## Working agreement

For each phase, follow this cycle:

1.  **Understand:** Learn the concept and why Ledgerly needs it.
2.  **Design:** Agree on the data model, API contract, and
    responsibilities.
3.  **Implement:** Create one file at a time and understand every
    important line.
4.  **Run:** Start the application and inspect the result.
5.  **Test:** Verify success cases, invalid inputs, and failure cases.
6.  **Review:** Discuss trade-offs and likely interview questions.
7.  **Record:** Update this roadmap and the engineering notes when a
    milestone is complete.

Do not mark a phase complete just because its classes exist. Mark it
complete when its exit checks pass and you can explain the
implementation.

------------------------------------------------------------------------

## Current progress

### Phase 0 — Project foundation and environment

- [x] Project generated in IntelliJ IDEA.
- [x] Project named `ledgerly`.
- [x] Maven and Spring Boot project structure created.
- [x] Core dependencies selected.
- [x] Java version property updated to `27`.
- [x] Confirm JDK 27 is installed.
- [x] Confirm `java -version` and `javac -version` report Java 27.
- [x] Confirm Maven Wrapper is using Java 27.
- [x] Review the generated `pom.xml` and understand the purpose of its core dependencies.
- [x] Run `.\mvnw.cmd clean compile` successfully.
- [x] Inspect `LedgerlyApplication.java`.
- [x] Understand the purpose of `@SpringBootApplication` and `SpringApplication.run()`.
- [x] Understand the initial application startup flow and the role of the main application class.
- [x] Complete the initial `.\mvnw.cmd clean test` successfully (previously failed because the datasource was not configured).
- [x] Learn the IoC container, beans, component scanning, and dependency injection in depth.
- [x] Review `application.properties` and configuration profiles.
- [ ] Initialize Git and make the first meaningful commit.

### Phase 1 — Health endpoint, PostgreSQL, Docker, and first deployment

- [x] Confirm Docker Desktop is installed and running.
- [x] Create the initial PostgreSQL Docker Compose configuration.
- [x] Understand Docker images, containers, and Docker Compose fundamentals.
- [x] Understand Docker restart policies, including `unless-stopped`.
- [x] Understand Docker health checks and the `pg_isready` utility.
- [ ] Start the PostgreSQL container using `docker compose up -d`.
- [ ] Verify PostgreSQL container status and health.
- [ ] Configure database connection settings outside source control where appropriate.
- [ ] Connect Spring Boot to PostgreSQL.
- [ ] Verify database connectivity.
- [ ] Understand datasource configuration, connection pools, and JPA startup.
- [ ] Add a basic integration test for application/database connectivity.
- [ ] Add a Dockerfile for the application.
- [ ] Add a simple application health endpoint or use the Actuator health endpoint.
- [ ] Understand Actuator health indicators and what `UP` does—and does not—guarantee.
- [ ] Deploy the minimal service and verify its health endpoint.

**Next milestone:** Start PostgreSQL using Docker Compose, verify that the container becomes healthy, and configure Spring Boot to connect to the database.

