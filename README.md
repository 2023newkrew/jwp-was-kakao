# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


## 기능 요구사항
1. ```GET /index.html```: index.html 파일 응답
2. ```GET /css/styles.css```: styles.css 파일 응답 (text/css)
3. Query String 파싱하여 User 저장
4. ```POST /user/create```: 3번을 POST 요청으로 수정 (application/x-www-form-urlencoded)
5. ```POST /user/create```: 요청 처리 후 /index.html로 redirect

## 리팩토링
1. 요청을 읽어와서 파싱하는 로직을 RequestParser로 분리
2. FrontController에서 요청 유형을 분리하여 처리
   - 문자열: 루트 경로일 경우에만 문자열 전송
   - 파일: 요청 경로에 매핑되는 파일 내용 전송
   - POST: 요청 작업 처리 후 redirect
3. 응답 정보를 객체에 담아 응답 전송 로직을 ResponseWriter로 분리