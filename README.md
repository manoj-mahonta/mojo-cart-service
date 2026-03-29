# mojo-cart-service
MOJOCART is an e-commerce backend that supports user registration, login, and profile management. It allows browsing products, searching with pagination, and viewing details. Users can place orders, track order history, and ensures stock management by preventing out-of-stock purchases.

## Environment Configuration

This application uses external environment variables for sensitive configuration.

### Setup

1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` with your actual database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://your-host:5432/your-database?sslmode=require
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

### Security

- `.env` file is included in `.gitignore` and will not be committed to version control
- `.env.example` is provided as a template for required properties
- Never commit sensitive credentials to the repository

## Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using IDE
Run `MojoCartServiceApplication.java` directly from your IDE.

## Health Checks

- Application health: `GET http://localhost:8080/api/health`
- Database connectivity: `GET http://localhost:8080/api/health/database`

## API Documentation

See `API-DOCUMENTATION.md` for detailed API endpoints and usage examples.
