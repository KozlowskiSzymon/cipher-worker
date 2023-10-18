#!/bin/bash
VERSION=0.2.1
docker build -t szymon2kozlowski/cypher-worker:$VERSION .
docker push szymon2kozlowski/cypher-worker:$VERSION