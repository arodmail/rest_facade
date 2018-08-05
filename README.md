# REST Facade

A RESTful layer for mid-tier applications

## Setup

#### 1. Clone repo

```bash
git clone https://github.com/arodmail/rest_facade/
```

#### 2. Run gradle wrapper task

```bash
gradle wrapper
```

#### 3. Run gradle build

```bash
gradlew build
```

#### 4. Start Server
```bash
./gradlew run
```
```bash
...
YYYY-MM-DDT00:00:00,000 DEBUG org.rest.facade.Main main Tomcat listening on Port 4000
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
  "lastModified" : "YYYY-MM-DDT00:00:00.000-0700"
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
  "lastModified" : "YYYY-MM-DDT00:00:00.000-0700"
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
  "lastModified" : "YYYY-MM-DDT00:00:00.000-0700"
}
```

#### Delete Resource
```bash
curl -v -X DELETE http://localhost:4000/rest/Resource/123
```

```bash  
* Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 4000 (#0)
> DELETE /rest/resource/123 HTTP/1.1
> Host: localhost:4000
> User-Agent: curl/7.54.0
> Accept: */*
> 
< HTTP/1.1 200 OK
```