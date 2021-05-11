# JX3Mirai
JX3Mirai is a JX3 Bot based on [mirai](https://github.com/mamoe/mirai)

[开发日志](DevelopmentLog.md)

## Before Use
1. 请仔细阅读Mirai官方给出的 [设备信息](https://github.com/mamoe/mirai/blob/dev/docs/Bots.md#%E8%AE%BE%E5%A4%87%E4%BF%A1%E6%81%AF) 和 [处理滑块验证码](https://github.com/mamoe/mirai/blob/dev/docs/Bots.md#%E5%A4%84%E7%90%86%E6%BB%91%E5%8A%A8%E9%AA%8C%E8%AF%81%E7%A0%81) 解决方案，并获取 device.json
   （如果实现起来有困难，可以参考 [用户手册](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md) 使用mirai-console获取）
2. 将 [BaseProperties.bak](src/main/java/top/yuany3721/JX3Mirai/util/BaseProperties.bak) 更名为BaseProperties.java，并将 device.json 中的信息填入 DeviceInfo 中

~~我才不会说自己看了两天mirai-login-solver-selenium完全弄不懂最后用mirai-console获取的~~


## Usage
```shell
mvn install
```
程序入口在 Application

## Package
```shell
mvn clean
mvn package
```

