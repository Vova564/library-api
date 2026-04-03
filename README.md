# Library API

REST API do zarządzania biblioteką — użytkownikami, książkami i wypożyczeniami.
Projekt napisany jako aplikacja portfolio prezentująca typowy backend produkcyjny.

## Uruchomienie

### Wymagania
- Docker Desktop

### Kroki

1. Sklonuj repozytorium
2. Zbuduj i uruchom wszystkie kontenery (backend jest konteryzowany wraz z bazą)
* docker-compose up -d  (w folderze projektu)
* lub odpalić plik docker-compose.yml w Intellij

Aplikacja startuje na http://localhost:8080  
Baza danych PostgreSQL działa na porcie 54421

## Autoryzacja

API używa JWT. Aby korzystać z chronionych endpointów:
1. Zarejestruj użytkownika: POST /auth/register
2. Zaloguj się: POST /auth/login — otrzymasz token
3. Dodaj nagłówek do kolejnych requestów: Authorization: Bearer <token>

## Inicjalizacja konta admina
W bazie automatycznie tworzony jest użytkownik z rolą admin:

* login: admin@example.com
* hasło: Haslo12345#

## Endpointy

| Metoda | Endpoint                 | Opis                                    | Rola      |
|--------|--------------------------|-----------------------------------------|-----------|
| POST   | /auth/register           | Rejestracja użytkownika                 | Publiczny |
| POST   | /auth/login              | Logowanie, zwraca JWT                   | Publiczny |
| GET    | /users/me                | Dane zalogowanego użytkownika           | USER      |
| PATCH  | /users/me                | Edycja własnych danych                  | USER      |
| DELETE | /users/me                | Usunięcie konta                         | USER      |
| GET    | /users                   | Dane wszystkich użytkowników            | ADMIN     |
| PATCH  | /users/{id}              | Edycja danych konkretnego użytkownika   | ADMIN     |
| DELETE | /users/{id}              | Usunięcie konta konkretnego użytkownika | ADMIN     |
| GET    | /books                   | Lista wszystkich książek                | USER      |
| GET    | /books/{id}              | Informacja konkretnej książki           | USER      |
| POST   | /books                   | Dodanie książki                         | ADMIN     |
| PATCH  | /books/{id}              | Edycja książki                          | ADMIN     |
| DELETE | /books/{id}              | Usunięcie książki                       | ADMIN     |
| POST   | /borrows/{bookId}        | Wypożyczenie książki                    | USER      |
| PATCH  | /borrows/{bookId}/return | Zwrot książki                           | USER      |
| GET    | /borrows/me              | Moje wypożyczenia                       | USER      |
| GET    | /borrows                 | Wszystkie wypożyczenia                  | ADMIN     |
| GET    | /borrows/{userId}        | Wypożyczenia konkretnego użetkownika    | ADMIN     |

Pełna kolekcja Postman znajduje sięw pliku: `Library.postman_collection.json`