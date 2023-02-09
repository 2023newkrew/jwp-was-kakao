# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


### 1단계 기능 구현 목록
1. GET /index.html 응답하기
    - 요청을 파싱하여 Request에 저장
    - 요청한 리소스를 읽어 Response body에 담아 반환
---
2. CSS 지원하기
   - static 디렉토리 아래 리소스에 대한 요청 판별하여 파일 읽기
---
3. Query String 파싱
    - 요청 URL에서 쿼리 스트링 파싱하여 쿼리 파라미터 추출
    - User 객체 생성하여 저장
---
4. POST 방식으로 회원가입
    - 요청 URL 경로에 따른 핸들러 메서드 매핑 구현
    - 요청 body에 있는 form 데이터 파싱하여 추출
    - User 객체 생성하여 저장
---
5. Redirect
    - Response에 302 status code와 Location 헤더를 지정하여 리다이렉트 구현

---
### 리팩토링 목록
#### 리뷰 반영
- [x] QueryParameters 클래스명 오타 수정
- [x] RequestHandler의 mapRequest 메서드 하나의 기능만 처리하도록 리팩토링
- [x] static 리소스 판별 메서드 Request 내부로 이동
- [x] StaticDirectory enum 리팩토링
  - capacity 수정 및 상수 선언
  - resolve() 파라미터 이름 수정
  - 삼항연산자 제거
- [x] RequestHeader의 메서드에서 depth 2이상인 코드 Steam API로 리팩토링
- [x] printStackTrace 대신 에러 추적성을 위해 logger로 로그 출력


- [x] 어노테이션 기반으로 컨트롤러의 핸들러 메서드 매핑하기 
- [x] 어노테이션 기반으로 컨트롤러 클래스 판별하기

---
### 2단계 기능 구현 목록
- [x] 로그인 기능 구현
  - 로그인 클릭 시 `http://localhost:8080/user/login.html` 로 이동
  - 로그인 성공 시 `index.html` 로 이동
  - 로그인 실패 시 `/user/login_failed.html` 로 이동
  - 세션 아이디 이름 : `JSESSIONID`
    - 서버에서 HTTP 응답 시 헤더에 `Set-Cookie` 추가, `JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46` 형태로 전달하면 클라이언트 요청 헤더의 `Cookie` 필드에 값이 추가됨


  - 서버로부터 쿠키 설정된 클라이언트의 HTTP Request Header 예
    ```
    GET /index.html HTTP/1.1
    Host: localhost:8080
    Connection: keep-alive
    Accept: */*
    Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46; Path=/
    ```
  - Cookie 클래스 추가, Request Header의 Cookie에 JSESSIONID가 없으면 Response Header에 Set-Cookie 추가해 반환
    ```
    HTTP/1.1 200 OK 
    Set-Cookie: JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46
    Content-Length: 5571
    Content-Type: text/html;charset=utf-8;
    ```

---
- [ ] 템플릿 엔진 활용하기
  - [ ] Cookie값이 logined=true 인 사용자는 `http://localhost:8080/user/list` 로 접근했을 때 사용자 목록을 출력, 만약 로그인하지 않은 상태라면 로그인 페이지(`login.html`)로 이동
  - [ ] 동적 html 생성을 위해 handlebars.java template engine 활용

---
- [x] Session 구현하기
  - 쿠키로 전달받은 `JSESSIONID` 값으로 로그인 여부 체크
  - 로그인 성공 시 Session 객체의 값으로 User 객체 저장
  - 로그인 상태에서 `/user/login` 페이지에 HTTP GET method로 접근하면 `index.html` 페이지로 리다이렉트
  - Session 구현, name을 키로 하여 객체 값을 저장 
  - SessionManager 구현, UUID를 키로 하여 Session 객체를 저장
  