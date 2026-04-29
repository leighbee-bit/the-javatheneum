# 📚 Javatheneum - Digital Bookstore

## Project Title
Javatheneum — A Cloud-Powered Digital Bookstore

## Cloud Platform Used
**Amazon Web Services (AWS)** — Amazon RDS (Relational Database Service)

## Description
Javatheneum is a full-stack digital bookstore application that allows users to create accounts, browse a library of 130+ books populated via the Google Books API, view detailed book information, access in-app book previews, place orders, and save books to a personal wishlist. The application demonstrates cloud computing concepts through a live PostgreSQL database hosted on Amazon RDS, connected to a Spring Boot REST API backend and a JavaFX desktop frontend with a Windows 98-inspired UI.

## Tech Stack
| Layer | Technology |
|---|---|
| **Frontend** | JavaFX (Java) |
| **Backend** | Spring Boot REST API (Java) |
| **Database** | PostgreSQL |
| **Cloud** | Amazon RDS |
| **External API** | Google Books API |
| **Security** | bcrypt password hashing |
| **Build Tool** | Maven |

## Services Used
| Service | Purpose |
|---|---|
| **Amazon RDS** | Cloud-hosted PostgreSQL database storing all users, books, wishlists and orders |
| **Google Books API** | Populates the book library with real metadata including titles, authors, descriptions, cover images, page counts and preview links |
| **Spring Boot** | REST API backend handling all business logic, authentication and database communication |
| **JavaFX** | Desktop frontend providing the user interface |
| **bcrypt** | Secure password hashing via Spring Security |

## Features
- User registration and login with bcrypt password hashing
- Browse 130+ books organized by genre in horizontal carousels
- Most Popular row based on wishlist count
- Search books by title or author
- View detailed book information including description, page count and price
- In-app Google Books preview via WebView
- Add books to a personal wishlist
- Place orders and view order history
- Windows 98 vaporwave-inspired UI with live clock taskbar

## Project Structure
```
bookstore_backend/          <- Spring Boot REST API
├── entities/               <- JPA entity classes
├── repositories/           <- Spring Data JPA repositories
├── services/               <- Business logic layer
└── controllers/            <- REST API endpoints

bookstore_frontend/         <- JavaFX desktop application
├── controllers/            <- UI screen controllers
├── models/                 <- Frontend data models
├── services/               <- API communication layer
└── resources/              <- FXML layout files
```

## Database Schema
| Table | Purpose |
|---|---|
| `users` | User account information |
| `books` | Book metadata from Google Books API |
| `orders` | User order history |
| `order_items` | Individual books within each order |
| `wishlists` | User wishlist containers |
| `wishlist_items` | Individual books within each wishlist |

## How It Works
The application follows a three-tier architecture:

**1. Frontend (JavaFX)**
The user interacts with a desktop application built in JavaFX. All data is fetched from the Spring Boot REST API via HTTP requests using Java's built-in HttpClient.

**2. Backend (Spring Boot)**
A Spring Boot application exposes REST endpoints for all operations. It handles bcrypt password hashing, business logic, and communicates with the cloud database. Built following the Repository, Service, Controller pattern.

**3. Database (PostgreSQL on Amazon RDS)**
All data is persisted in a PostgreSQL database hosted on Amazon RDS. The database is accessible from anywhere, making the app truly cloud-powered. Hibernate/JPA handles all SQL generation automatically.

## API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/register` | Register new user |
| POST | `/api/users/login` | Login with bcrypt verification |
| GET | `/api/books` | Get all books |
| GET | `/api/books/genre/{genre}` | Get books by genre |
| GET | `/api/books/popular` | Get most wishlisted books |
| GET | `/api/books/search/title` | Search by title |
| POST | `/api/orders` | Place an order |
| GET | `/api/orders/user/{id}` | Get user orders |
| POST | `/api/wishlists` | Create wishlist |
| POST | `/api/wishlist-items` | Add to wishlist |

## Setup & Installation

### Prerequisites
- Java 17+
- PostgreSQL (local) or Amazon RDS instance
- IntelliJ IDEA
- Maven

### Backend Setup
1. Clone the repository
2. Open `bookstore_backend` in IntelliJ
3. Set environment variables:
```
DB_URL=jdbc:postgresql://your-rds-endpoint:5432/postgres
DB_USERNAME=postgres
DB_PASSWORD=yourpassword
GOOGLE_BOOKS_API_KEY=your_api_key
```
4. Run `BookstoreBackendApplication.java`

### Frontend Setup
1. Open `bookstore_frontend` in IntelliJ
2. Update `ApiService.java` with your backend IP/URL
3. Run `BookstoreApplication.java`

## Environment Variables
| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `GOOGLE_BOOKS_API_KEY` | Google Books API key |

## Credits
- Book data provided by [Google Books API](https://developers.google.com/books)
- Pixel art logo: original artwork

## License
MIT License
