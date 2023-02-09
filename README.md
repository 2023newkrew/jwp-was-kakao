# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단계 - HTTP 웹 서버 구현하기

- GET /index.html 응답하기
  - http://localhost:8080/index.html에 접근할 수 있도록 구현한다.
  - RequestHandlerTest 테스트가 모두 통과하도록 구현한다.
- CSS 지원하기
- Query String 파싱
- POST 방식으로 회원가입
- Redirect
- **로그인 기능 구현**
  - **“로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.**
  - **로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.**
  - **회원가입한 사용자로 로그인할 수 있어야 한다.**
  - **자바 진영에서 세션 아이디를 전달하는 이름으로 JSESSIONID를 사용한다.**
  - **서버에서 HTTP 응답을 전달할 때 응답 헤더에 Set-Cookie를 추가하고 JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46 형태로 값을 전달하면 클라이언트 요청 헤더의 Cookie 필드에 값이 추가된다.**
- **템플릿 엔진 활용하기**
  - **접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 로 접근했을 때 사용자 목록을 출력한다. 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.**
  - **동적으로 html을 생성하기 위해 handlebars.java template engine을 활용한다.**
- **Session 구현하기**
  - **쿠키에서 전달 받은 JSESSIONID의 값으로 로그인 여부를 체크할 수 있어야 한다.**
  - **로그인에 성공하면 Session 객체의 값으로 User 객체를 저장해보자.**
  - **로그인된 상태에서 /user/login 페이지에 HTTP GET method로 접근하면 이미 로그인한 상태니 index.html 페이지로 리다이렉트 처리한다.**
