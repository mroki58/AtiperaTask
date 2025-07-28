# GitHub Repositories Explorer API

This Spring Boot application exposes an HTTP endpoint that allows users to fetch public GitHub repositories (excluding forks) for a given username. For each repository, the API returns its name, the owner's login, and a list of branches with the last commit SHA.

## Features

- Lists all **non-fork** repositories of a specified GitHub user
- Provides:
  - Repository name
  - Owner login
  - Branch name and last commit SHA for each branch
- Returns a structured `404` response for non-existent users
- Uses [GitHub REST API v3](https://developer.github.com/v3/)
- Built using Spring Boot and Java 17+

---

## Technologies

- Java 21
- Spring Boot 3.5.4 
- Maven 
- IntelliJ IDEA

---

## Getting Started

### Prerequisites

- Java 21
- Maven (or Gradle)
- IntelliJ IDEA or another Java IDE

### Clone the Repository

```bash
git clone https://github.com/your-username/github-repo-explorer-spring.git
cd github-repo-explorer-spring
```

### Build the Project

Using Maven:

```bash
./mvnw clean install
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at:

```
http://localhost:8080/?username={github_username}
```

---

## Example Usage

### ✅ GET `/?username=octocat`

#### Response:
```json
[
    {
    "repositoryName": "hello-worId",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "master",
        "sha": "7e068727fdb347b685b658d2981f8c85f7bf0585"
      }
    ]
  },
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "master",
        "sha": "7fd1a60b01f91b314f59955a4e4d4e80d8edf11d"
      },
      {
        "name": "octocat-patch-1",
        "sha": "b1b3f9723831141a31a1a7252a213e216ea76e56"
      },
      {
        "name": "test",
        "sha": "b3cbd5bbd7e81436d2eee04537ea2b4c0cad4cdf"
      }
    ]
  },
]
```

---

### ❌ GET `/?username=nonexistentuser`

#### Response:
```json
{
  "status": "Not Found",
  "message": "Not found user with username: {nonexistentuser}"
}
```

## Project Structure

```
src/
├── config/                 # configuration
├── utils/                  # helper classes and methods
├── dto/                    # Data Transfer Objects (used for responses)
├── exception/              # Custom exceptions and global exception handling

and other files - services, controllers which were few so I didn't add specific packages

```

---

## Error Handling

- `404 Not Found` — returned if the GitHub user does not exist, with custom JSON body
- Exceptions throwned by RestTemplate are also formatted to be returned with specified format
- Response format is consistent and clear for both successful and failed requests

---

## Notes

- The app uses GitHub’s public REST API v3 — unauthenticated usage is rate-limited (60 requests/hour/IP). To raise this limit, you may authenticate via GitHub token.
- Forked repositories are excluded in the response.
- JSON serialization follows camelCase formatting.

---

## License

This project is licensed under the MIT License.
