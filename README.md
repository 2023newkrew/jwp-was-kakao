# 웹 애플리케이션 서버

## Step1 기능 요구사항

- [x] GET /index.html 응답하기
- [x] CSS 지원하기
- [x] Query String 파싱
    - [x] GET 방식으로 회원가입
- [x] POST 방식으로 회원가입
- [x] Redirect
    - [x] 회원가입 완료 후 index.html로 리다이렉트

## Step2 기능 요구사항

- [x] 로그인 기능 구현
    - “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
    - 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
    - 회원가입한 사용자로 로그인할 수 있어야 한다.
    - 자바 진영에서 세션 아이디를 전달하는 이름으로 JSESSIONID를 사용한다.
    - 서버에서 HTTP 응답을 전달할 때 응답 헤더에 Set-Cookie를 추가하고 JSESSIONID=UUID 형태로 값을 전달하면 클라이언트 요청
      헤더의 Cookie 필드에 값이 추가된다.
- [x] 템플릿 엔진 활용하기
    - 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 로 접근했을 때 사용자 목록을 출력한다. 만약
      로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.
    - 동적으로 html을 생성하기 위해 handlebars.java template engine을 활용한다.
- [x] Session 구현하기
    - 쿠키에서 전달 받은 JSESSIONID의 값으로 로그인 여부를 체크할 수 있어야 한다.
    - 로그인에 성공하면 Session 객체의 값으로 User 객체를 저장한다.
    - 로그인된 상태에서 /user/login 페이지에 HTTP GET method로 접근하면 이미 로그인한 상태니 index.html 페이지로 리다이렉트 처리한다.
