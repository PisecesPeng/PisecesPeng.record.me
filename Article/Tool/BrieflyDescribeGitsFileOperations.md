<h2> Git各区之间的文件操作 </h2>

- [git 'work区'操作](#git-work区操作)
- [git 'stage(index)区'操作](#git-stageindex区操作)
- [git 'history区'操作](#git-history区操作)

![./res/GitFileOperation/GitOp.jpg](./res/GitFileOperation/GitOp.jpg)
<hr>

### git 'work区'操作

> ‘工作区’从上图去理解，就是我们平时work的环境

'工作区'的撤销修改 : 
```shell
-- 若是撤销对某一文件的修改(修改/删除等都可)
-- 需要有两个前提:
--  1.文件未添加到stage/index.
--  2.文件已存在repository中，因为'撤销修改'相当于'版本回退'，所以需之前已被'tracked'.
git checkout -- <文件名>
```
``` ps. 添加文件'.gitignore'可配置需要默认忽略add的文件 ```
<hr>

### git 'stage(index)区'操作

> add可以把文件存在stage(index)，这时候的文件相当于放入了'内存'一样，可以简单的添加/移除

'stage区'常用'添加'操作 : 
```shell
-- 查看项目的status(包含新建、删除、修改等文件的状态)
git status

-- add'添加'所有文件(包括新文件、修改文件、删除文件)
git add .
git add -A

-- add'添加'修改文件与删除文件
git add -u

-- add'添加'新文件与修改文件
git add --ignore-removeal

-- add'添加'单个文件
git add <文件路径/.../文件名>

-- add可以加入一些'正则'匹配需要'添加'的文件，示例:
git add *.java
git add Test?java
```

'stage区'常用'移除'操作(注意！仅针对未commit操作的文件，即还在stage/index的文件) : 
```shell
-- add'移除'全部add'添加'的文件
git reset -q

-- add'移除'已经add'添加'的文件，且状态不变(ps. HEAD只是参照,可以根据实际情况改变)
git reset HEAD <文件名/目录路径>

-- add'移除'已经add'添加'的文件，且将其状态变为'untracked'(ps.不推荐已在repository中存在的文件进行此操作)
git rm --cached <文件名/目录路径>
```
<hr>

### git 'history区'操作

> 已被commit的文件区域,不知道叫’history区’算不算很妥帖..,


'history区'常用'提交'操作 : 
```shell
git commit -m '<just say something>'
```

'history区'常用'回退'操作 : 
```shell
-- 查看log记录(reflog可以查看所有操作记录，包括commit hash、commit记录、reset记录等)
git reflog

-- 仅取消commit操作
git reset --soft <commit hash>
-- ps. 操作后，所有文件都将回退到'stage/index'中，即已被add'添加'的状态

-- 取消commit、add操作
git reset --mixed <commit hash>
-- ps. 注意，--mixed为reset默认操作，即在git reset后不加任何参数时，即为git reset --mixed
-- ps. 操作后，所有文件将回退到'工作区'，即未被add'添加'的状态

-- 取消commit、add、源文件修改操作
git reset --hard <commit hash>
-- ps. 注意！该操作会影响到源文件，相当于整个项目回退到指定的版本(commit hash那个版本)
```
<hr>

