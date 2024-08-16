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

구현 기능

- 입국 허가 요건 API, 현지 연락처 정보 API, 국가 기본 정보 API, 여행 경보 API

  - POST 메소드를 활용, 외교부의 오픈 API를 호출하여 반환받는 데이터들을 가공하여 유저가 검색한 국가의 필요한 정보들만 반환하도록 구현
  입국 심사 질문과 답변 리스트를 가져오는 API

- get 메소드를 활용, DB에 저장된 입국 심사 질문과 답변 데이터들을 반환하도록 구현
  - 한국어 -> 외국어 번역 API

- POST 메소드를 활용, AWS translate를 사용하여 유저가 입력한 텍스트를 선택한 언어로, 번역된 텍스트를 반환하도록 구현
  - 외국어 -> 한국어 번역 API

- POST 메소드를 활용, AWS translate를 사용하여 유저가 입력한 텍스트를 한국어로, 번역된 텍스트를 반환하도록 구현
  - 날씨 정보 API

- POST 메소드를 활용, openWeatherMap API를 호출하여 선택한 위치의 날씨정보를 반환하도록 구현
  - gemini를 활용한 날씨 비교 분석 API

- POST 메소드를 활용, gemini API를 사용하여 사용자의 현재 위치 날씨 정보와 검색한 위치의 날씨정보를 비교 분석하여 반환하도록 구현



✏️ 배포
-

serverless, Docker, AWS LAMBDA 사용해서 배포했습니다.

github Actions로 git pull 자동화했습니다.



✏️ 화면 개발 [android-NBGG-App]
-

### ✉︎ 핵심 기능


![image](https://github.com/user-attachments/assets/5545f6cb-7997-49cc-baa6-c1fe92d4bd50)

### 1. 외교부 오픈 API를 통한 해외 정보 제공
- 화면
<p align="center">
  <img width="400" src="https://github.com/user-attachments/assets/d7726a30-1c6e-4369-949e-f440dd5dcf09">
</p>

- 사용자는 정보가 필요한 국가를 검색하거나 선택할 수 있습니다.
- 사용자는 해당 국가의 기본 정보, 여행 경보, 입국 허가 요건과 현지 연락처를 확인할 수 있습니다.

### 2. AWS Translate 를 사용한 번역기능
- 화면
<div>
  <p align="center">
    <img width="200" src="https://github.com/user-attachments/assets/de16ce59-f19f-4671-bb1e-2f308d7dd878">
    <img width="200" src="https://github.com/user-attachments/assets/96b6fa71-8de2-4b03-adc1-b013b68a2f61">
  </p>
</div>

- 입국심사 예상 질문의 답변을 직접 작성해서 원하는 언어로 번역합니다.
- 마이크 접근을 허용하여 대화 형식으로 한 화면에서 사용자와 외부 사용자의 대화를 번역합니다.

### 3. OpenWeather API 와 Gemini 인공지능을 활용한 선택한 지역 날씨와 현재 위치 날씨 
- 화면
<div>
  <p align="center">
    <img width="200" src="https://github.com/user-attachments/assets/576ea7c6-c07a-4cb2-b455-738b39467954">
    <img width="200" src="https://github.com/user-attachments/assets/315c1d74-48ab-410f-9e97-064e477e0ccc">
    <img width="200" src="https://github.com/user-attachments/assets/4e1dce45-fba4-4679-b348-e83664b2254e">
  </p>
</div>

- 처음 앱을 들어가면 현재 위치를 가져와 현재 위치의 날씨 정보를 전달합니다.
- 사용자는 궁금한 전세계 날씨를 지도를 보고 선택하거나 검색할 수 있습니다.
- 선택한 지역의 날씨를 가져오고 인공지능을 사용해 현재위치와 검색한 위치의 날씨를 비교해서 설명해줍니다.


✏️ 서버 아키텍처
-
<p align="center">
  <img src="https://github.com/user-attachments/assets/b9f340e5-e825-4416-8f10-d7e9439e3835">
</p>


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


