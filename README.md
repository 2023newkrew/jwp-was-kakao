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

## HTTP 웹 서버 구현하기
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

**[6. 로그인 기능 구현]**
- [x] `POST /user/login` 로그인 성공 시, `index.html`로, 로그인 실패 시, `/user/login_failed.html`로 이동한다
  - [x] 회원가입한 사용자로 로그인 할 수 있어야 한다
  - [x] 로그인한 사용자는 JSESSIONID로 세션을 발급받는다
  - [x] UUID를 활용해 고유한 아이디를 만든다

**[7. 템플릿 엔진 활용하기]**
- [ ] 사용자가 로그인 상태일 경우에 한해, `/user/list`의 사용자 목록을 출력한다
  - [ ] 로그인 되어있지 않으면 `index.html`로 이동한다
  - [ ] handlebars 템플릿 엔진을 활용하여 출력한다

**[8. Session 구현하기]**
- [x] JSESSIONID의 값으로 로그인 여부를 체크한다
- [x] 로그인에 성공했다면, Session 객체의 값으로 User 객체를 저장할 것
- [x] 로그인 상황에서 `/user/login`에 접근한 경우, `index.html`에 리다이렉트 할 것
- [x] 로그인 하지 않은 상황에 `/user/login`에 접근한 경우, `user/login.html`에 리다이렉트 할 것

## 리팩터링 중점 사안
- [x] FileType이 static 경로에 있는지를 FileType에 메시지 던져서 해결
- [x] Request를 RequestLine, Headers, RequestBody의 wrapper 클래스로 감싸기
- [x] 매직 넘버 상수화
- [x] HTTP Header를 enum으로 관리
- [ ] 단위테스트 작성
- [ ] 예외 처리 구현하기
