#!/usr/bin/env bash
# Release check for the IntelliJ plugin. Runs every check and summarizes.
# Gradle's IntelliJ tooling needs JDK 17-21; resolve one explicitly so the
# shell's default JDK (possibly newer) can't break the build.

NAMES=(); STATUSES=(); DETAILS=()

announce() { printf "→ %s...\n" "$1"; }

record() { NAMES+=("$1"); STATUSES+=("$2"); DETAILS+=("$3"); }

export JAVA_HOME=${JAVA_HOME_OVERRIDE:-$(/usr/libexec/java_home -v 21 2>/dev/null || /usr/libexec/java_home -v 17 2>/dev/null)}
JDK_DETAIL=$(basename "$(dirname "$(dirname "${JAVA_HOME:-/none/x/y}")")")

PKG_VERSION=$(grep -m1 '^version' build.gradle.kts | sed -E 's/.*"(.*)".*/\1/')
echo "== xwidget-intellij-plugin $PKG_VERSION release check =="

announce "git status"
DIRTY=$(git status --porcelain 2>/dev/null | wc -l | tr -d ' ')
if [ "$DIRTY" = "0" ]; then record "git tree clean" pass "no changes"
else record "git tree clean" fail "$DIRTY dirty/untracked files"; fi

announce "compile + tests"
if [ -z "$JAVA_HOME" ]; then
  record "compile" fail "no JDK 17/21 found"
  record "tests" fail "no JDK 17/21 found"
else
  if OUT=$(./gradlew compileKotlin --no-configuration-cache -q 2>&1); then
    record "compile" pass "kotlin compiles ($JDK_DETAIL)"
  else
    record "compile" fail "$(echo "$OUT" | grep -m1 -iE 'error' | head -c 60)"
  fi
  if OUT=$(./gradlew test --no-configuration-cache -q 2>&1); then
    record "tests" pass "gradle test green"
  else
    DETAIL=$(echo "$OUT" | grep -m1 -iE 'fail|error' | head -c 60)
    [ -z "$DETAIL" ] && DETAIL=$(echo "$OUT" | grep -m1 . | head -c 60)
    record "tests" fail "${DETAIL:-tests produced no output}"
  fi
fi

announce "IDE compatibility"
# Ship targeting the newest IntelliJ line, counting stable and RC (not EAP —
# early builds fluctuate). Prevents the "IDE updated, plugin disabled" scramble.
UNTIL_BUILDS=$(grep -oE 'untilBuild(\.set\(|[ ]*=[ ]*)"[0-9]+' build.gradle.kts | grep -oE '[0-9]+' | sort -u)
UNTIL_COUNT=$(echo "$UNTIL_BUILDS" | wc -l | tr -d ' ')
UNTIL=$(echo "$UNTIL_BUILDS" | tail -1)
LATEST=$(
  { curl -sf --max-time 10 "https://data.services.jetbrains.com/products/releases?code=IIU&latest=true&type=release"; echo;
    curl -sf --max-time 10 "https://data.services.jetbrains.com/products/releases?code=IIU&latest=true&type=rc"; echo; } |
  python3 -c "
import json, sys
best = 0
for line in sys.stdin.read().splitlines():
    if not line.strip(): continue
    try:
        for r in json.loads(line).get('IIU', []):
            best = max(best, int(r['build'].split('.')[0]))
    except Exception: pass
print(best or '')"
)
if [ "$UNTIL_COUNT" != "1" ]; then
  record "IDE compat" fail "untilBuild values disagree in build.gradle.kts: $(echo $UNTIL_BUILDS | tr '\n' ' ')"
elif [ -z "$LATEST" ]; then
  record "IDE compat" pass "targeting $UNTIL.* (JetBrains API unreachable, latest check skipped)"
elif [ "$UNTIL" -ge "$LATEST" ]; then
  record "IDE compat" pass "targeting $UNTIL.*; latest stable/RC line is $LATEST"
else
  record "IDE compat" fail "targeting $UNTIL.* but IntelliJ $LATEST is at stable/RC — raise untilBuild"
fi

announce "change-notes"
# plugin.xml's <change-notes> is a hand-maintained marketplace copy of the
# CHANGELOG — easy to forget when cutting a release.
if grep -q "<h3>$PKG_VERSION</h3>" src/main/resources/META-INF/plugin.xml; then
  record "change-notes" pass "plugin.xml has a $PKG_VERSION section"
else
  record "change-notes" fail "plugin.xml <change-notes> missing <h3>$PKG_VERSION</h3>"
fi

announce "changelog"
CL_HEAD=$(grep -m1 '^### ' CHANGELOG.md | sed -E 's/^### ([0-9.]+).*/\1/')
if [ "$CL_HEAD" = "$PKG_VERSION" ]; then
  ENTRIES=$(awk '/^### /{n++} n==1 && /^\* /{c++} END{print c+0}' CHANGELOG.md)
  if [ "$ENTRIES" -gt 0 ]; then record "changelog" pass "$PKG_VERSION heading with $ENTRIES entries"
  else record "changelog" fail "$PKG_VERSION heading has no entries"; fi
else
  record "changelog" fail "heading '$CL_HEAD' != version '$PKG_VERSION'"
fi

echo ""
FAILED=0; i=0
while [ $i -lt ${#NAMES[@]} ]; do
  if [ "${STATUSES[$i]}" = "pass" ]; then MARK="✓"; else MARK="✗"; FAILED=$((FAILED+1)); fi
  printf " %s %-16s %s\n" "$MARK" "${NAMES[$i]}" "${DETAILS[$i]}"
  i=$((i+1))
done
TOTAL=${#NAMES[@]}
echo "--------------------------------------"
if [ $FAILED -eq 0 ]; then echo "$TOTAL/$TOTAL passed — READY"; exit 0
else echo "$((TOTAL-FAILED))/$TOTAL passed — NOT READY"; exit 1; fi
