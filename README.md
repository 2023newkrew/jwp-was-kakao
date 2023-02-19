# 웹 애플리케이션 서버
### 구현 목록 (요구 사항)
- [x] Request Header 읽어 오기 구현
- [x] HttpRequestBuilder 구현
- [x] GET /index.html 응답 구현
- [x] css, js 등을 적용할 수 있도록 구현
- [x] 쿼리스트링 파서 구현
- [x] 회원 가입 구현
- [x] 회원가입 리다이렉트 구현

### 부가적인 코드 개선
- [x] FrontController 구현
- [x] Api 어노테이션을 통해 메소드 매핑 구현
- [x] Controller를 통해서 요청을 처리하도록 구현
- [x] 존재하지 않는 페이지에 대해 404 응답을 내려주도록 구현
- [x] Service, Dao 레이어 분리

### 1차 피드백 개선
- [x] Map에 key, value 타입 지정하기
- [x] Delimiter들 상수 처리하기
- [x] token substring 관련 가독성 높히기
- [x] requestBody 생성 시 content-type 활용하기
- [x] ResponseUtils 개선하기 (응답 처리 개선)
  - [x] throws IOException에 대한 대책 생각
  - [x] in-line 코딩에 대한 의견 제시
- [x] handlerController map에 컨트롤러 매핑시 UserController에 정의되어 있는 값들을 읽어와서 세팅 시도
- [x] 동일한 method, url에 대해서 content-type이 다른 경우 어떻게 처리해야 할지 고민

### 2단계 구현 목록
- [x] 응답 객체를 조작할 수 있도록 변경
- [x] 로그인 기능 구현
  - [x] 계정 또는 비밀번호 불일치 확인
  - [x] 로그인 성공 시 쿠키 세팅 확인
- [x] 비로그인 상태일 경우, 유저 리스트 이동 시 user/login.html로 리다이렉트
- [x] 로그인 상태일 경우, 유저 리스트로 이동하도록 구현
- [x] Session 구현하기
  - [x] 로그인 된 유저가 로그인하려 할 경우 index.html로 리다이렉트