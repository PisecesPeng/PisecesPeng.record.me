<h2> Maven踩坑备忘 </h2>

1. jar目录下的'_remote.repositories'需要注意'jar'or'pom'的'镜像id'是否正确 ?
``` 
PIPE.RELEASE.jar>central=
PIPE.RELEASE.pom>central=
```

2. jar目录下的'*.lastUpdated'需要注意'.lastUpdated'的值(时间戳)是否为最近 ?
``` 
https\://*.lastUpdated=1234567890000
```