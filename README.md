# Purpose Combo Shop v2 (Fuller demo)

## What’s improved vs v1
- DB-backed users + registration (BCrypt password)
- Role-based access (USER / ADMIN)
- Product images (URL), product edit + delete
- Order status + Cancel order (before return/refund)
- Return window rule (default 7 days)
- Admin return workflow (APPROVE/REJECT/REFUND)
- Cleaner UI + security-aware navbar

## Run
```bash
mvn spring-boot:run
```
Open: http://localhost:8080

## Default admin
- admin / admin

Users can self-register.

## H2 console
http://localhost:8080/h2
JDBC URL: jdbc:h2:mem:combo
