# School UI Configuration

This document describes the configuration settings for the School Management UI application, a React application built with Vite.

## Package.json

The project dependencies and scripts are defined in `package.json`:

### Scripts
- `npm run dev`: Starts the development server using Vite.
- `npm run build`: Builds the application for production.
- `npm run lint`: Runs ESLint to check for code quality issues.
- `npm run preview`: Previews the built application locally.

### Dependencies
- `react` and `react-dom`: Core React libraries for building the UI.
- `axios`: HTTP client for making API requests to the backend.

### DevDependencies
- `@vitejs/plugin-react`: Vite plugin for React support.
- `eslint` and related plugins: For code linting and quality checks.
- `@types/react` and `@types/react-dom`: TypeScript type definitions (though the project uses JavaScript).

## Vite Configuration

The Vite configuration is in `vite.config.js`:

```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
})
```

This basic configuration enables React support with Hot Module Replacement (HMR) for fast development.

## ESLint Configuration

ESLint is configured in `eslint.config.js` with rules for React, including hooks and refresh plugins:

- Uses recommended JS rules.
- Includes React hooks recommendations.
- Configured for browser globals and JSX parsing.
- Ignores the `dist` directory.

## API Configuration

The application communicates with the backend API. Currently, the API base URL is hardcoded in `src/App.jsx`:

```javascript
const API_BASE_URL = "https://school-management-c5dg.onrender.com/students";
```

For local development, you may need to change this to point to your local backend (e.g., `https://school-management-c5dg.onrender.com/students`).

To make it configurable, consider using environment variables:

1. Create a `.env` file in the root of the UI project.
2. Add: `VITE_API_BASE_URL=https://school-management-c5dg.onrender.com/students`
3. Update `App.jsx` to use: `const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "https://school-management-c5dg.onrender.com/students";`

## Dockerfile

The application is containerized using the provided `Dockerfile`:

- Uses Node.js 22 Alpine image.
- Installs dependencies and copies source code.
- Exposes port 5173 (Vite's default).
- Runs `npm run dev -- --host` to allow external access in Docker.

## Prerequisites

- Node.js 22 or higher
- npm (comes with Node.js)

## Running the Application

### Local Development
1. Install dependencies: `npm install`
2. Start the development server: `npm run dev`
3. Open http://localhost:5173 in your browser.

### Production Build
1. Build the application: `npm run build`
2. Preview locally: `npm run preview`

### Docker
The application can be run using Docker Compose. Refer to the root `docker-compose.yml` for details. The frontend service builds from this directory and runs on port 5173.

## Linting

Run `npm run lint` to check for code quality issues. Fix any reported problems to maintain code standards.
