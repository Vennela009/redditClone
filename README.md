# Reddit Clone Application

## Project Overview
This is a **Reddit Clone** built using **Spring Boot**, **Spring Data JPA**, **Thymeleaf**, **WebSockets**, and **Amazon S3** for file storage. The application allows users to create posts, comments, and interact with various subreddits, with features like voting and user authentication.

## Part 1: Core Features

### Use Cases for Non-Logged-In Users:
- **View Posts and Subreddits**: 
  - View a list of posts, including titles, authors, content, and associated subreddits.
  - Browse various subreddits and see their posts.
  
- **Commenting**:
  - Read comments on posts.
  
- **User Authentication**:
  - Register and log in as a user.

### Use Cases for Logged-In Users:
- **Post Creation**:
  - Create a post in a subreddit.
  
- **Commenting**:
  - Add comments to posts.
  
- **Vote**:
  - Upvote or downvote posts and comments.

### Database Schema:
- **User**:
  - id, username, password, email, created_at, updated_at

- **Post**:
  - id, title, content, author_id, subreddit_id, created_at, updated_at

- **Comment**:
  - id, content, post_id, author_id, created_at, updated_at
  
- **Subreddit**:
  - id, name, description, created_at, updated_at
  
- **Vote**:
  - id, user_id, post_id, value (upvote/downvote)

### Steps to Build:
1. **Design & Implement Database Schema**: Create tables for users, posts, comments, subreddits, and votes.
2. **Create HTML & CSS**: Develop the layout for pages such as homepage, post creation page, and comment section.
3. **Integrate Thymeleaf**: Use Thymeleaf to render data dynamically in the HTML templates.
4. **Connect Frontend with Backend**: Use Spring Boot and Spring Data JPA for database operations and Spring Security for authentication.

### Future Enhancements:
- **Real-Time Features**:
  - Implement a real-time chat feature using WebSockets for messaging between users.
  
- **File Storage**:
  - Implement file upload functionality using Amazon S3 for storing images and avatars.

---

## Part 2: Authentication & Authorization

### Technologies Used:
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Thymeleaf**

### Use Cases for Non-Logged-In & Logged-In Users:
- **Non-Logged-In Users**:
  - View posts and comments.
  
- **Logged-In Users**:
  - Create, update, and delete posts.
  - Comment on posts and manage their comments.

- **Admin Privileges**:
  - Admin users can manage users and subreddits.

### Steps to Implement:
1. **Incorporate Spring Security**: Set up authentication and authorization with Spring Security.
2. **Enable Role-Based Access**: Define roles such as Admin and User, allowing admins to manage users and subreddits.
3. **Implement Post and Comment Management**: Enable logged-in users to create, update, and delete their posts and comments.

---

## RedditClone Directory Structure

```plaintext
redditClone
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
├── .mvn
│   └── wrapper
│       └── maven-wrapper.properties
└── src
    ├── main
    │   ├── java
    │   │   └── io
    │   │       └── mountblue
    │   │           └── reddit_project
    │   │               ├── RedditProjectApplication.java
    │   │               ├── aws
    │   │               │   └── AWSConfig.java
    │   │               ├── config
    │   │               │   ├── WebSocketConfig.java
    │   │               │   └── WebSocketEventListener.java
    │   │               ├── controller
    │   │               │   ├── ChatController.java
    │   │               │   ├── CommentController.java
    │   │               │   ├── PostController.java
    │   │               │   ├── SubRedditController.java
    │   │               │   ├── UserController.java
    │   │               │   └── ViewController.java
    │   │               ├── exception
    │   │               │   └── GlobalExceptionHandler.java
    │   │               ├── model
    │   │               │   ├── ChatMessage.java
    │   │               │   ├── Comment.java
    │   │               │   ├── Flair.java
    │   │               │   ├── Post.java
    │   │               │   ├── SubReddit.java
    │   │               │   ├── Subscription.java
    │   │               │   ├── User.java
    │   │               │   ├── Vote.java
    │   │               │   └── VoteComment.java
    │   │               ├── repository
    │   │               │   ├── CommentRepository.java
    │   │               │   ├── FlairRepository.java
    │   │               │   ├── PostRepository.java
    │   │               │   ├── SubRedditRepository.java
    │   │               │   ├── SubscriptionRepository.java
    │   │               │   ├── UserRepository.java
    │   │               │   └── VoteCommentRepository.java
    │   │               ├── security
    │   │               │   ├── RedditCloneUserDetailService.java
    │   │               │   └── SecurityConfig.java
    │   │               └── service
    │   │                   ├── CommentService.java
    │   │                   ├── FlairService.java
    │   │                   ├── MarkDownService.java
    │   │                   ├── PostService.java
    │   │                   ├── S3Service.java
    │   │                   ├── SubRedditService.java
    │   │                   ├── SubscriptionService.java
    │   │                   ├── UserService.java
    │   │                   ├── VoteCommentService.java
    │   │                   └── VoteService.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       │   ├── images
    │       │   │   └── avatar
    │       │   │       ├── 1.svg
    │       │   │       ├── 10.svg
    │       │   │       ├── 11.svg
    │       │   │       ├── 12.svg
    │       │   │       ├── 13.svg
    │       │   │       ├── 14.svg
    │       │   │       ├── 15.svg
    │       │   │       ├── 2.svg
    │       │   │       ├── 3.svg
    │       │   │       ├── 4.svg
    │       │   │       ├── 5.svg
    │       │   │       ├── 6.svg
    │       │   │       ├── 7.svg
    │       │   │       ├── 8.svg
    │       │   │       ├── 9.svg
    │       │   │       └── reddit.png
    │       │   └── logo
    │       │       └── reddit.png
    │       └── templates
    │           ├── about-reddit-html.html
    │           ├── about-reddit.html
    │           ├── chat.html
    │           ├── comment-updater.html
    │           ├── content-policy.html
    │           ├── create-post-next.html
    │           ├── create-post.html
    │           ├── edit-user.html
    │           ├── error.html
    │           ├── full-post-view.html
    │           ├── homepage.html
    │           ├── login.html
    │           ├── post-updater.html
    │           ├── privacy-policy.html
    │           ├── register.html
    │           ├── user-agreement.html
    │           └── user-view.html
    └── test
        └── java
            └── io
                └── mountblue
                    └── reddit_project
                        └── RedditProjectApplicationTests.java

```
# Technologies Used

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **Amazon S3** (for file storage)
- **WebSockets** (for real-time chat)
- **Maven**

---

## Installation

### Prerequisites
- **Java 17** or higher
- **PostgreSQL** (configured and running)
- **Amazon S3** (configured for file storage)
- **Maven**

### Clone the Repository

```bash
git clone https://github.com/yourusername/redditClone.git
cd redditClone
