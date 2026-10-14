# ZeroriServer 프로젝트 컨텍스트

## 목적

이 문서는 ZeroriServer에서 작업하는 에이전트의 시작점이다. 로그 분석, 코드
리뷰, 보안 점검, 문서 정리 전에 프로젝트 구조와 판단 기준의 위치를 파악하는 데
사용한다.

## 정보의 우선순위

문서와 코드가 다르면 다음 순서로 신뢰한다.

1. 현재 소스 코드와 빌드 설정
2. 실행 설정과 배포 워크플로
3. 기존 프로젝트 문서

`README.md`에는 Java 11, Spring Boot 2.5.5가 적혀 있지만, 현재 기준은
`build.gradle`의 Java 17, Spring Boot 3.2.6이다.

## 기술 스택

| 영역 | 현재 구현 |
| --- | --- |
| 언어와 프레임워크 | Java 17, Spring Boot 3.2.6, Spring MVC |
| 영속성 | Spring Data JPA, QueryDSL, MySQL |
| 보안 | Spring Security, JDBC 기반 remember-me, BCrypt, 소셜 ID 토큰 검증 |
| API와 검증 | Spring Web, Bean Validation, springdoc OpenAPI/Swagger |
| 외부 연동 | OpenFeign, AWS S3, Firebase Admin, Kakao, Naver, Apple, GitHub Models |
| 관측성 | Logback JSON API 로그, Sentry, Elastic APM, Slack 경고 알림 |
| 빌드와 배포 | Gradle, GitHub Actions, Docker 기반 배포 워크플로 |

## 코드 구조

| 위치 | 책임 |
| --- | --- |
| `controller/`, `adminController/` | HTTP 엔드포인트와 요청/응답 바인딩 |
| `service/`, `adminService/` | 비즈니스 유스케이스와 트랜잭션 조합 |
| `repository/` | JPA와 QueryDSL 데이터 접근 |
| `model/entity/` | 영속 엔티티 |
| `model/req/`, `model/res/`, `model/adminReq/`, `model/adminRes/` | 요청/응답 DTO |
| `security/` | 인증 Provider, 세션 사용자 처리, remember-me, 접근 실패 처리 |
| `config/` | 보안, AWS, OpenAPI, 웹, 외부 클라이언트 설정 |
| `log/` | API 요청/응답 로그 |
| `rest/` | 외부 서비스용 Feign Client |
| `httpException/` | 애플리케이션 오류 정의와 예외 처리 |
| `common/` | 공통 상수, 유틸리티, 로깅 Appender |

## 인증과 인가

- `config/SecurityConfig.java`가 Spring Security 필터 체인을 정의한다.
- `common/Constant.java`가 공개, 인증 필요, 사용자 역할, 관리자 역할 경로를
  정의한다.
- `security/`에는 이메일 로그인, 소셜 ID 토큰 인증, remember-me 인증이 있다.
- 보안 체인은 `UsernamePasswordAuthenticationFilter`보다 먼저
  `log/ApiLogFilter.java`를 등록한다.

엔드포인트나 보안 변경을 검토할 때는 `SecurityConfig.java`, `Constant.java`의
경로 그룹, 대상 Controller를 함께 확인한다.

## 운영과 관측

- `ApiLogFilter`는 `/api/health-check`를 제외한 API 요청마다 JSON 로그를 남긴다.
- `logback-spring.xml`은 `dev`, `production` 프로필에서 API JSON 로그를 분리하며,
  WARN 이상 애플리케이션 로그를 Sentry와 Slack으로 전송한다.
- `application.yml`은 DB, 메일, 소셜 로그인, JWT, GitHub Models, S3, Sentry,
  Slack 설정을 환경 변수로 참조한다.
- 에이전트 결과에 해석된 환경 변수, 인증 정보, 개인정보를 포함하지 않는다.

## 기존 개발 규칙

코드 리뷰의 프로젝트별 원본 규약은 `.github/CONVENTION/`이다.

- `controller.md`
- `service.md`
- `repository.md`
- `entity.md`
- `res.md`

이 문서는 해당 규약을 대체하거나 중복하지 않는다. 에이전트는 변경된 계층에 맞는
규약을 읽고 적용한다.
