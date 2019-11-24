<h2> Git配置'SSH key'以及关联远程仓库 </h2>

- [1. 生成’SSh key’](#1-%E7%94%9F%E6%88%90ssh-key)
- [2. 关联远程仓库](#2-%E5%85%B3%E8%81%94%E8%BF%9C%E7%A8%8B%E4%BB%93%E5%BA%93)
  - [1. 将远程仓库直接clone到本地​](#1-%E5%B0%86%E8%BF%9C%E7%A8%8B%E4%BB%93%E5%BA%93%E7%9B%B4%E6%8E%A5clone%E5%88%B0%E6%9C%AC%E5%9C%B0%E2%80%8B)
  - [2. 添加远程仓库​](#2-%E6%B7%BB%E5%8A%A0%E8%BF%9C%E7%A8%8B%E4%BB%93%E5%BA%93%E2%80%8B)
- [3. 进行一次简单的push操作](#3-%E8%BF%9B%E8%A1%8C%E4%B8%80%E6%AC%A1%E7%AE%80%E5%8D%95%E7%9A%84push%E6%93%8D%E4%BD%9C)
<hr>

### 1. 生成’SSh key’

使用'config'添加用户名与用户邮箱:
```shell
git config (--global) user.name "用户名"
git config (--global) user.email "用户邮箱"
```
可以通过两种方式查看'config':
```
1. git config --list
2. 到.git目录中,查看config文件(生成.git目录,需使用'git init'命令将当前目录初始化为Git仓库)
```
``` ps. 取消配置需: git config --unset (--global user.name/email等.. ```

生成与配置'SSH key' : ``` ssh-keygen -t rsa -C "用户邮箱" ``` <br/>
然后处理一些配置即可(使用默认则全回车即可).<br/>
此时,.ssh目录下会有一个公钥(id_rsa.pub)一个私钥(id_rsa),将公钥的内容放到GitHub/GitLab等用户设置的SSH keys中即可.<br/>
<hr>

### 2. 关联远程仓库

以下关联远程仓库的方法,任选一种即可：

#### 1. 将远程仓库直接clone到本地​
```shell
git clone git://xxx/xx.git
```
``` ps. clone的同时,也会把仓库中的文件同时pull到本地. ```

#### 2. 添加远程仓库​

```shell
git init
git remote add origin git://xxx/xx.git
```
``` ps. origin为该远程仓库的名称,可以由用户自定义设置,不过推荐origin ```<br/>
``` ps. 该操作仅将远程仓库相关联,并没有将仓库中的文件pull下来 ```<br/>

查看本地的所有远程仓库 : ``` git remote -v ```
<hr>

### 3. 进行一次简单的push操作

将最新的远程仓库pull下来
```shell
git pull <远程仓库名称> <远程仓库分支名称>
```
新添/修改的文件,并将其add进暂存区
```shell
git add <filename>
```
将add的文件commit到分支上
```shell
git commit -m "<just say something>"
```
将其分支push到远程仓库``` ps. -u选项会指定一个默认主机,若下次'git push'不加参数则指向默认主机 ```
```shell
git push -u <远程仓库名称> <远程仓库分支名称>
```
<hr>
