# 웹 애플리케이션 서버
## 기능 요구사항
- [x] GET /index.html 응답하기
  - [x] RequestHandlerTest 통과하도록 구현
  - [x] http header 전체 출력
    - [x] 헤더 정보를 담기 위한 `RequestHeader` 클래스 구현
    - [x] `while (!"".equals(line)) {}`로 header 마지막 판단
    - [x] `if (line == null) { return;}`로 line이 null인 경우에 대한 예외 처리
    - [x] `utils.FileIoUtils`의 `loadFileFromClasspath()` 이용하여 파일 읽기
- [x] CSS 지원하기
- [x] Query String 파싱
- [x] POST 방식으로 회원가입
  - [x] `IOUtils.readData()` 활용하여 본문 데이터 읽기
- [x] 회원가입 완료 후 `index.html`로 Redirect