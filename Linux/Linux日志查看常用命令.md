## Linux日志查看常用命令

- [Linux日志查看常用命令](#linux日志查看常用命令)
  - [1. cat](#1-cat)
  - [2. tail](#2-tail)
  - [3. grep](#3-grep)

### 1. cat
常用参数 :  
`-n : 由1开始对所有输出的行数编号`  
`-b : 和-n相似, 但不对空白行进行编号`  

常用组合命令示例 : 
```bash
# 查看文件全部内容
cat test.log

# 显示文件中'aaa'的内容, 以及其下3行内容
cat test.log | grep -A 3 'aaa'
# 显示文件中'aaa'的内容, 以及其上3行内容
cat test.log | grep -B 3 'aaa'
# 显示文件中'aaa'的内容, 以及其上下3行内容
cat test.log | grep -C 3 'aaa'

# 显示文件中'aaa'的内容与行号, 以及其上下1行内容
cat -n test.log | grep -B 1 -A 1 'ccc'
# 显示文件中'aaa'的内容, 以及其上下1行内容, 以及显示文件行号(突出匹配行号)
cat test.log | grep -n -B 1 -A 1 'ccc'

# 显示文件内容与行号, 以及从正数3行开始输出, 输出2行内容
cat -n test.log | tail -n +3  | head -n 2
# 显示文件内容与行号, 以及从负数3行开始输出, 输出2行内容
cat -n test.log | tail -n -3  | head -n 2

# 显示文件中'bbb'的内容, 且多页显示(space键下一页, b键上一页)
cat -n test.log | grep 'bbb' | more
```


### 2. tail
常用参数 :  
`-f 实时循环读取`  
`-n 显示文件的尾部n行内容`

常用组合命令示例 : 
```bash
# 显示文件首部10行之后的内容, 且实时更新内容输出
tail -f -n +10 test.log
# 显示文件尾部10行的内容, 且实时更新内容输出
tail -f -n 10 test.log
# 显示文件尾部10行的内容, 且实时更新内容输出
tail -10f test.log
```


### 3. grep
常用参数 :  
`-c 计算符合正则要求的行数`  
`-v 显示不包括匹配文本的所有行`  
`-x 只显示全列符合的行`  
`-E 将样式为延伸的正则表达式来使用` 
`-i 忽略字符大小写的差别` 
`-A <number> 增加显示匹配内容后{:number}行内容` 
`-B <number> 增加显示匹配内容前{:number}行内容` 
`-C <number> 增加显示匹配内容前后各{:number}行内容` 

常用组合命令示例 : 
```bash
# 显示文本中'aa'的匹配内容
grep 'aa' test.log
# 显示文本中不为'aa'的匹配内容
grep -v 'aa' test.log
# 显示文本中'aa'的匹配内容的行数
grep -c 'aa' test.log
# 显示文本中整行为'aa'的内容
grep -x 'aa' test.log

# 显示文本中既带'aa'又带'bb'的匹配内容
grep 'aa' test.log | grep 'bb'
# 显示文本中既带'aa'且不带'bb'的匹配内容
grep 'aa' test.log | grep -v 'bb'

# 显示文本中带'aa'或带'bb'或两者均含有的匹配内容
grep 'aa\|bb' test.log
grep -E 'aa|bb' test.log 

# 显示文本中带'aa'的匹配内容(忽略大小写)
grep -i 'aa' test.log 
# 显示文本中带'aa'的匹配内容(忽略大小写), 及其上下内容各5行
grep -i 'aa' -C 5 test.log 
```


