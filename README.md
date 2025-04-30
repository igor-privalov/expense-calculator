# Expense Calculator

A full-stack application for managing and calculating expenses, built with React for the frontend and Spring Boot for the backend.

[![GitHub Repository](https://img.shields.io/badge/GitHub-Repository-blue)](https://github.com/igor-privalov/expense-calculator)

## Project Overview

This project is part of the AI In Development course homework, focusing on three main tasks:
1. Web Application: Expense Calculator (implemented in this repository)
2. API Testing: Identifying Defects in Product Data
3. SQL Queries: Analyzing a Database Online

For detailed information about all tasks, please refer to [README.md](https://github.com/igor-privalov/ai-in-development-homework/blob/main/README.md).

## Prerequisites
- Node.js (v20 or higher)
- Java (21 or higher)
- Maven (3.8 or higher)
- Docker and Docker Compose
- MongoDB (if running locally)

## Project Structure

```
expense-calculator/
├── frontend/          # React frontend application
├── backend/           # Spring Boot backend application
└── docker-compose.yml # Docker Compose configuration
```

## Running the Application

### Option 1: Using Docker Compose (Recommended)

1. Clone the repository and navigate to the project directory:
```bash
cd expense-calculator
```

2. Start all services:
```bash
docker-compose up -d
```

The application will be available at:
- Frontend: http://localhost:3000
- Backend API: http://localhost:9090
- MongoDB: localhost:27017

### Option 2: Running Frontend Separately

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will be available at http://localhost:3000

### Option 3: Running Backend Separately

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the application:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend API will be available at http://localhost:9090

## Environment Variables

### Backend
- `SPRING_DATA_MONGODB_URI`: MongoDB connection string (default: mongodb://localhost:27017/expensio)

### Frontend
- `REACT_APP_API_URL`: Backend API URL (default: http://localhost:9090)

## Development

### Frontend Development
- Uses React with TypeScript
- Styled with modern CSS frameworks
- Implements responsive design

### Backend Development
- Built with Spring Boot
- Uses MongoDB as the database
- Implements RESTful API design
- Includes comprehensive error handling 