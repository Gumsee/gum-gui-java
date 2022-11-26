#!/bin/sh

EXAMPLE_NAME=$1
shift

#mvn exec:java -pl example -Dexec.mainClass=com.gumse.$EXAMPLE_NAME -Dexec.args="$*"
java -XstartOnFirstThread -jar ./example/target/example-1.0.0.jar
