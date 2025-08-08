# LMS Backend

A Spring Boot Learning Management System backend application.

## Features

- User authentication and authorization with JWT
- Course management
- Category management
- Enrollment system
- File upload to Supabase S3
- RESTful API with Swagger documentation

## Technology Stack

- Java 17
- Spring Boot 3.5.3
- Spring Security
- PostgreSQL (Supabase)
- Maven
- JWT for authentication

## Local Development

### Prerequisites

- Java 17
- Maven
- PostgreSQL database

### Running Locally

1. Clone the repository
2. Configure your database connection in `application.properties`
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Deployment to Render

### Prerequisites

1. A Render account
2. A PostgreSQL database (you can use Supabase or Render's PostgreSQL service)
3. Your application code pushed to a Git repository

### Deployment Steps

1. **Connect your repository to Render:**
   - Go to [Render Dashboard](https://dashboard.render.com)
   - Click "New +" and select "Web Service"
   - Connect your Git repository

2. **Configure the service:**
   - **Name:** lms-backend
   - **Environment:** Java
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/lms-0.0.1-SNAPSHOT.jar`

3. **Set Environment Variables:**
   - `SPRING_PROFILES_ACTIVE`: `production`
   - `SERVER_PORT`: `8080`
   - `SPRING_DATASOURCE_URL`: Your PostgreSQL connection URL
   - `SPRING_DATASOURCE_USERNAME`: Your database username
   - `SPRING_DATASOURCE_PASSWORD`: Your database password
   - `SUPABASE_S3_ENDPOINT`: Your Supabase S3 endpoint
   - `SUPABASE_S3_REGION`: Your S3 region
   - `SUPABASE_S3_ACCESS_KEY`: Your S3 access key
   - `SUPABASE_S3_SECRET_KEY`: Your S3 secret key
   - `SUPABASE_S3_BUCKET`: Your S3 bucket name

4. **Deploy:**
   - Click "Create Web Service"
   - Render will automatically build and deploy your application

### Using render.yaml (Alternative)

If you prefer using the `render.yaml` file:

1. Push your code with the `render.yaml` file to your repository
2. In Render dashboard, select "New +" → "Blueprint"
3. Connect your repository
4. Render will automatically detect and use the `render.yaml` configuration

## API Documentation

Once deployed, you can access the Swagger UI at:
`https://your-app-name.onrender.com/swagger-ui.html`

## Environment Variables

Make sure to set these environment variables in your Render dashboard:

- **Database Configuration:**
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`

- **Supabase S3 Configuration:**
  - `SUPABASE_S3_ENDPOINT`
  - `SUPABASE_S3_REGION`
  - `SUPABASE_S3_ACCESS_KEY`
  - `SUPABASE_S3_SECRET_KEY`
  - `SUPABASE_S3_BUCKET`

## Troubleshooting

1. **Build Failures:**
   - Check that Java 17 is being used
   - Ensure all dependencies are properly configured in `pom.xml`

2. **Database Connection Issues:**
   - Verify your database is accessible from Render's servers
   - Check that your database credentials are correct
   - Ensure your database allows connections from external IPs

3. **Application Startup Issues:**
   - Check the logs in Render dashboard
   - Verify all required environment variables are set
   - Ensure the port configuration is correct

## Security Notes

- Never commit sensitive information like database passwords or API keys
- Use environment variables for all sensitive configuration
- Consider using Render's built-in PostgreSQL service for better security
