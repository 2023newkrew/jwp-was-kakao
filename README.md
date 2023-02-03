# 웹 애플리케이션 서버

## 요구사항
### GET /index.html 응답하기
* [x] http://localhost:8080/index.html에 접근할 수 있도록 구현한다.
* [x] RequestHandlerTest 테스트가 모두 통과하도록 구현한다.

### CSS 지원하기
* [ ] Stylesheet 파일을 지원하도록 구현하도록 한다.

### String Parsing을 이용하여 POST 방식으로 회원가입
* [ ] http://localhost:8080/user/form.html 파일의 form 태그 method를 `post`로 방식으로 회원가입 기능이 정상적으로 동작하도록 구현한다.
* [ ] 사용자가 입력한 값을 파싱해 회원가입 정보를 `model.User` 클래스에 저장하고 `DataBase.addUser()` 메서드를 활용해 RAM 메모리에 저장한다.

### Redirect
* [ ] “회원가입”을 완료 후 `/user/create`에서 redirect를 이용하여 `index.html`로 이동해야 한다.