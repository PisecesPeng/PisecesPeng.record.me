#!/bin/bash
set -e

# 取得当前目录路径
CURDIR=`pwd`

# find --help 查看参数作用
# '\;'为结束一行命令
# 'sh -c'将一个字符串作为完整命令来执行
find . -name "pom.xml" -exec sh -c 'mvn clean package -f {}' \;

# 多个jar包后台运行
cd ${CURDIR}/Demo1/target
nohup java -jar *.jar >> ${CURDIR}/run.log 2>&1 &
cd ${CURDIR}/Demo2/target
nohup java -jar *.jar >> ${CURDIR}/run.log 2>&1 &

