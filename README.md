# 웹 애플리케이션 서버

## 진행 방법

* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정

* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 기능 요구사항

1. GET /index.html 응답하기
   http://localhost:8080/index.html에 접근할 수 있도록 구현한다.
   RequestHandlerTest 테스트가 모두 통과하도록 구현한다.

* HTTP Request Header 예
  ```
  GET /index.html HTTP/1.1
  Host: localhost:8080
  Connection: keep-alive
  Accept: */*
  ```

2. CSS 지원하기
   인덱스 페이지에 접속하면, 현재 stylesheet 파일을 지원하지 못하고 있다. Stylesheet 파일을 지원하도록 구현하도록 한다.
   HTTP Request Header 예
   ```
   GET ./css/style.css HTTP/1.1
   Host: localhost:8080
   Accept: text/css,*/*;q=0.1
   Connection: keep-alive
   ```


3. Query String 파싱
   “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입할 수 있다.
   회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
   HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
   회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 RAM 메모리에 저장한다.

* HTTP Request Header 예
  ```
  GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1
  Host: localhost:8080
  Connection: keep-alive
  Accept: */*
  ```

4. POST 방식으로 회원가입
   http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.

* HTTP Request Header 예
  ```
  POST /user/create HTTP/1.1
  Host: localhost:8080
  Connection: keep-alive
  Content-Length: 59
  Content-Type: application/x-www-form-urlencoded
  Accept: */*
  ```

5. Redirect
   현재는 “회원가입”을 완료 후, URL이 /user/create 로 유지되는 상태로 읽어서 전달할 파일이 없다. redirect 방식처럼 회원가입을 완료한 후 index.html로 이동해야 한다.

## 추가미션 구현

1. 로그인 기능 구현

- “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
- 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
- 회원가입한 사용자로 로그인할 수 있어야 한다.
- Cookie 클래스를 추가하고 HTTP Request Header의 Cookie에 JSESSIONID가 없으면 HTTP Response Header에 Set-Cookie를 반환해주는 기능을 구현한다.

```
HTTP/1.1 200 OK
Set-Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46
Content-Length: 5571
Content-Type: text/html;charset=utf-8;
```

2. 템플릿 엔진 활용하기

- 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 로 접근했을 때 사용자 목록을 출력한다. 만약 로그인하지
  않은 상태라면 로그인 페이지(login.html)로 이동한다.
- 동적으로 html을 생성하기 위해 handlebars.java template engine을 활용한다.

3. Session 구현하기

- 쿠키에서 전달 받은 JSESSIONID의 값으로 로그인 여부를 체크할 수 있어야 한다.
- 로그인에 성공하면 Session 객체의 값으로 User 객체를 저장해보자.
- 로그인된 상태에서 /user/login 페이지에 HTTP GET method로 접근하면 이미 로그인한 상태니 index.html 페이지로 리다이렉트 처리한다.

* 세션 관리를 위한 자료구조
  Map을 활용할 수 있으며, Map<String, HttpSession>와 같은 구조가 될 것이다. 이 Map의 키(key)는 앞에서 UUID로 생성한 고유한 아이디이다.
