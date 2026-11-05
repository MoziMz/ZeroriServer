#!/usr/bin/env bash

set -euo pipefail

usage() {
  echo "Usage: $0 <PR title> <PR body markdown file>" >&2
  exit 1
}

[[ $# -eq 2 ]] || usage

title="$1"
body_file="$2"

[[ -f "$body_file" ]] || {
  echo "Error: PR body file not found: $body_file" >&2
  exit 1
}

branch="$(git branch --show-current)"
[[ -n "$branch" ]] || {
  echo "Error: detached HEAD is not supported." >&2
  exit 1
}

if [[ "$branch" == "main" || "$branch" == "dev" ]]; then
  echo "Error: do not create or push a PR from $branch." >&2
  exit 1
fi

if [[ -n "$(git status --porcelain)" ]]; then
  echo "Error: commit or stash all changes before opening a PR." >&2
  exit 1
fi

git push -u origin "$branch"

existing_pr="$(gh pr list --head "$branch" --base main --json number --jq '.[0].number')"

if [[ -n "$existing_pr" && "$existing_pr" != "null" ]]; then
  gh pr edit "$existing_pr" --title "$title" --body-file "$body_file" >/dev/null
  pr_url="$(gh pr view "$existing_pr" --json url --jq '.url')"
else
  pr_url="$(gh pr create --base main --draft --title "$title" --body-file "$body_file")"
fi

printf '%s\n' "$pr_url"
