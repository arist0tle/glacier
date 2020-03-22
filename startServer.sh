#!/bin/sh

cd `dirname $0`
nohup java -jar glacier-server/target/glacier-server-1.0.jar 2>&1 &