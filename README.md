[//]: # (# 웹 애플리케이션 서버)

[//]: # (## 진행 방법)

[//]: # (* 웹 애플리케이션 서버 요구사항을 파악한다.)

[//]: # (* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request&#40;이하 PR&#41;를 통해 코드 리뷰 요청을 한다.)

[//]: # (* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.)

[//]: # (* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.)

[//]: # ()

[//]: # (## 온라인 코드 리뷰 과정)

[//]: # (* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정]&#40;https://github.com/next-step/nextstep-docs/tree/master/codereview&#41;)

[//]: # ()

# 1단계 - HTTP 웹 서버 구현하기

https://edu.nextstep.camp/s/GiKTqpMP/ls/CYcM3ljf

## 요청 처리 과정

1. `Webserver` 클래스에서 8080 포트로의 요청에 따라 커넥션이 생성되면 `RequestHandler`를 실행하는 스레드를 생성한다.
2. `RequestHandler`는 커넥션의 `InputStream`으로 부터 요청을 읽어 `HttpRequest` 객체를 생성하여 요청을 처리한다.
3. `RequestHandler`는 처리한 요청에 따라 `HttpResponse` 객체를 만들고, 응답 객체의 `writeToOutputStream` 메서드를 이용해 커넥션에 `OutputStream`에 응답을
   바이트로 작성한다.
4. 모든 예외에 대한 에러 `HttpResponse` 객체를 생성하여 응답을 전달한다.
5. 각 `Controller` 객체는 싱글톤으로 관리되며 `HttpRequest` 객체를 받아 처리하여 `HttpResponse` 객체를 생성하여 반환한다.
    - `UserController`는 요청에 따라 User 객체를 생성하여 인메모리 DB인 `DataBase`에 User 정보를 저장한다.
    - `StaticFileController`는 특정 경로에 정적 파일을 반환하는 요청을 처리한다.
        - 요청된 경로의 파일의 Content-type에 따라 알맞은 응답을 생성한다.

## 기능 요구 사항

### 1. GET /index.html 응답하기

- /index.html 경로의 GET 요청에 응답한다.
- 정적 파일을 반환해주어야 하므로 `StaticFileController` 에서 요청을 처리한다.
- `FileIoUtils.loadFileFromClasspath` 메서드를 통해 요청된 경로의 정적 파일을 바이트 형식으로 가져온다.
    - 만약 지정된 경로에 정적파일이 존재하지 않다면 `ResourceNotFoundException`이 발생한다.

### 2. CSS 지원하기

- 모든 정적 파일에 대한 GET 요청에 응답한다.
- 정적 파일을 반환해주어야 하므로 `StaticFileController` 에서 요청을 처리한다.
- `FileIoUtils.loadFileFromClasspath` 메서드를 통해 요청된 경로의 정적 파일을 바이트 형식으로 가져온다.
    - Regex를 이용하여 알맞은 경로로 부터 정적 파일을 가져온다.
        - `.html`의 경우 `/template` 패키지, `.css`의 경우 `/static` 패키지
    - 만약 지정된 경로에 정적파일이 존재하지 않다면 `ResourceNotFoundException`이 발생한다.

### 3. Query String 파싱

- /user/create?{queryParams} 경로의 GET 요청에 응답한다.
- 요청에 따라 User를 생성하므로 `UserController`에서 요청을 처리한다.
    - `createUserGet` 메서드
- Request 헤더의 queryParams를 파싱하여 User 객체를 생성한다.
    - `IOUtils.extractUserFromPath` 메서드로 url로 부터 `Map<String,String> userInfo` 객체를 생성한다.
    - userInfo 객체를 통해 User 객체를 생성한다.
- 생성된 User 객체를 인메모리 DB인 `DataBase`에 저장한다.

### 4. POST 방식으로 회원가입

- /user/create 경로의 POST 요청에 응답한다.
- 요청에 따라 User를 생성하므로 `UserController`에서 요청을 처리한다.
    - `createUserPost` 메서드
- Request의 body에서 params를 파싱하여 User 객체를 생성한다.
- 이후의 과정은 GET 메서드로 회원가입과 동일하다.

### 5. Redirect

- /user/create로의 GET, POST 요청에 대한 응답으로 302 FOUND를 반환한다.
- 요청에 따라 User를 생성하므로 `UserController`에서 요청을 처리한다.
- `createUserGet`, `createUserPost` 메서드의 반환값을 수정한다.
    - 302 FOUND Response를 생성하여 반환한다.
    - redirectURI는 /index.html 이다.
