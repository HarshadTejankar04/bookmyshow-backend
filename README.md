# BookMyShow – Movie Ticket Booking Backend

A RESTful backend for a movie ticket booking platform, built with Java and Spring Boot. Supports movie browsing, show scheduling, seat-level booking with transactional integrity, JWT-based authentication, and AI-powered movie recommendations using a locally-hosted LLM.

## Features

- **Movie & Show Management** — Browse movies, view shows by movie/city/date range, manage theaters and screens
- **Seat Booking** — Real-time seat status tracking (AVAILABLE, LOCKED, BOOKED) with transactional integrity to prevent double-booking
- **Authentication & Authorization** — JWT-based login/register using Spring Security, with BCrypt password encryption
- **AI Movie Recommendations** — Natural language movie suggestions powered by Spring AI, using a locally-hosted LLM (Ollama) and live database records
- **Centralized Exception Handling** — Structured API error responses via `@ControllerAdvice`

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot, Spring Data JPA, Spring Security, Spring AI
- **Database:** MySQL
- **AI:** Ollama (locally-hosted LLM, e.g. Llama 3.2)
- **Auth:** JWT (jjwt)
- **Build Tool:** Maven

## Getting Started

### Prerequisites
- Java 21+
- Maven
- MySQL running locally
- [Ollama](https://ollama.com/download) installed, with a model pulled (e.g. `ollama pull llama3.2`)

### Environment Variables
This project requires the following environment variables to be set before running (see `.env.example`):

| Variable | Description |
|---|---|
| `DB_USERNAME` | Your local MySQL username |
| `DB_PASSWORD` | Your local MySQL password |
| `JWT_SECRET` | A random Base64-encoded secret for signing JWTs |

### Running Locally
```bash
git clone https://github.com/HarshadTejankar04/bms.git
cd bms

# Make sure Ollama is running in the background
ollama pull llama3.2

# Set the environment variables listed above (via your IDE run config or terminal export)
mvn spring-boot:run
```

The application starts on `http://localhost:8080`. A MySQL database `bms_db` will be created automatically on first run.

## API Overview

| Endpoint | Method | Auth Required | Description |
|---|---|---|---|
| `/api/auth/register` | POST | No | Register a new user |
| `/api/auth/login` | POST | No | Login, returns JWT token |
| `/api/movies` | GET | No | List all movies |
| `/api/movies/{id}` | GET | No | Get movie by ID |
| `/api/movies/recommend` | POST | Yes | Get AI-powered movie recommendations |
| `/api/shows` | GET | No | List all shows |
| `/api/shows/{id}` | GET | No | Get show by ID |
| `/api/shows/movie/{movieId}` | GET | No | List shows for a movie |
| `/api/shows/movie/{movieId}/city/{city}` | GET | No | List shows for a movie in a city |
| `/api/theaters` | GET | No | List all theaters |
| `/api/theaters/{id}` | GET | No | Get theater by ID |
| `/api/theaters/city/{city}` | GET | No | List theaters in a city |
| `/api/bookings` | POST | Yes | Book seats for a show |
| `/api/bookings/{id}` | GET | Yes | Get booking by ID |
| `/api/users/me` | GET | Yes | Get currently logged-in user |

## Architecture Notes

- Passwords are hashed with BCrypt before storage — never stored in plain text.
- JWT tokens are stateless (`SessionCreationPolicy.STATELESS`) — no server-side session storage.
- Movie/show/theater browsing is public (`GET`); booking and AI recommendations require authentication.
- The AI recommendation feature uses Spring AI's `ChatClient` abstraction, which decouples the application from any specific LLM provider — switching from a cloud provider to a local one (Ollama) required only a dependency and configuration change, with no changes to business logic.

## Notes

This is a personal learning project built to practice backend development with Spring Boot, Spring Security, and Spring AI.