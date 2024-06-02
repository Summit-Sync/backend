#!/bin/bash

# Path to your service account file
SERVICE_ACCOUNT_FILE="./src/main/resources/service-account.json"

# The scope for the Google API you're using
SCOPE="https://www.googleapis.com/auth/cloud-platform https://www.googleapis.com/auth/calendar"

# Extract the private key, client email, and token_uri from the service account file
PRIVATE_KEY=$(jq -r '.private_key' $SERVICE_ACCOUNT_FILE)
CLIENT_EMAIL=$(jq -r '.client_email' $SERVICE_ACCOUNT_FILE)
TOKEN_URI=$(jq -r '.token_uri' $SERVICE_ACCOUNT_FILE)

# Create a JWT and sign it with the private key
JWT_HEADER=$(echo -n '{"alg":"RS256","typ":"JWT"}' | openssl base64 -e | tr -d '=' | tr '/+' '_-' | tr -d '\n')
JWT_CLAIM_SET=$(echo -n "{\"iss\":\"$CLIENT_EMAIL\",\"scope\":\"$SCOPE\",\"aud\":\"$TOKEN_URI\",\"exp\":$((`date +%s` + 3600)),\"iat\":`date +%s`}" | openssl base64 -e | tr -d '=' | tr '/+' '_-' | tr -d '\n')
JWT_SIGNATURE=$(echo -n "$JWT_HEADER.$JWT_CLAIM_SET" | openssl dgst -sha256 -sign <(echo -n "$PRIVATE_KEY") | openssl base64 -e | tr -d '=' | tr '/+' '_-' | tr -d '\n')
JWT="$JWT_HEADER.$JWT_CLAIM_SET.$JWT_SIGNATURE"

# Request the access token
ACCESS_TOKEN=$(curl -s -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=$JWT" $TOKEN_URI | jq -r '.access_token')

# Print the access token
echo $ACCESS_TOKEN
