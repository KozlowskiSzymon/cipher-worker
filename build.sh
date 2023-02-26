#!/bin/bash
VERSION=0.0.6-SNAPSHOT
docker build -t szymon2kozlowski/cypher-worker:$VERSION .
docker push szymon2kozlowski/cypher-worker:$VERSION