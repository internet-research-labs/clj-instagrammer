#!/bin/sh

IG_CLIENT_ID="597d57d253d446a89bda86c03b129326"
IG_CLIENT_SECRET="2558af42f56b4514afa374d2ef0684c0"

# curl -X DELETE "https://api.instagram.com/v1/subscriptions?client_secret=${IG_CLIENT_SECRET}&object=all&client_id=${IG_CLIENT_ID}"
curl "https://api.instagram.com/v1/locations/$1?client_secret=${IG_CLIENT_SECRET}&client_id=${IG_CLIENT_ID}"
