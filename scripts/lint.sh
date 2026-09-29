#!/usr/bin/env bash
# Test payload for the fork PR approval gate. Harmless: prints only.
echo "::warning::UNTRUSTED FORK CODE RAN"
echo "event=$GITHUB_EVENT_NAME actor=$GITHUB_ACTOR head=$GITHUB_HEAD_REF"
