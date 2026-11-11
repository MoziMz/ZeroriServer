# REVIEW.md

ZeroriServer(Spring Boot 3.2 / Java 17 / Gradle)용 예시입니다. 레포 루트에 두고 쓰세요.

## 우선순위
1. 실제 버그 (데이터 손실, 500 에러, 잘못된 응답, 다른 사용자 데이터 노출)
2. 보안 문제 (인증·인가 우회, 입력 검증 누락)
3. 그 외는 지적하지 않는다

## 영역별 "실패"의 정의
- `security/`: 인증 없이 접근 가능해지거나 세션이 다른 사용자 것으로 섞인다
- `service/`: 트랜잭션 경계가 잘못되어 일부만 저장된다. 같은 요청이 두 번 와서 중복 생성된다
- `service/AsyncService`: 비동기 콜백이 그 사이 바뀐 상태를 덮어쓴다
- `controller/`, `adminController/`: 요청/응답 필드가 말없이 바뀌어 클라이언트가 깨진다. `@Valid` 검증이 빠진다
- `repository/`: QueryDSL 조건이 빠져 엉뚱한 행을 읽거나 지운다
- `model/`: 엔티티 제약·연관관계가 기존 테이블과 어긋난다
- `httpException/`: 실패를 200으로 돌려준다
- `config/`: 엉뚱한 환경으로 라우팅되거나 비밀값이 평문으로 들어간다
- `src/main/resources/`: 프로필별 설정이 엇갈려 엉뚱한 환경을 가리킨다

## DB 스키마
스키마는 레포가 아니라 사람이 관리한다. 엔티티를 추가하면 **배포 전에 운영/데브 DB에
DDL을 직접 실행**한다. `spring.jpa.hibernate.ddl-auto`는 `application.yml`에서 주석
처리되어 있고(외부 MySQL이므로 기본값 `none`), Hibernate는 테이블을 만들지도 검증하지도
않는다. 따라서 테이블이 없어도 앱은 정상 기동하고, 그 테이블을 처음 건드리는 요청에서
런타임 오류가 난다.

- **"PR에 마이그레이션 파일이 없다"는 사실만으로는 지적하지 않는다.** 레포 구조상 항상 없다
  (`.sql`, Flyway, Liquibase 모두 없음).
- 단, 새 엔티티나 컬럼 때문에 **배포 시 테이블/컬럼이 없어 깨질 수 있으면** 지적하고,
  필요한 DDL을 적어 준다. 기동이 아니라 런타임에 터지므로 리뷰에서 잡는 값이 크다.
- 엔티티 정의가 기존 테이블과 어긋나는 경우(컬럼 타입, nullable, 유니크 제약, 연관관계)도
  지적한다.

## 건너뛸 경로
- `src/main/generated/`, `build/`, `bin/`, `.gradle/`
- `gradle/wrapper/`, `*.lock`
- `src/main/resources/static/`

## 코멘트 규칙
- 모든 지적에 `파일:라인`과 근거 코드를 붙인다. 클래스 이름만 보고 추측하지 않는다.
- Nit은 PR당 최대 3개.
- 포맷팅, 네이밍 취향, import 순서, Lombok 사용 여부는 언급하지 않는다.
- 레이어 컨벤션은 `.github/CONVENTION/{controller,entity,repository,service,res}.md`를 기준으로 판단한다.
- QueryDSL `Q` 클래스는 빌드 산출물이므로 지적하지 않는다.
- 이 PR이 만들지 않은 기존 문제는 `pre_existing`으로 표시한다.

## 요약 형식
- 첫 줄은 개수로 시작한다. 예: "Important 1, Nit 2"
- 지적할 게 없으면 "수정할 사항 없음"이라고만 쓴다.
