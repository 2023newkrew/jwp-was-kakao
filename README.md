# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 학습 목표
- 웹 서버 구현을 통해 HTTP 이해도 높이기
- 서블릿 컨테이너의 역할과 내부 구조를 이해한다
- HTTP의 이해도를 높여 성능 개선할 부분을 찾고 적용해보는 경험을 한다
- 자바 Thread에 대해 학습하고, Servlet Container와 Thread의 역할을 이해한다

## HTTP 웹 서버 구현하기 1단계
**[1. GET /index.html 응답하기]**
- [x] 클라이언트는 http://localhost:8080/index.html 에 접근할 수 있어야한다
- [x] RequestHandlerTest가 모두 통과해야한다

**[2. CSS 지원하기]**
- [x] Stylesheet 파일을 응답할 수 있도록 한다

**[3. Query String 파싱]**
- [x] http://localhost:8080/user/form.html 에서 회원 가입을 할 수 있다
  - [x] GET http://localhost:8080/user/create?userId=id&password=pw&name=name&email=email 형식으로 요청이 발생한다
  - [x] 쿼리 스트링 형태로 회원 가입 정보가 넘어온다
  - [x] 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다
  - [x] User를 DataBase에 저장한다

**[4. POST 방식으로 회원가입 리팩터링]**
- [x] 회원 가입을 GET 방식에서 POST 방식으로 리팩터링
  - [x] Request Body에 담긴 내용을 추출하여 User 클래스를 생성한다

**[5. Redirect]**
- [x] 회원 가입 완료 후 index.html로 이동한다
  - [x] 상태 코드를 302를 사용한다

## HTTP 웹 서버 구현하기 2단계
**[1. 로그인 기능 구현하기]**
- [x] ```http://localhost:8080/user/login``` 에 접속하면 로그인 페이지로 이동한다.
- [x] 로그인에 성공하면 index.html로 이동한다.
- [x] 로그인에 실패하면 `/user/login_failed.html`로 이동한다.
- [x] 로그인에 성공하면 `JSESSIONID` 를 생성해 `Set-Cookie`를 통해 클라이언트에게 전달한다.

**[2. 템플릿 엔진 활용하기]**
- [x] 클라이언트가 로그인한 상태면 ```http://localhost:8080/user/list``` 로 접근했을 때 사용자 목록이 출력된다.
- [x] 클라이언트가 로그인하지 않았다면 `login.html`로 이동한다.

**[3. Session 구현하기]**
- [x] `JSESSIONID` 값을 통해 로그인 여부를 체크할 수 있다.
- [x] 로그인에 성공하면 Session 객체에 User 객체를 추가한다.
- [x] 로그인된 상태에서 `/user/login`에 접근하면 `index.html`로 리다이렉트 처리된다.