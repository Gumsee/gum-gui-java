#!/bin/sh

mvn exec:java -pl example -Dexec.mainClass=com.gumse.Example -Dexec.args="$*"
