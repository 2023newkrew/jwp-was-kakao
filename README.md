# 웹 애플리케이션 서버
## 진행 방법
- 웹 애플리케이션 서버 요구사항을 파악한다.
- 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
- 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
- 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
- [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

---

### 1. GET /index.html 응답하기
- "http://localhost:8080/index.html" 에 접근할 수 있도록 구현한다.
- RequestHandlerTest 테스트가 모두 통과하도록 구현한다.
  - [x] Request Header를 파싱한다.
  - [x] 요청에 맞는 URL로 연결한다.
  - [x] 파일 읽어 응답만들기


### 2. CSS 지원하기
- Stylesheet 파일을 지원하도록 구현하도록 한다.


### 3. Query String 파싱
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입할 수 있다.
- 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
- HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 RAM 메모리에 저장한다.
  - [x] 쿼리를 파싱한다.
  - [x] User 객체를 만든다.
  - [x] User 객체를 데이터베이스에 저장한다.


### 4. POST 방식으로 회원가입
- "http://localhost:8080/user/form.html" 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.
  - [x] request body 를 파싱하여 3번과 같은 동작을 하도록 한다.


### 5. Redirect
- 현재는 “회원가입”을 완료 후, URL이 /user/create 로 유지되는 상태로 읽어서 전달할 파일이 없다. redirect 방식처럼 회원가입을 완료한 후 index.html로 이동해야 한다.
  - [x] 다양한 response 를 줄 수 있도록 하기 위해 Response 객체와 ResponseBuilder 도입
  - [x] 기존 create 처리하는 부분에서 redirect 관련 response 설정하도록 수정


### 리팩토링
- 원시값, 문자열 포장하기, 일급 콜렉션 사용하기 등의 클래스 분리
  - [x] HttpMethod, HttpStatus, Content-Type 등
  - [x] RequestHeaders 와 RequestBody 를 포함하는 Request 객체
- controller 대신 Servlet 으로 변경
  - [x] Servlet 형태로 변경 - doPost, doGet 을 갖는 interface 와 구현체
  - [x] ServletContainer - Servlet 들을 등록하고 매핑시키는 객체. Servlet 에서 발생하는 예외에 대한 try catch.
  - [x] Container 초기화
- [ ] 메소드당 10 라인 내로 고치기
