<img src="https://capsule-render.vercel.app/api?type=venom&color=8EB075&height=150&section=header&text=NBGG_SERVICE&animation=fadeIn&fontSize=40" />

# 날보고가 서비스
### 인공지능과 오픈 API를 활용한 해외 정보 전달 서비스

[프로젝트 기술서](https://docs.google.com/presentation/d/1AQag_PWCm9ceecWCT3EUIB5WncG7KaB6tDUjOzdZFdY/edit#slide=id.p)
| [앱 구현 영상](https://www.youtube.com/watch?v=19rsdIO6g-w)
| [Figma 화면 기획서](https://www.figma.com/design/laSgfv5ctjrYBpJpAe5FN5/%EB%82%A0%EB%B3%B4%EA%B3%A0%EA%B0%80-%EC%95%B1-%ED%99%94%EB%A9%B4-%EA%B8%B0%ED%9A%8D%EC%84%9C?node-id=0-1&t=B3bLwn9HAZ49UwtT-1) | [API 명세서 (Postman)](https://documenter.getpostman.com/view/35043308/2sA3kXDzoe)



✏️ 작업순서
-

데이터 주제 선정 ➡︎ 데이터 가공(Teachable Machine, 웹 크롤링, 주피터노트북) ➡︎ 화면기획서 (Figma) ➡︎ 테이블 명세서 작성(ERD Cloud) ➡︎ github 레파지토리 생성 ➡︎ 서버개발 | API 명세서 (postman) ➡︎ DB 인덱스 적용 ➡︎ 안드로이드 스튜디오 개발




✏️ 데이터 가공 [NBGG-data]
-

OPEN API 요청으로 오는 답변 크롤링 및 문서 크롤링 한 데이터 pandas 사용해서 데이터 가공



✏️ Server 개발 [aws-NBGG-server]
-

Visual Studio Code를 사용해서 서버 개발 (Python)
- Framwork는 Flask, Serverless 사용했습니다.

서버 아키텍처
- AWS IAM, LAMBDA, RDS(MySQL), ECR(Docker) 로 구성했습니다. 

VSC 폴더명으로 정리했습니다.
✉︎ APP.PY
- 환경변수 세팅
- Entry Point. api.add_resource 입력
- 도커사용을 위한 핸들러 코드

✉︎ COUNTRY.PY
- 0

✉︎ TRANSLATE.PY 
- 0

✉︎ WEATHER.PY
- 0




✏️ 배포
-

serverless, Docker, AWS LAMBDA 사용해서 배포했습니다.

github Actions로 git pull 자동화했습니다.



✏️ 화면 개발 [android-NBGG-App]
-


파일명으로 정리했습니다.

✉︎ JAVA
- ✉︎ ADAPTER
  - 0
- ✉︎ API
  - 0
- ✉︎ CONFIG
  - 0
- ✉︎ MODEL
  - 0




✏️ 사용한 프로그램
-

<a href="https://jupyter.org/"><img src="https://img.shields.io/badge/jupyter-F37626?style=flat-square&logo=jupyter&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/>
<img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat-square&logo=Visual Studio Code&logoColor=white"/>
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>
<img src="https://img.shields.io/badge/TensorFlow-FF6F00?style=flat-square&logo=tensorflow&logoColor=white"/>
<img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=figma&logoColor=white"/>
<img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat-square&logo=Android Studio&logoColor=white"/>


<img src="https://img.shields.io/badge/Flask-000000?style=flat-square&logo=flask&logoColor=white"/> <img src="https://img.shields.io/badge/serverless-FD5750?style=flat-square&logo=serverless&logoColor=white"/>



✏️ 사용한 언어
-

<img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=Python&logoColor=white"/>


