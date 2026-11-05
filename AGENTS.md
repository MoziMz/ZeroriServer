# ZeroriServer

Spring Boot 기반 ZeroriServer 백엔드입니다. `main`은 운영(prod) 브랜치이며 모든 작업 브랜치는 여기서 분기합니다. `dev`는 push 시 데브 서버에 배포되고 PR 없이 로컬 머지로 받는 검증용 브랜치입니다.

## 브랜치와 커밋

- `main` = 운영, `dev` = 데브 환경; 작업 브랜치는 `main`에서 분기하고 PR은 항상 `main`으로 생성합니다.
- 작업 브랜치: `feature/#<이슈번호>`
- 커밋: `[feat|fix|chore|test|refactor] #<이슈번호> - <English message>`
- 예: `[fix] #154 - Fix Spring Security auto-login error`

## 빌드·테스트

```bash
./gradlew build
```

코드를 작성하기 전에 해당 레이어의 `.github/CONVENTION/{controller,entity,repository,service,res}.md`를 먼저 확인합니다.

## PR 올리기

변경을 커밋하고 본문 Markdown 파일을 준비한 뒤 실행합니다.

```bash
scripts/open-pr.sh "PR title" /path/to/pr-body.md
```

Codex 샌드박스에서 push 또는 `gh`가 네트워크 차단으로 실패하면 `--sandbox workspace-write -c 'sandbox_workspace_write.network_access=true'`로 재실행해야 합니다.
