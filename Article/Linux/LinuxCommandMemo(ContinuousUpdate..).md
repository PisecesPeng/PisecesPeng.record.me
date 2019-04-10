<h2> Linux command memo (continuous update..) </h2>

- [SSH](#ssh)
- [SCP](#scp)
- [TAR](#tar)
- [NC/NETCAT](#ncnetcat)
- [NETSTAT](#netstat)
<hr>

### SSH
**command** : ``` ssh username@remote_host ```
> **desc**. 使用默认端口的ssh连接

**command** : ``` ssh -i ~/key_file username@remote_host ```
> **desc**. 指定密钥文件进行ssh连接

**command** : ``` ssh -L local_port:host:remote_port username@remote_host -p port_num ```
> **desc**. 使用指定端口的ssh连接,并且将远程的端口转发到本地的指定端口(Point.本地转发/远程转发)

> **eg.** 
``` ssh root@1.2.3.4 -p 22333 ```
``` ssh -L 8989:localhost:8888 root@1.2.3.4 -p 22333 ```
<hr>

### SCP
**command** : ``` scp ~/local_file username@remote_host:/remote_dir ```
> **desc**. 使用默认端口,将本地文件拷贝到远程文件夹中

**command** : ``` scp -P 2222 username@remote_host:/remote_file ~/local_dir ```
> **desc**. 使用指定端口,将远程文件拷贝到本地文件夹中

**command** : ``` scp -i ~/key_file -r username@remote_host:/remote_dir ~/local_dir ```
> **desc**. 指定密钥文件,将远程文件夹拷贝到本地文件夹中

> **eg.** 
``` scp -P 23233 /demo/a.log root@1.2.3.4:/tmp ```
<hr>

### TAR
**command** : ``` tar -cvf[z][j] tarname.tar[.gz][.bz2] ~/filepath ~/dirpath ```
> **desc**. 将多个文件或目录压缩,以tar格式压缩,[z]以tar.gz格式压缩,[j]以tar.bz2格式压缩

**command** : ``` tar -xvf[z][j] tarname.tar[.gz][.bz2] -C ~/filepath ```
> **desc**. 将压缩包解压,默认在当前目录解压,-C解压至指定目录

**command** : ``` tar -tvf tarname.tar[.gz][.bz2] ```
> **desc**. 列出该压缩包里的所有文件

**command** : ``` tar -xvf[z][j] tarname.tar[.gz][.bz2] filename dirname [--wildcards '*.*'] ```
> **desc**. 解压出压缩包中的指定文件/目录,[--wildcards]支持正则表达式匹配

**command** : ``` tar -rvf tarname.tar filename dirname ```
> **desc**. 追加文件/目录进压缩包中(仅限定tar格式)

> **eg.** 
``` tar -zcvf log.tar.gz a.log b.log ```
``` tar -zxvf demo.tar.gz -C /home/demo --wildcards '*.log' ```
<hr>

### NC/NETCAT
**command** : target ``` nc -lp local_port > filename ```
**command** : source ``` nc -w num target_host target_port < filename ```
> **desc**. 从source端到target端实现文件拷贝(target端需先激活监听)
> **desc**. [-l]使用监听模式，监控传入的资料,[-p]设置本地主机使用的通信端口,[-w]设置等待连线的时间

**command** : ``` nc [-u] -v -w num remote_host -z port_start_num[-port_end_num] ```
> **desc**. 进行端口扫描
> **desc**. [-v]显示指令执行过程,[-z]使用0输入/输出模式(只在扫描通信端口时使用),[-u]使用UDP传输协议

**command** : target  ``` nc -lp local_port ```
**command** : source ``` nc remote_host remote_port ```
> **desc**. 实现聊天功能(ctrl+d退出)

> **eg.** 
target ``` nc -lp 12345 > source.log ```、source ``` nc -w 1 1.2.3.4 12345 < source.log ``` 
``` nc -v -w 1 1.2.3.4 -z 1-10000 ```
<hr>

### NETSTAT
**command** : ``` netstat -a[t][u][x] ```
> **desc**. 列出所有的Socket(包括监听和未监听的),[t]TCP端口,[u]UDP端口,[x]UNIX端口.

**command** : ``` netstat -l[t][u][x] ```
> **desc**. 列出所有处于监听状态的Socket,[t]TCP端口,[u]UDP端口,[x]UNIX端口.

**command** : ``` netstat -p ```
> **desc**. 显示输出PID和进程名称.

**command** : ``` netstat -n[r] ```
> **desc**. 使用数字代替主机、端口和用户名等名称,且不用进行比对查询可加速输出,[r]显示核心路由信息.

**command** : ``` netstat -i[e] ```
> **desc**. 显示网络接口列表,[e]显示详细信息(类似ifconfig回显).

<hr>