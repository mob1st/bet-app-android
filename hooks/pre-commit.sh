#!/bin/bash

# Stash the current index to be used in the event of failure
git stash -q --keep-index

echo "Running Ktlint before git commit is committed"

./gradlew ktlintFormat

RESULT=$?

git stash pop -q

# return 1 exit code if running checks fails
[ $RESULT -ne 0 ] && exit 1

######## KTLINT-GRADLE HOOK START ########

CHANGED_FILES="$(git --no-pager diff --name-status --no-color --cached | awk '$1 != "D" && $2 ~ /\.kts|\.kt/ { print $2}')"

if [ -z "$CHANGED_FILES" ]; then
    echo "No Kotlin staged files."
    exit 0
fi;

echo "Running ktlint over these files:"
echo "$CHANGED_FILES"

./gradlew --quiet ktlintFormat -PinternalKtlintGitFilter="$CHANGED_FILES"

echo "Completed ktlint run."
echo "$CHANGED_FILES" | while read -r file; do
    if [ -f $file ]; then
        git add $file
    fi
done
echo "Completed ktlint hook."
######## KTLINT-GRADLE HOOK END ########
