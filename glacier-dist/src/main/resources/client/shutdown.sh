#!/bin/sh

ps -ef |grep glacier|grep client|grep jar|awk '{print $2}'|xargs -t -i kill -9 {}
echo "shutdown glacier"