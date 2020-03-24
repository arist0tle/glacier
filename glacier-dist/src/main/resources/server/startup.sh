#!/bin/sh

cd `dirname $0`
nohup java -jar ../lib/glacier-server*.jar 2>&1 &
