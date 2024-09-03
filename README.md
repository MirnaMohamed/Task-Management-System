# Task management service

A Backend application for managing tasks between users and admin

## Project Setup 
- Make a database inside PostgreSQL named `task_management_db` and set the admin to
`task-admin` with password `task-admin` 
- set the Eureka Server to `http://localhost:8761/eureka/` or modify them inside the `application.properties` file.
- If you want to use the service APIs, refer to it using the `spring.application.name` inside `application.properties`.

## Features:
Almost all features require logging   
Not authorized users have access to login or registration controller    
The application checks every 30 minutes if there is a task that is not done yet and its due date in two hours.

**Admin (manager) can:**
-	Create task and assign task to any user
-	View list of all users
-	View list of all tasks with editing or deleting task
-	View tasks by a specific user

**Common user can:**
-	Create task only for himself
-	View list of his/her own tasks
-	Can update or delete his tasks

**Every authorized user can:**
-	View his own profile

## Data Loader
The project loads initial data for users and tasks. You can view the user's email 
and password in the DataLoader class and delete it if you want to rerun the 
application to avoid errors.


## Built With
* Spring Boot
* Spring Security
* PostgreSQL database
* Maven
* Spring Cloud
* Spring Validation
* Flyway Migration
* Java Mail Sender

