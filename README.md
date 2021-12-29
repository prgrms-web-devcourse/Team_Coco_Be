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

## 💻 주요 기능

### 🔑 회원 관련 기능

|                                                            로그인                                                             |                                                           회원가입                                                            |                                                           친구 추가                                                           |                                                  좋아요, 작성한 게시글 보기                                                   |
| :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: |
| <img width='500' src='https://user-images.githubusercontent.com/37530109/146910797-a8357d47-4baa-4a62-abcc-296fd5bee5ab.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146925360-0660ee8e-3eed-45f8-b19f-6efd2281ed6b.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146913007-8d9eb946-1db6-4a51-8331-3699b436eac7.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146924410-1a069638-c532-44de-8a59-93a8cfbb30f5.gif'> |

### 🗓 일정 관련 기능

|                                                           일정 조회                                                           |                                                           일정 생성                                                           |                                                           일정 삭제                                                           |                                                          체크리스트                                                           |
| :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: |
| <img width='500' src='https://user-images.githubusercontent.com/37530109/146925476-352dccf0-902c-41c1-9151-cb49553b7129.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146927179-2037fd6b-a036-4a9a-ad0b-3e160a143970.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146929125-432857cd-4d8b-469d-82f0-f53cd56f2040.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146926976-e21759ac-307c-4a23-93d7-87c7877fd6c2.gif'> |

|                                                           메모 생성                                                           |                                                           메모 수정                                                           |                                                           메모 삭제                                                           |
| :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: |
| <img width='500' src='https://user-images.githubusercontent.com/37530109/146929283-cde2d77d-8ff9-4e28-8e05-235de667be2b.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146929374-7c206dbb-ab72-4e75-b079-67f0fc9cabed.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146929463-762362d4-a7bc-422d-8e87-a7ad7eea20f5.gif'> |

|                                                           투표 생성                                                           |                                                           투표 행사                                                           |                                                           투표 삭제                                                           |
| :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: |
| <img width='500' src='https://user-images.githubusercontent.com/37530109/146929605-2e3e2700-b881-4d9c-8cf8-b6f2e39a91ab.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146929667-e640f4f4-ebdb-4da2-8bdb-2fba2b639ef0.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146929733-632f2ccf-51da-4787-9d37-7049ae46099d.gif'> |

### 📄 게시글 관련 기능

|                                                          게시글 조회                                                          |                                                          게시글 생성                                                          |                                                          게시글 수정                                                          |                                                          게시글 삭제                                                          |
| :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: |
| <img width='500' src='https://user-images.githubusercontent.com/37530109/146922895-c27dc5a3-e64f-4e17-81bd-8d669286f9f2.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146924092-37710045-c78b-4262-878c-4ac2c21c8366.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146924204-ad9322d9-6b56-40c2-8b13-3139259f786c.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146924324-79b1d3ae-534d-4722-8f56-5145937a5698.gif'> |

|                                                         게시글 좋아요                                                         |                                                           댓글 작성                                                           |                                                           댓글 삭제                                                           |
| :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------: |
| <img width='500' src='https://user-images.githubusercontent.com/37530109/146922773-2d62480f-41fa-4914-970e-a6b6233c58f7.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146922459-abbbe5ca-a8c2-4e25-9eef-a85e6c269e7d.gif'> | <img width='500' src='https://user-images.githubusercontent.com/37530109/146922588-9bbc2010-53bf-4193-9876-89f54c0754b8.gif'> |          
          
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
