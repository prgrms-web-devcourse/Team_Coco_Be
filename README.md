# 트립플랜(triplan)

<div align='center'>
  <img src="https://user-images.githubusercontent.com/37530109/146879939-cbd67eb2-770a-4f01-902f-d1ab54fa15e2.png">
  
  📆 2021.11.26 ~ 2021.12.21
</div>

<p align="cetner">
  <img src="https://img.shields.io/badge/Java-11-007396?style=flat-square&logo=Java&logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/Spring Boot-2.6.1-6DB33F?style=flat-square&logo=Spring&logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/-JPA-gray?logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/MySQL-8.0.27-4479A1?style=flat-square&logo=MySQL&logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/Maven-4429A1?style=flat-square&logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/Junit-25A162?style=flat-&logo=JUnit5&logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=amazon%20AWS&logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/%20-logback-informatonal" />
  <img src="https://img.shields.io/badge/%20-QueryDSL-blue" />
  <br>
  <img src="https://img.shields.io/badge/%20-Spring%20Security-brightgreen" />
  <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira%20software&logoColor=white&style=flat" />
  <img src="https://img.shields.io/badge/slack-232F3E?style=flat-square&logo=slack&logoColor=white&style=flat" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white&style=flat" />
  <img src="https://img.shields.io/badge/ERDCloud-4429A7?style=flat-square&logoColor=white&style=flat"/>
</p>
          
## 📝 프로젝트 소개

### 👨‍👩‍👧‍👦 여행 구성원과 공유할 수 있는 일정 관리 서비스

> - 멤버를 초대하여 여행 일정을 관리하고 공유할 수 있습니다.
> - 멤버들과 함께 체크리스트, 메모, 투표 등을 공유하고 확인할 수 있습니다.

### 📝 자신의 여행을 자랑하고 공유할 수 있는 서비스

> - 자신이 계획한 여행 일정을 게시글로 작성하고 이를 공유할 수 있습니다.
> - 다른 사람이 작성한 게시글을 보고 자신의 일정을 계획하는 데 참고할 수 있습니다.

## 팀원

<table>
    <tr align="center">
        <td><B>이태현<B></td>
        <td><B>김휘년<B></td>
        <td><B>엄진환<B></td>
    </tr>
    <tr align="center">
        <td>
            <img width="100" src="https://github.com/neilsonT.png?">
            <br>
            <a href="https://github.com/neilsonT"><I>neilsonT</I></a>
        </td>
        <td>
            <img src="https://github.com/HwiNyeonKim.png?size=100">
            <br>
            <a href="https://github.com/HwiNyeonKim"><I>HwiNyeonKim</I></a>
        </td>
        <td>
            <img src="https://github.com/UJHa.png?size=100">
            <br>
            <a href="https://github.com/UJHa"><I>UJha</I></a>
        </td>
    </tr>
</table>

## 설계

### ERD

<img width="2381" alt="CleanShot 2021-12-21 at 20 49 11@2x" src="https://user-images.githubusercontent.com/41899242/146936348-e51f0cf2-e830-45e8-aa62-47163f494b9e.png">

### API

* [Swagger 링크](http://13.209.224.77:8080/swagger-ui/index.html#/)
* [PDF로 다운로드](https://github.com/prgrms-web-devcourse/Team_Coco_Be/files/7755629/Swagger.UI.pdf)

## 개발 환경설정

### application.yml
```yml
spring:
  messages:
    basename: i18n/messages
    encoding: UTF-8
    cache-duration: PT1H
  profiles:
    active: local
  datasource:
    url: # Your MySQL url
    username: # Your MySQL user name
    password: # Your MySQL user passowrd
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: none
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    defer-datasource-initialization: true
    database: mysql
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
  sql:
    init:
      schema-locations: classpath:sql/schema_new.sql
      data-locations: classpath:sql/data_new.sql
      encoding: UTF-8
      mode: always
  output:
    ansi.enabled: always
server:
  port: 8080
  servlet:
    encoding:
      charset: UTF-8
      enabled: true
      force: true
jwt:
  header: token
  issuer: prgrms
  client-secret: # Secret
  expiry-seconds: 1800
```