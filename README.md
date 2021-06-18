# minesweeper-server
[![Java CI with Maven](https://github.com/I-Info/minesweeper-server/actions/workflows/maven.yml/badge.svg)](https://github.com/I-Info/minesweeper-server/actions/workflows/maven.yml)      
在线扫雷大战的后端, 本项目采用 maven 构建

## 如何运行项目

在项目根目录下执行以下命令

```bash
chmod +x ./run.sh
./run.sh
```

## 项目说明

1. 推荐调试工具：[SSokit](https://github.com/rangaofei/SSokit-qmake)
2. 相关协议 格式是 SSokit 测试用的格式
```
用户注册
[03 0A 05 02]abc[05 04]

用户邀请
[03 0C 05 02]邀请者名字[05 02]被邀请者名字[05 04]

用户接受邀请
[03 0D 05 01 01 05 02]邀请者名字[05 02]被邀请者名字[05 04]

用户拒绝邀请
[03 0D 05 01 02 05 02]邀请者名字[05 02]被邀请者名字[05 04]

用户同步数据
[03 0E 05 01 01 05 01 01 05 01 01 05 01 01 05 01 01 05 01 01 05 04]

```
3. 协议模版
```
[03 操作指令码 05][01 整数参数 05] ··· [02]字符串参数[05 04]
```

## 项目规范
1. 每个 catch 语句在捕捉到错误后，如果这个地方的错误输出不会影响程序的使用效果，将错误的**具体**原因输出，方便排查错误
2. 本后端采用了类似 HTTP 的`请求-响应`通信方式，每个请求状态码绑定一个 handler，在 handler 中编写业务逻辑
