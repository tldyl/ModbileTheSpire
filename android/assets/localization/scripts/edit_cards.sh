#!/usr/bin/env bash
set -xe
for JSON_FILE in **/cards.json; do
  #JSON_FILE="eng/cards.json"
  tmp=$(mktemp)
  new_desc=$(jq --raw-output '.Bloodletting.DESCRIPTION' "$JSON_FILE" | sed 's/\[R\]/\[R\] \[R\]/g')
  jq --arg new_desc "$new_desc" '.Bloodletting.DESCRIPTION = $new_desc' "$JSON_FILE" > "$tmp"
  if [[ "$JSON_FILE" != *"www"* ]]; then
  mv "$tmp" "$JSON_FILE"
  fi
done
