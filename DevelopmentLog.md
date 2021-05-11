# 开发日志
## demo v0.1
### 2021年5月11日
- 移除无用的Pair工具类
---
- 新增禁言管理、禁言时长缓存
- 修复缓存Buffer找不到缓存文件时缓存失效的问题
---
- @Function注解新增visible、needAdmin、needBotAdmin属性，现在只有visible=true(default)的功能才会被放入功能列表
- 新增权限管理，可设置是否需要管理员操作、bot是否需要管理员权限
---
- 修改function包下父类及接口类类名，使之更符合实际功能
```shell
# function接口
BaseFunction --> FunctionInterface
# 验证群功能是否执行的验证器
FunctionSwitch --> GroupFunctionValidator
```
- function包内新增子包basic，用于存放功能抽象类及接口类
- 新增BaseProperties -- botAlias属性
### 2021年5月8日
- 建立仓库 [JX3Mirai](https://github.com/yuany3721/JX3Mirai/) ，并发布demo v0.1 Pre Release
---
- 建立缓存buffer包和@Buf注解，提取Buffer父类
---
- 完善JavaDoc
### 2021年5月7日
- 新增疯狂的复读机功能
---
- 新增群聊功能说明
- 新增群聊功能开关
---
- 修复无法识别不同图片区别的bug
- 修复无法复读含有多图片消息的bug
---
- 新增群聊消息缓存功能
- 修复复读机不同群聊消息会相互混淆的bug
- 复读机现在可以复读富文本消息了（如图片、引用、@等
---
- 新增群临时会话、陌生人消息回复
### 2021年5月6日
- 修改Listener注册方式
- 新建开发日志