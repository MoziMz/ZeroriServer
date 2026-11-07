---
name: open-pr
description: ZeroriServer의 완료된 작업을 확인하고 draft PR을 생성하거나 갱신할 때 사용합니다.
---

# PR 올리기

완료된 ZeroriServer 변경 사항을 `main` 대상으로 draft PR로 올릴 때 사용합니다.

1. `git status`와 `git diff main...HEAD`로 변경 범위를 확인합니다.
2. 브랜치명 `feature/#N`에서 이슈 번호를 파악합니다. 없으면 사용자에게 질문합니다.
3. `[feat|fix|chore|test|refactor] #N - <영문 메시지>` 형식으로 커밋하고, 해당 타입을 선택한 근거를 한 줄로 설명합니다.
4. `./gradlew build -x test`로 빌드 통과를 확인합니다.
5. `.github/PULL_REQUEST_TEMPLATE.md`의 모든 섹션을 채운 PR 본문을 임시 파일로 작성합니다.
   - **세부 작업 내용**은 실제 diff를 기준으로 구체적으로 작성하고 템플릿 예시 문구는 제거합니다.
   - **반영 브랜치**는 `feature/#N -> main`으로 작성합니다.
6. `scripts/open-pr.sh "<제목>" <본문-파일>`을 실행합니다.
7. 출력된 PR URL을 사용자에게 보고합니다.

## 가드레일

- `main`이나 `dev`에 직접 커밋하거나 푸시하지 않습니다. 모든 작업 브랜치는 `main`에서 분기합니다.
- 데브 환경 배포를 위한 `dev` 병합은 자동으로 수행하지 않습니다. 사용자가 명시적으로 요청하고 실행 전에 확인한 경우에만 수행합니다.
- PR은 항상 draft로 생성하며, 병합은 사람만 수행합니다.
- 푸시 전에 diff에 토큰, 비밀번호, 키가 포함되지 않았는지 확인합니다.
- 요청한 작업 범위를 벗어난 파일은 수정하지 않습니다.
