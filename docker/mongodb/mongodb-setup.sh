#!/usr/bin/env bash

echo 'Creating application user and db'

mongo ${MONGODB_DATABASE} \
      --host ${MONGODB_CONNECTION_URL} \
      --authenticationDatabase admin \
      --eval "db.createUser({user: '${MONGODB_USER}', pwd: '${MONGODB_PASSWORD}', roles:[{role: 'dbOwner', db: '${MONGODB_DATABASE}'}]});"
