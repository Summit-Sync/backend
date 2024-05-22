#!/bin/bash
printf '%s' "$(xh -f 'https://keycloak.summitsync.meschter.me/realms/summit-sync/protocol/openid-connect/token' \
grant_type=password \
client_id=summit-sync-bff \
client_secret=Wnhhsz4xp4k3PXUjrIyCSqhCLLHcjux1 \
username=test_admin password=test scope='openid profile email' | jq)"
