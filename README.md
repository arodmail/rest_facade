# REST Facade

A RESTful layer for mid-tier applications

## Setup

#### 1. Clone repo

```bash
git clone https://github.com/arodmail/rest_facade/
```

#### 2. Run gradle wrapper task

```bash
cd rest_facade
gradle wrapper
```

#### 3. Run gradle build

```bash
./gradlew build
```

#### 4. Start Server
```bash
./gradlew run
```

```bash
YYYY-MM-DDTHH:MM:SS,000 DEBUG org.rest.facade.Main main Tomcat listening on Port 4000
```

## Sample Requests

#### Get Root
```bash
curl -X GET http://localhost:4000/rest/
```

```json
{
     "resources" : 
     [ 
         {
           "name" : "Resource",
           "link" : "http://localhost:4000/rest/Resource"
         } 
     ]
}
```

#### Get Resource
```bash
curl -X GET http://localhost:4000/rest/Resource/123
```

```json
{
  "id" : "123",
  "name" : "name-123",
  "lastModified" : "YYYY-MM-DDTHH:MM:SS.000-0700"
}
```

#### Post Resource
```bash
curl -X POST -d '{"name":"name-234"}' http://localhost:4000/rest/Resource/
```

```json
{
  "id" : "1",
  "name" : "name-234",
  "lastModified" : "YYYY-MM-DDTHH:MM:SS.000-0700"
}
```

#### Put Resource
```bash
curl -X PUT -d '{"id":"1","name":"rename-123"}' http://localhost:4000/rest/Resource/
```

```json
{
  "id" : "1",
  "name" : "rename-123",
  "lastModified" : "YYYY-MM-DDTHH:MM:SS.000-0700"
}
```

#### Delete Resource
```bash
curl -v -X DELETE http://localhost:4000/rest/Resource/123
```

```bash
< HTTP/1.1 200 OK
```

#### Asynchronous Requests

##### Step 1: Send Request

```bash
curl -v -X POST -d '{\"name\":\"async-name\"}' http://localhost:4000/rest/AsyncResource
```

```bash
< HTTP/1.1 202 Accepted
< Location: /queue/AsyncResource/123
```

```json
{
  "status" : "PROCESSING",
  "progress" : "0"
}
```

##### Step 2: Check Status

```bash
curl -v -X GET http://localhost:4000/rest/queue/AsyncResource/123
```

```bash
< HTTP/1.1 200 OK
< Location: /rest/async/AsyncResource/123
```

```json
{
  "status" : "COMPLETED",
  "progress" : "100"
}
```

##### Step 3: Get Result

```bash
curl -v -X GET http://localhost:4000/rest/async/AsyncResource/123
```

```json
{
  "items" : 
  [ 
       {
         "id" : "0",
         "name" : "asyncname-0",
         "lastModified" : "YYYY-MM-DDTHH:MM:SS.000-0700"
       }
  ]
}
```
