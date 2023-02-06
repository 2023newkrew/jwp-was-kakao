# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


## 요구사항 분석
- [x] 헤더 파싱 클래스
  - [x] 메서드, 리소스, http 버전 추출
  - [x] 각종 헤더값들
    - [x] 헤더를 파싱해서 저장할 Request 생성
- [x] RequestHandler -> 메서드, uri에 따라서 분기
  - [x] html -> templates/
  - [x] 이 외 -> static/
- [ ] URL 파싱 -> 파라미터 추출