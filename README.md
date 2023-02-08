## 요구사항

## STEP 1

### 1. GET /index.html 응답하기

- [x] http://localhost:8080/index.html 에 접근할 수 있도록 구현한다.
- [x] RequestHandlerTest가 모두 통과하도록 구현한다.

### 2. CSS 지원하기

- [x] CSS 파일을 지원하도록 구현한다.

### 3. Query String 파싱

- [x] “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입할 수 있다.
- [x] 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
- [x] HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 user.User 클래스에 저장한다.
- [x] 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 RAM 메모리에 저장한다.

### 4. POST 방식으로 회원가입

- [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.

---

## STEP 2

### 1.로그인 기능 구현

- [x] “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
    - [x] 로그인 메뉴 선택시 `/user/login.html`에 접근할 수 있다.
    - [x] 로그인 기능 구현
- [x] 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
- [x] 회원가입한 사용자로 로그인할 수 있어야 한다.
- [x] 자바 진영에서 세션 아이디를 전달하는 이름으로 JSESSIONID를 사용한다.
- [x] 서버에서 HTTP 응답을 전달할 때 응답 헤더에 Set-Cookie를 추가하고 JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46 형태로 값을 전달하면 클라이언트 요청
  헤더의 Cookie 필드에 값이 추가된다.

### 2. 템플릿 엔진 활용하기

- [x] 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 로 접근했을 때 사용자 목록을 출력한다. 만약
  로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.
- [x] 동적으로 html을 생성하기 위해 handlebars.java template engine을 활용한다.

### 3. Session 구현하기

- [x] 쿠키에서 전달 받은 JSESSIONID의 값으로 로그인 여부를 체크할 수 있어야 한다.
- [x] 로그인에 성공하면 Session 객체의 값으로 User 객체를 저장해보자.
- [x] 로그인된 상태에서 /user/login 페이지에 HTTP GET method로 접근하면 이미 로그인한 상태니 index.html 페이지로 리다이렉트 처리한다.

## 추가 구현 사항

- [x] reflection을 이용한 annotation 기반 구현
- [x] 패키지간 의존성 분리
  - [x] infra package가 auth, app을 의존하지 않게 구현
  - [x] auth package가 app을 의존하지 않게 구현
- [x] Filter 구현