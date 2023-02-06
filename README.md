# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


### 기능 구현 목록
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