#!/bin/sh

mvn package
mvn install

ln -s /usr/local/lib64/libGumSystem-shared.so example/target/classes/
ln -s /usr/local/lib64/libGumGLFW-shared.so example/target/classes/
ln -s /usr/local/lib64/libGumGUI-shared.so example/target/classes/
