Технопарк@Mail.ru
============
Курс: Углубленное программирование на Java

Учебный период: весенний семестр 2015г.

##Студенты
* Аксенов Михаил, AverageS, blabos@ya.ru
* Крутяков Михаил, mikrut, mihanik01@mail.ru
* Фатхи Дмитрий, Fatkhi, ?

Подробное о проекте можно прочесть на сайте [Технопарка] [1]
[1]: http://tp.mail.ru/ "Технопарк@Mail.ru"

# API

API can be easily customised just by changing XML config files in srv_tmpl directory.
Exempli gratia, basic responses can be edited in response_resource.xml file.

## Login

**Request**
* Method: POST
* URL: /login
* Data: name, password

**Response**
* Format: JSON
* Data:
```
{
  "status": "OK"/"Error",
  "message": ${some_message}
}
```

## Logout

**Request**
* Method: POST
* URL: /logout
* Data: -

**Response**
* Format: -
* Data: -

## Registration

**Request**
* Method: POST
* URL: /signin
* Data: name, password

**Response**
* Format: JSON
* Data:
```
{
  "status": "OK"/"Error",
  "message": ${some_message}
}
```

## User info

**Request**
* Method: GET
* URL: /getinfo

**Response**
* Format: JSON
* Data:
```
{
  "loggedIn": true/false,
  "username": ${username}
}
```

## Admin info
_for admin only_

**Request**
* Method: GET
* URL: /getadmin

**Response**
* Format: JSON / Forbidden
* Data:
```
{
  "status":"OK",
  "users":[
    {
      "username":${username},
      "userid":${userid}
    }
  ]
}
```

## Server stop
_for admin only_

**Request**
* Method: POST
* URL: /getadmin/stop

**Response**
* None

## Game

* Type: JSON
* Method: WebSocket
* URL: /chat

### Connection established event

Nota bene: user should be authorized, user can play with himself if he uses different sessions

**Response**
```
{
  "Status":"Connected",
  "Message":"Connection successfull. Waiting for another player."
}
```
### Game start event

**Response**
```
{
  "is_first":true/false,
  "message":"Let the game start!",
  "status":"Game start"
}
```

### Capture

**Request**
```
{
  "row":"1",
  "col":"2"
}
```
**Response**
```
{
  "col":"2",
  "row":"1",
  "message":"Data accepted."/"Invalid data.",
  "board":[
    [0,0,0,0,0],
    [0,0,1,0,0],
    [0,0,0,0,0],
    [0,0,0,0,0],
    [0,0,0,0,0]
  ],
  "status":"OK"/"Error",
  "who_moves":1 // Index of player having next step right: 0 - first, 1 - second
}
```

