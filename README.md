# blockj

> Java 实现的一个简易区块链（联盟链）项目，包括加密工具，钱包，P2P 传输，区块同步，网络共识等基础实现。
> 它使用 SpringBoot + Tio 网络框架实现，是一个非常好的区块链学习项目，目前只实现了 POW 共识算法，如果要用于生产项目需要根据自己的项目需求修改共识。

-----------------------------------------------

## 项目架构

主程序使用 SpringBoot 实现， P2P 传输这块使用的是 [t-io 网络框架](https://github.com/tywo45/t-io)。

## 项目模块
* blockj-base 基础公共的工具包，如加密，区块，消息等数据模型，数据存储等。
* blockj-miner 区块链主程序，如矿工，区块同步，P2P 网络，RPC API 等。
* blockj-client 客户端命令行工具，主要就是调用 Miner 的相关 API，用户跟链交互。
  
## 快速开始

创建一条链的操作流程如下：

1. 创建创世节点（创建一个网络）
2. 启动创世节点（Genesis Miner）
3. 其他节点要加入网络的话，只需要以创世区块初始化 Miner，然后再启动 Miner 即可。

### 创建创世节点

首先我们需要编译打包程序：

```bash
git clone https://gitee.com/blackfox/blockj.git
cd blockj
mvn clean package
```

然后创建创世节点：

```bash
./miner genesis --repo=/data/genesis --enable-mining=true
```

启动创世 Miner：

```bash
./miner run --repo=/data/genesis 
```

### 启动新 Miner

首先需要初始化 miner，需要导入创世区块（genesis.car）来加入网络：

```bash
./miner init --repo=/data/miner1 --genesis=genesis.car --api.port=8002 --p2p.port=3456
```


启动 Miner 

```bash
./miner run --repo=/data/miner1
```

## TODOLIST
- [ ] blockj-miner api 实现
- [ ] 消息同步和打包
- [ ] blockj-client(客户端)功能实现：网络，钱包，转账
- [ ] 文档完善
- [ ] 添加 PBFT 共识支持
