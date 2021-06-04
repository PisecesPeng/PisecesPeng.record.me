## Git配置'SSH KEY'以及关联远程仓库

- [Git配置'SSH KEY'以及关联远程仓库](#git配置ssh-key以及关联远程仓库)
  - [1. 生成’SSh key’](#1-生成ssh-key)
  - [2. 关联远程仓库](#2-关联远程仓库)
    - [1. 将远程仓库直接clone到本地](#1-将远程仓库直接clone到本地)
    - [2. 添加远程仓库](#2-添加远程仓库)
  - [3. 进行一次简单的push操作](#3-进行一次简单的push操作)

### 1. 生成’SSh key’

使用'config'添加用户名与用户邮箱:
```bash
git config (--global) user.name "用户名"
git config (--global) user.email "用户邮箱"
```

可以通过两种方式查看'config':
```
1. git config --list
2. 到.git目录中,查看config文件(生成.git目录,需使用'git init'命令将当前目录初始化为Git仓库)
```

`ps. 取消配置需: git config --unset (--global user.name/email等..)`

生成与配置'SSH key' : `ssh-keygen -t rsa -C "用户邮箱"`  
然后处理一些配置即可(使用默认则全回车即可).  
此时,.ssh目录下会有一个公钥(id_rsa.pub)一个私钥(id_rsa),将公钥的内容放到GitHub/GitLab等用户设置的SSH keys中即可.  

---

### 2. 关联远程仓库

以下关联远程仓库的方法,任选一种即可：

#### 1. 将远程仓库直接clone到本地

```bash
git clone git://xxx/xx.git
```

`ps. clone的同时,也会把仓库中的文件同时pull到本地.`

#### 2. 添加远程仓库

```bash
git init
git remote add origin git://xxx/xx.git
```

`ps. origin为该远程仓库的名称,可以由用户自定义设置,不过推荐origin`
`ps. 该操作仅将远程仓库相关联,并没有将仓库中的文件pull下来`


查看本地的所有远程仓库 : `git remote -v`

---

### 3. 进行一次简单的push操作

将最新的远程仓库pull下来
```bash
git pull <远程仓库名称> <远程仓库分支名称>
```

新添/修改的文件,并将其add进暂存区
```bash
git add <filename>
```

将add的文件commit到分支上
```bash
git commit -m "<just say something>"
```

将其分支push到远程仓库`ps. -u选项会指定一个默认主机,若下次'git push'不加参数则指向默认主机`
```bash
git push -u <远程仓库名称> <远程仓库分支名称>
```

