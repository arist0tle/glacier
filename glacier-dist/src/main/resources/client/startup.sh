#!/bin/sh

cd `dirname $0`
nohup java -jar ../lib/glacier-client*.jar 2>&1 &
