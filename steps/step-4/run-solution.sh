#!/usr/bin/env sh

set -e -x

mvn clean package -Psolution
java -jar target/step-4.jar -Dvertxweb.environment=dev
