## 요구사항

### 1. GET /index.html 응답하기
- [x] http://localhost:8080/index.html 에 접근할 수 있도록 구현한다.
- [x] RequestHandlerTest가 모두 통과하도록 구현한다.

### 2. CSS 지원하기
- [x] CSS 파일을 지원하도록 구현한다.

### 3. Query String 파싱
- [x] “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입할 수 있다.
- [x] 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
- [x] HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- [x] 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 RAM 메모리에 저장한다.

###  4. POST 방식으로 회원가입
- [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.