# blockj

> Java 实现的一个简易区块链（联盟链）项目，包括加密工具，钱包，P2P 传输，区块同步，网络共识等基础实现。
> 它使用 SpringBoot + Tio 网络框架实现，是一个非常好的区块链学习项目，目前只实现了 POW 共识算法，如果要用于生产项目需要根据自己的项目需求修改共识。

-----------------------------------------------

## 项目架构

主程序使用 SpringBoot 实现， P2P 传输这块使用的是 [t-io 网络框架](https://github.com/tywo45/t-io)。

**运行环境为 JDK1.8 以上版本。**

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

### 客户端使用

1. 查看钱包列表
    ```shell
    ./client wallet list
    # 输出
   Address                                      Balance        Nonce

   0x0d181310331612e107b5e0dfdf971cfb9de780bb   800               1
   0x2505bf54f3a63848e44a105e8de599ad08ae8c58   2400              0
   0xb6258bc70240ee5daa213f671c08db51e50a4cbe   50800             3
   0xcd3da3ec4195070e13a6d88b75101f6ceb427a8e   0                 0
   ```

2. 创建新钱包
   ```shell
   # 创建普通钱包
   ./client wallet new
   0xb640636a77381b6589c78d58d629221131946dc
   # 创建带助记词的钱包，可设置密码，生成 keystore 文件
   ./client wallet new --mnemonic=true 123456
   
   Mnemonic words: rain fog canal matrix tonight initial frog wear feel movie worry whisper
   Address: 0x42a8037f2876f649e08f7be6764b810e9a2f21da
   ```
3. 查询钱包余额

   ```shell
   ./client wallet balance 0x2505bf54f3a63848e44a105e8de599ad08ae8c58
   # 输出
   Address                                      Balance
   0x2505bf54f3a63848e44a105e8de599ad08ae8c58   2400
   ```

4. 转账
   ```shell
   ./client chain send --from=0x0d181310331612e107b5e0dfdf971cfb9de780bb 0x2505bf54f3a63848e44a105e8de599ad08ae8c58 123
   # 输出
   Send message, CID: 05b6074241f1406cd1a68731d74cf612f55981692a3f4e5d9da01b13b4ee3631
   ```

5. 查看当前链高度

   ```shell
   ./client chain head
   Chain head: 1217
   ```

6. 查看指定的链上消息

   ```shell
   ./client chain getMessage 05b6074241f1406cd1a68731d74cf612f55981692a3f4e5d9da01b13b4ee3631
   # 输出
   Message{version=1, from='0x0d181310331612e107b5e0dfdf971cfb9de780bb', to='0x2505bf54f3a63848e44a105e8de599ad08ae8c58', value=123, timestamp=1672826743640, pubKey='PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCw5EXe33S25zZDT25sNYu1bjtBfaCwEGSgnGhJiE31fCfDsyE3pNFw7cC87VfQZQqiEdntMmztfpiDcRe1gv3aJJ4', cid='05b6074241f1406cd1a68731d74cf612f55981692a3f4e5d9da01b13b4ee3631', status=APPENDING, nonce=2, params='null', height=0, sign='30460221009A8B2750A6D986EB926B67163B740BBACDD07FE2D87F8FA9AE2F08424989477602210082C1C36EAEEC6367C023847F995291873F305B867E9B65A5C68ED8A4293DB890'}
   ```

7. 查看指定高度的区块信息

   ```shell
   ./client chain getBlock 1
   # 输出
   Block{header=BlockHeader{height=1, difficulty=28269553036454149273332760011886696253239742350009903329945699220681916416, nonce=703, createTime=1672813674, hash='0004c262f7ead28cc66c9336d7a8335cb8fea5a06b0b1fd7488c3c9b140987cc', previousHash='ed5126ddd65f39a17739b8e26ea3edecfff6bf196148dc259d9a3eddeefc23d5'}, messages=[Message{version=0, from='B099', to='0xb6258bc70240ee5daa213f671c08db51e50a4cbe', value=50, timestamp=1672813674163, pubKey='PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSD1ErKp8AqSj4Ph9Jsj2Gvk7w1pyLgqDRiguC7JvjeGZJZ1si1qRYCAsVmu1UvYRqvhiCBgYDpmyuWK5VzD5KK4RNY', cid='3e1f8987b0d66b2de155e78aeef6984ab7cb6c3acdf03c835c01f4b4088fb90d', status=SUCCESS, nonce=0, params='Miner Reward.', height=1, sign='304402202DF6EABBF5C81C41996C44F8E1230D44CAE8ADA9184B466D9B708ADC8B050225022049D74E4E99E0EA8E56208AD2E4B7B17C2320DC3E6461A17C94D1820818559CA9'}], blockSign='3046022100C04714C00642527AF6AA1DB2B537E5FD887F52999F66929AA2A928D4C6A4897A022100AF98888FDC825FFB0683E8D65494363790E5173F3991AF61AB86DEEFEAF15D81'}
   ```

8. 查看当前网络中 P2P 节点列表

   ```shell
   ./client net peers
   ```

9. 查看当前节点 P2P 连接信息

   ```shell
   ./client net listen
   # P2P 连接信息，用于被其他节点连接
   127.0.0.1:2345
   ```

10. 手动连接某个节点
   ```shell
   ./client net connect 192.22.33.11:3456
   ```

## TODOLIST

- [x] blockj-miner api 实现
- [x] 消息同步和打包
- [x] blockj-client(客户端)功能实现：网络，钱包，转账
- [x] 文档完善
- [ ] 添加 PBFT 共识支持
