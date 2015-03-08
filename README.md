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

## Login

**Request**
* Method: POST
* URL: /login
* Data: username, password

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
* Data: username, password

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
