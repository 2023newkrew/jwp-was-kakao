# 웹 애플리케이션 서버
## 요구사항
### 1단계
1. GET /index.html 응답 
    - http://localhost:8080/index.html 접근 가능
    - 해당하는 파일 `src/main/resources` 디렉토리에서 읽어 전달
       ```http request
       GET /index.html HTTP/1.1
       Host: localhost:8080
       Connection: keep-alive
       Accept: */*
        ```
2. CSS 지원 
   - stylesheet 파일 지원하도록 구현
   - 해당하는 파일 `src/main/resources` 디렉토리에서 읽어 전달
     ```http request
     GET ./css/style.css HTTP/1.1
     Host: localhost:8080
     Accept: text/css,*/*;q=0.1
     Connection: keep-alive
     ```
3. Query String 파싱
    - 회원가입 메뉴 클릭시 http://localhost:8080/user/form.html 으로 이동하면서 회원가입
    - 사용자가 입력한 값을 파싱해 model.User 클래스에 저장 
    - 생성한 User 객체를 `DataBase.addUser()` 메서드 활용해 RAM 메모리에 저장
      ```http request
      GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1
      Host: localhost:8080
      Connection: keep-alive
      Accept: */*
      ```
4. POST 방식으로 회원가입 
   - http://localhost:8080/user/form.html 파일의 form 태그 method post로 수정 후 회원가입 기능 정상 동작하도록 구현
     ```http request
     POST /user/create HTTP/1.1
     Host: localhost:8080
     Connection: keep-alive
     Content-Length: 59
     Content-Type: application/x-www-form-urlencoded
     Accept: */*
  
     userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com
     ```
5. Redirect
   - 회원가입 완료 후, URL이 `/user/create`로 유지되는 상황에서
   - 회원가입 완료한 후 `index.html`로 이동하도록 함
### 2단계
1. 로그인 기능 구현
   - 로그인 성공시 `index.html`으로 이동
   - 로그인 실패시 `/user/login_failed.html`로 이동
   - 로그인 성공시 `JSESSIONID` 이름으로 쿠키 설정 (모든 요청에 대해 쿠키 처리 가능하도록 Path `/`로 설정)
2. 템플릿 엔진 활용
   - 사용자가 로그인 상태일 경우 http://localhost:8080/user/list 로 접근했을 때 사용자 목록을 출력
   - 사용자가 로그인 상태가 아닐 경우 로그인 페이지(`login.html`)로 이동
   - 동적 html 생성을 위해 handlerbars.java template engine 활용
3. Session 구현
   - 쿠키에 전달 받은 `JSESSIONID` 값으로 로그인 여부 체크
   - 로그인 성공시 Session 객체 값으로 User 객체 저장
   - 로그인 상태에서 `/user/login` 페이지 접근시 이미 로그인 상태이므로 `index.html`로 리다이렉트