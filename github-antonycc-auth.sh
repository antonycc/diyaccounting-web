#!/usr/bin/env bash
# Purpose: Configure the SSH agent to only recognise keys for antonycc@github
# Usage: ./github-antonycc-auth.sh
ssh-add -D
ssh-add ~/.ssh/id_antony_pers_mbp_2222
ssh -T git@github.com
