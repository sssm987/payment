# Payment Consistency

결제 시스템에서 발생할 수 있는 **데이터 정합성 문제**를 단계적으로 해결하는 토이 프로젝트입니다.

## 목표

단순 CRUD 구현이 아닌, 실제 결제 시스템에서 발생하는 다양한 정합성 문제를 직접 구현하고 해결 과정을 기록합니다.

## 기술 스택

- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database
- WebClient
- Docker

## 프로젝트 구조

```
Order API
        │
        ▼
Order UseCase
        │
        ├── InventoryService
        ├── OrderService
        ├── PaymentService
        └── PaymentApiService
                    │
                    ▼
               Mock PG Server
```

## 시리즈

- DB 트랜잭션
- 보상 트랜잭션(Saga)
- 요청 이력 및 재시도
- Outbox Pattern
- CDC

## 브랜치

각 게시글은 독립적인 브랜치에서 관리됩니다.

| Branch | Description |
|--------|-------------|
| main | 프로젝트 기본 구조 |
| series/db-transaction | DB 트랜잭션 |
| series/compensation | 보상 트랜잭션 |
| series/retry | 요청 이력 및 재시도 |
| series/outbox | Outbox Pattern |
| series/cdc | CDC |
