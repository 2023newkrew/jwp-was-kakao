# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단계 - HTTP 웹 서버 구현하기

- GET /index.html 응답하기
  - http://localhost:8080/index.html에 접근할 수 있도록 구현한다.
  - RequestHandlerTest 테스트가 모두 통과하도록 구현한다.
- CSS 지원하기
- Query String 파싱
- POST 방식으로 회원가입
- Redirect
- 로그인 기능 구현