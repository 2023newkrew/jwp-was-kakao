# 웹 애플리케이션 서버
## 기능 요구사항
- [ ] GET /index.html 응답하기
  - [ ] RequestHandlerTest 통과하도록 구현
  - [ ] http header 전체 출력
    - [ ] `while (!"".equals(line)) {}`로 header 마지막 판단
    - [ ] `if (line == null) { return;}`로 line이 null인 경우에 대한 예외 처리
    - [ ] `utils.FileIoUtils`의 `loadFileFromClasspath()` 이용하여 파일 읽기
- [ ] CSS 지원하기
- [ ] Query String 파싱
- [ ] POST 방식으로 회원가입
  - [ ] `IOUtils.readData()` 활용하여 본문 데이터 읽기
- [ ] 회원가입 완료 후 `index.html`로 Redirect