# Library API

REST API do zarządzania biblioteką — użytkownikami, książkami i wypożyczeniami.
Projekt napisany jako aplikacja portfolio prezentująca typowy backend produkcyjny.

## Tech stack

- Java 21, Spring Boot 4.0
- Spring Security + JWT
- PostgreSQL, Spring Data JPA
- Argon2 (własna implementacja password encodera)
- Docker, Docker Compose
- JUnit 5, Mockito, AssertJ

## Uruchomienie

### Wymagania
- Docker Desktop

### Kroki

1. Sklonuj repozytorium
2. Zbuduj i uruchom wszystkie kontenery (backend jest konteryzowany z bazą)

Aplikacja startuje na http://localhost:8080  
Baza danych PostgreSQL działa na porcie 54421

## Autoryzacja

API używa JWT. Aby korzystać z chronionych endpointów:
1. Zarejestruj użytkownika: POST /auth/register
2. Zaloguj się: POST /auth/login — otrzymasz token
3. Dodaj nagłówek do kolejnych requestów: Authorization: Bearer <token>

## Endpointy

| Metoda | Endpoint              | Opis                          | Rola       |
|--------|-----------------------|-------------------------------|------------|
| POST   | /auth/register        | Rejestracja użytkownika       | Publiczny  |
| POST   | /auth/login           | Logowanie, zwraca JWT         | Publiczny  |
| GET    | /users/me             | Dane zalogowanego użytkownika | USER       |
| PATCH  | /users/me             | Edycja własnych danych        | USER       |
| DELETE | /users/me             | Usunięcie konta               | USER       |
| GET    | /books                | Lista wszystkich książek      | USER       |
| POST   | /books                | Dodanie książki               | ADMIN      |
| PATCH  | /books/{id}           | Edycja książki                | ADMIN      |
| DELETE | /books/{id}           | Usunięcie książki             | ADMIN      |
| POST   | /borrows/{bookId}     | Wypożyczenie książki          | USER       |
| PATCH  | /borrows/{bookId}/return | Zwrot książki              | USER       |
| GET    | /borrows/me           | Moje wypożyczenia             | USER       |

Pełna kolekcja Postman: `Library.postman_collection.json`