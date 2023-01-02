# MoziServer

'제로리'는 친환경 활동이 일상화되도록 돕는 친환경 습관 형성 애플리케이션입니다.

'MosiServer'는 제로리의 서버 이름입니다.

<br/>

# 프로젝트 소개

## 프로젝트 배경

지구를 파괴하는 환경 오염의 심각성에 대한 인식이 높아지면서 사람들은 경각심을 가지기 시작하였습니다. 그러나 일상 생활의 환경 보호가 익숙하지 않아 지속되지 않고 또 방법을 알기 어려워 시작을 어려워하는 문제점이 발생되고 있어 이를 해결하기 위한 서비스를 생각하였습니다.
즉, 편한 시작과 즐거운 지속으로 제로의 자연스러운 일상화를 이끌고자 했습니다.

## 기능
1. 미션 형식의 활동
   <br/>
   <img src="https://user-images.githubusercontent.com/47858282/200795434-61659431-473f-46db-94da-72c9dd061eab.jpg" width="300" height="500"/>
2. 멸종위기 동물 이주시키기
   <br/>
   <img src="https://user-images.githubusercontent.com/47858282/200795978-fb75a245-ebd7-42fc-b3fe-a81c468a5201.jpg" width="300" height="500"/>
3. 인증하는 제로 활동
   <br/>
   <img src="https://user-images.githubusercontent.com/47858282/200796307-d275d77e-fe86-43a5-b128-27c23d4fca7d.jpg" width="300" height="500"/>

<br/>

# 기술스택

> - Back-End : <img src="https://img.shields.io/badge/Spring Boot 2.5-6DB33F?style=for-the-badge&logo=Spring Boot 2.5&logoColor=yellow">&nbsp;<img src="https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/java 11-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white">
> - Common :  <img src="https://img.shields.io/badge/AWS RDS/EC2-232F3E?style=for-the-badge&logo=Amazon&logoColor=white"/>&nbsp;
> - ETC :    <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Trello-0052CC?style=for-the-badge&logo=Trello&logoColor=white"/>

<br/>

# 설계
<img width="786" alt="KakaoTalk_Photo_2022-11-09-21-21-54" src="https://user-images.githubusercontent.com/47858282/200829291-d7c99d14-6d36-4354-8100-05abcb7f5627.png">

# **🐰🐯** Commit C**onvention**

## 🛼 Git Workflow

Branch를 생성하기 전 Issue를 먼저 작성하고,

`<Prefix>/#<Issue_Number>` 의 양식에 따라 브랜치 명을 작성합니다.

## 🛼 Branch Prefix

- `develop` : feature 브랜치에서 구현된 기능들이 merge될 브랜치. **default 브랜치이다.**
- `feature` : 기능을 개발하는 브랜치, 이슈별/작업별로 브랜치를 생성하여 기능을 개발한다
- `main` : 개발이 완료된 산출물이 저장될 공간
- `release` : 릴리즈를 준비하는 브랜치, 릴리즈 직전 QA 기간에 사용한다
- `bug` : 버그를 수정하는 브랜치
- `hotfix` : 정말 급하게, 제출 직전에 에러가 난 경우 사용하는 브렌치

---

## 🧸 Commit Format

`[prefix 소문자로 시작] #이슈번호 - 이슈내용` or `[prefix] 커밋내용`

Ex. [feat] #1 - 회원가입

## 🧸 Commit Prefix

| Type 키워드 | 사용 시점 |
| --- | --- |
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| docs | 문서 수정 |
| style | 코드 스타일 변경 (코드 포매팅, 세미콜론 누락 등)기능 수정이 없는 경우 |
| design | 사용자 UI 디자인 변경 (CSS 등) |
| test | 테스트 코드, 리팩토링 테스트 코드 추가 |
| refactor | 코드 리팩토링 |
| build | 빌드 파일 수정 |
| ci | CI 설정 파일 수정 |
| perf | 성능 개선 |
| chore | 빌드 업무 수정, 패키지 매니저 수정 (gitignore 수정 등) |
| rename | 파일 혹은 폴더명을 수정만 한 경우 |
| remove | 파일을 삭제만 한 경우 |

---

## 🎈Issue Format (using template)

```markdown
## 목적
> 

## 작업 상세내용
- []

## 참고 사항
[링크 이름](https://www.naver.com/)
```

---

## 🎈PR Format (using template)

```markdown
## 개요 
- Issue 번호
- 작업 내용

### 세부 작업 내용
- 로그인 기능 추가했습니다.
- 아주 아주 상세하게 적기

### PR 타입(하나 이상의 PR 타입을 선택해주세요)
- [] 기능 추가
- [] 기능 삭제
- [] 버그 수정
- [] 의존성, 환경 변수, 빌드 관련 코드 업데이트

### 반영 브랜치
ex) feat/login -> dev

### 테스트 결과 (optional)
ex) 베이스 브랜치에 포함되기 위한 코드는 모두 정상적으로 동작해야 합니다. 결과물에 대한 스크린샷, GIF, 혹은 라이브

### 특이 사항 (optional)
- 로컬 및 개발 서버에 환경변수 추가되었습니다.
```
