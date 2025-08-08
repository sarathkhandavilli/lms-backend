# Deployment Guide for Render

## Quick Start

### Step 1: Prepare Your Repository
1. Make sure your code is pushed to a Git repository (GitHub, GitLab, etc.)
2. Ensure all the deployment files are in your repository:
   - `render.yaml`
   - `Dockerfile`
   - `src/main/resources/application-production.properties`

### Step 2: Deploy to Render

#### Option A: Using Render Dashboard (Recommended)

1. **Go to Render Dashboard**
   - Visit [https://dashboard.render.com](https://dashboard.render.com)
   - Sign up or log in

2. **Create New Web Service**
   - Click "New +" button
   - Select "Web Service"
   - Connect your Git repository

3. **Configure the Service**
   - **Name:** `lms-backend`
   - **Environment:** `Java`
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/lms-0.0.1-SNAPSHOT.jar`

4. **Set Environment Variables**
   Click "Advanced" and add these environment variables:

   **Required Variables:**
   ```
   SPRING_PROFILES_ACTIVE=production
   SERVER_PORT=8080
   ```

   **Database Configuration:**
   ```
   SPRING_DATASOURCE_URL=your_postgresql_connection_url
   SPRING_DATASOURCE_USERNAME=your_database_username
   SPRING_DATASOURCE_PASSWORD=your_database_password
   ```

   **Supabase S3 Configuration:**
   ```
   SUPABASE_S3_ENDPOINT=your_supabase_s3_endpoint
   SUPABASE_S3_REGION=ap-south-1
   SUPABASE_S3_ACCESS_KEY=your_s3_access_key
   SUPABASE_S3_SECRET_KEY=your_s3_secret_key
   SUPABASE_S3_BUCKET=images
   ```

5. **Deploy**
   - Click "Create Web Service"
   - Wait for the build to complete (usually 5-10 minutes)

#### Option B: Using render.yaml (Blueprint)

1. **Push your code** with the `render.yaml` file to your repository
2. **In Render Dashboard:**
   - Click "New +" → "Blueprint"
   - Connect your repository
   - Render will automatically detect the `render.yaml` configuration
3. **Set Environment Variables** in the dashboard (same as above)
4. **Deploy**

### Step 3: Configure Your Database

You have two options for the database:

#### Option A: Use Your Existing Supabase Database
- Use the same database URL from your `application.properties`
- Make sure your Supabase database allows external connections

#### Option B: Create a New PostgreSQL Database on Render
1. In Render dashboard, click "New +" → "PostgreSQL"
2. Create a new database
3. Use the provided connection details in your environment variables
4. You'll need to run your schema.sql manually or modify the application to create tables

### Step 4: Verify Deployment

1. **Check the logs** in Render dashboard for any errors
2. **Test your API endpoints** using the provided URL
3. **Access Swagger UI** at: `https://your-app-name.onrender.com/swagger-ui.html`

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Spring profile to use | `production` |
| `SERVER_PORT` | Port for the application | `8080` |
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://host:port/db` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `your_password` |
| `SUPABASE_S3_ENDPOINT` | Supabase S3 endpoint | `https://your-project.storage.supabase.co/storage/v1/s3` |
| `SUPABASE_S3_REGION` | S3 region | `ap-south-1` |
| `SUPABASE_S3_ACCESS_KEY` | S3 access key | `your_access_key` |
| `SUPABASE_S3_SECRET_KEY` | S3 secret key | `your_secret_key` |
| `SUPABASE_S3_BUCKET` | S3 bucket name | `images` |

## Troubleshooting

### Common Issues:

1. **Build Fails**
   - Check that Java 17 is being used
   - Verify all dependencies in `pom.xml`
   - Check the build logs in Render dashboard

2. **Application Won't Start**
   - Verify all environment variables are set
   - Check the application logs
   - Ensure database is accessible

3. **Database Connection Issues**
   - Verify your database credentials
   - Check if your database allows external connections
   - Test the connection URL locally

4. **File Upload Issues**
   - Verify Supabase S3 credentials
   - Check bucket permissions
   - Ensure CORS is configured properly

### Getting Help:
- Check Render logs in the dashboard
- Review application logs for specific error messages
- Verify all environment variables are correctly set

## Security Best Practices

1. **Never commit sensitive data** to your repository
2. **Use environment variables** for all sensitive configuration
3. **Regularly rotate** your database and S3 credentials
4. **Monitor your application** logs for security issues
5. **Use HTTPS** for all external communications

## Cost Optimization

- Render's free tier includes 750 hours per month
- Your app will sleep after 15 minutes of inactivity
- Consider upgrading to a paid plan for production use
