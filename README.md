# Bookstore
Bookstore API

## Functionalities
### Add a new book
URI: **POST** - `/v1/bookstore/books`
#### Headers Sample

| Header Key    | Header Value               |
|---------------|----------------------------|
| Content-Type  | application/json           |
| Authorization | Basic dXNlcjpwYXNzd29yZA== |

#### Request Samples
Request contains authors' ids
```json
{
  "isbn": "9780596520683",
  "title": "Math",
  "year": 1999,
  "authors": [
    {
      "id": 1
    }
  ],
  "price": 0.1,
  "genre": "non fiction"
}
```
Request without authors' ids
```json
{
  "isbn": "9780596520683",
  "title": "Math",
  "year": 1999,
  "price": 0.1,
  "genre": "non fiction"
}
```
#### Response Samples
Success - 201 Created
```json
{
    "id": 8,
    "isbn": "9780596520683",
    "title": "Math",
    "year": 1999,
    "authors": [
        {
            "id": 1,
            "name": "John Doe",
            "birthday": "1990-01-01"
        }
    ],
    "price": 0.1,
    "genre": "non fiction"
}
```
Existing ISBN - 400 Bad Request
```json
{
    "timestamp": "2023-04-27T06:55:37.073+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "ISBN value already exists",
    "path": "/v1/bookstore/books"
}
```

### Update book
URI: **PUT** - `/v1/bookstore/books/{bookId}`
#### Request Samples
```json
{
  "isbn": "9780596520683",
  "title": "Math",
  "year": 1999,
  "authors": [
    {
      "id": 1
    }
  ],
  "price": 0.1,
  "genre": "non fiction"
}
```

#### Response Samples
Success - 200 OK
```json
{
    "id": 8,
    "isbn": "9780596520683",
    "title": "Math",
    "year": 1999,
    "authors": [
        {
            "id": 1,
            "name": "John Doe",
            "birthday": "1990-01-01"
        }
    ],
    "price": 0.1,
    "genre": "non fiction"
}
```

### Add an author to book
URI: **PUT** - `/v1/bookstore/books/{bookId}/authors/{authorId}` <br>
No Request Body

#### Response Samples
Success - 200 OK
```json
{
    "id": 8,
    "isbn": "9780596520683",
    "title": "Math",
    "year": 1999,
    "authors": [
        {
            "id": 1,
            "name": "John Doe",
            "birthday": "1990-01-01"
        }
    ],
    "price": 0.1,
    "genre": "non fiction"
}
```

### Get book by title
URI: **GET** - `/v1/bookstore/books?title={title}` <br>
No Request Body
#### Response samples
Success - 200 OK
```json
[
    {
        "id": 5,
        "isbn": "9780596520689",
        "title": "Random",
        "year": 1990,
        "authors": [
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            },
            {
                "id": 3,
                "name": "Maxim Well",
                "birthday": "1990-01-01"
            }
        ],
        "price": 20.1,
        "genre": "non-fiction"
    },
    {
        "id": 6,
        "isbn": "9780596520688",
        "title": "Random",
        "year": 2019,
        "authors": [
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            },
            {
                "id": 2,
                "name": "George Roger",
                "birthday": "1990-01-01"
            }
        ],
        "price": 10.0,
        "genre": "non fiction"
    }
]
```

### Get book by author name(s)
URI: **GET** - `/v1/bookstore/books?authorName={authorName}` or <br>
URI: **GET** - `/v1/bookstore/books?authorName={authorName1}&authorName={authorName2}` <br>
No Request Body
#### Response Sample
Find books by one author name - 200 OK
```json
[
    {
        "id": 2,
        "isbn": "ISBN-13: 978-0-596-52068-7",
        "title": "Funny",
        "year": 1980,
        "authors": [
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            }
        ],
        "price": 15.3,
        "genre": "fiction"
    },
    {
        "id": 3,
        "isbn": "978 0 596 52068 7",
        "title": "Bus",
        "year": 2020,
        "authors": [
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            }
        ],
        "price": 8.9,
        "genre": "fiction"
    },
    {
        "id": 8,
        "isbn": "9780596520683",
        "title": "Math",
        "year": 1999,
        "authors": [
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            }
        ],
        "price": 0.1,
        "genre": "non fiction"
    }
]
```
Find books by multiple author names - 200 OK
```json
[
    {
        "id": 5,
        "isbn": "9780596520689",
        "title": "Random",
        "year": 1990,
        "authors": [
            {
                "id": 3,
                "name": "Maxim Well",
                "birthday": "1990-01-01"
            },
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            }
        ],
        "price": 20.1,
        "genre": "non-fiction"
    },
    {
        "id": 7,
        "isbn": "9780596520686",
        "title": "Science",
        "year": 2005,
        "authors": [
            {
                "id": 3,
                "name": "Maxim Well",
                "birthday": "1990-01-01"
            },
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            }
        ],
        "price": 1.1,
        "genre": "non-fiction"
    }
]
```

### Get book by author name(s) and title
URI: GET - /v1/bookstore/books?title={title}authorName={authorName} or <br>
URI: GET - /v1/bookstore/books?authorName={authorName1}&authorName={authorName2} <br>
No Request Body

#### Response Samples
Find book by title and author name - 200 OK
```json
[
    {
        "id": 2,
        "isbn": "ISBN-13: 978-0-596-52068-7",
        "title": "Funny",
        "year": 1980,
        "authors": [
            {
                "id": 1,
                "name": "John Doe",
                "birthday": "1990-01-01"
            }
        ],
        "price": 15.3,
        "genre": "fiction"
    }
]
```

### Delete book
URI: **DELETE** - `/v1/bookstore/books/{bookId}` <br>
No Request Body
#### Headers Sample
| Header Key    | Header Value               |
|---------------|----------------------------|
| Content-Type  | application/json           |
| Authorization | Basic YWRtaW46cGFzc3dvcmQ= |

#### Response Sample
Success - 204 No Content <br>
No Response Body

### Security
API security was implemented using Basic Authentication. <br>
There are 2 hardcoded users created

| Username | Password |
|----------|----------|
| user     | password |
| admin    | password |

Accessing API without providing Auth in the header will return
```json
{
    "timestamp": "2023-04-27T07:19:32.543+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Unauthorized",
    "path": "/v1/bookstore/books/7/authors/1"
}
```

Request is not authorized for given user provided in Auth header will return 
```json
{
    "timestamp": "2023-04-27T07:21:27.636+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Forbidden",
    "path": "/v1/bookstore/books/8"
}
```

### Database
Book info is stored in SQLite, filename `bookstore.db`.