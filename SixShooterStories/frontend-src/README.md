# Frontend Development

This directory contains the frontend source code for the Six Shooter Stories application.

## Setup

1. Navigate to the frontend-src directory:
   ```bash
   cd frontend-src
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

## Development

### Development Mode
To run the frontend in development mode with hot reload:
```bash
npm start
```
This will start a development server at http://localhost:3000 with proxy to the backend API.

### Build for Production
To build the frontend for production:
```bash
npm run build
```
This will build the frontend and place the files in `src/main/resources/static`.

### Watch Mode
To build in development mode and watch for changes:
```bash
npm run dev
```

## Integration with Spring Boot

The frontend is configured to build automatically when you run:
```bash
mvn clean package
```

The built files will be placed in `src/main/resources/static` and served by Spring Boot.

## Project Structure

```
frontend-src/
├── src/
│   ├── index.html          # Main HTML template
│   ├── index.js            # Main application entry point
│   ├── styles/
│   │   └── main.css        # Application styles
│   └── services/
│       ├── StoryService.js # API service for stories
│       └── AuthService.js  # API service for authentication
├── package.json            # Node.js dependencies
└── webpack.config.js       # Webpack configuration
```

## API Integration

The frontend is configured to make API calls to `/api/*` endpoints, which will be proxied to your Spring Boot backend during development and served directly in production.
