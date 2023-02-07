# 웹 애플리케이션 서버

## Step 1
### GET /index.html 응답하기
* [x] http://localhost:8080/index.html에 접근할 수 있도록 구현한다.
* [x] RequestHandlerTest 테스트가 모두 통과하도록 구현한다.

### CSS 지원하기
* [x] Stylesheet 파일을 지원하도록 구현하도록 한다.

### String Parsing을 이용하여 POST 방식으로 회원가입
* [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 `post`로 방식으로 회원가입 기능이 정상적으로 동작하도록 구현한다.
* [x] 사용자가 입력한 값을 파싱해 회원가입 정보를 `model.User` 클래스에 저장하고 `DataBase.addUser()` 메서드를 활용해 RAM 메모리에 저장한다.

### Redirect
* [x] “회원가입”을 완료 후 `/user/create`에서 redirect를 이용하여 `index.html`로 이동해야 한다.

## Step 2
### 로그인 기능 구현
* [ ] “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다.
* [ ] 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
* [ ] 회원가입한 사용자로 로그인할 수 있어야 한다.
* [ ] 자바 진영에서 세션 아이디를 전달하는 이름으로 JSESSIONID를 사용한다.
* [ ] 서버에서 HTTP 응답을 전달할 때 응답 헤더에 Set-Cookie를 추가하고 JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46 형태로 값을 전달하면 클라이언트 요청 헤더의 Cookie 필드에 값이 추가된다.

### 템플릿 엔진 활용하기

### 세션 구현하기
* [ ] 쿠키에서 전달 받은 JSESSIONID의 값으로 로그인 여부를 체크할 수 있어야 한다.
* [ ] 로그인에 성공하면 Session 객체의 값으로 User 객체를 저장해보자.
* [ ] 로그인된 상태에서 /user/login 페이지에 HTTP GET method로 접근하면 이미 로그인한 상태니 index.html 페이지로 리다이렉트 처리한다.
