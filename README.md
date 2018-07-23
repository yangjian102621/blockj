# blockchain-java

> 区块链钱包 Java 的简易实现版，自学了一段时间的区块链理论知识，自己尝试着写一个具有钱包雏形的区块链项目，开源出来，
供像我一样想开发区块链又不知从何下手初学者参考。目前大部分区块链从业者都是从事 DApp 开发，做底层链开发的还是比较少，目前也没有很大必要，
不过尝试写链可以更好的理解区块链底层的运行逻辑。本人在开发的过程中最大的体会是，在理论学习的时候觉得区块链知识并不复杂，
理论自己都懂了，但是真要自己实现了就会发现有各种坑，最后才发现自己对某些细节理解还是一知半解。
希望这个项目能对初学区块链者有所帮助，大家一起学习，一起成长。

### Version 1.1
1. 将项目名称修改为 ppblock
2. 重构钱包生成算法，修改了签名算法，新增通过助记词创建钱包, 新增生成 keystore file 钱包文件功能
3. 支持通过私钥字符串， 助记词，助记词+密码，keystore + 密码等多种方式恢复钱包(此处可以点赞)
4. 修改了交易(Transaction) 和账户（Account)实体的数据结构, 职责更加明确，Account 不在存储公钥等数据，只存储地址和余额。
5. 鉴于网上很多同学都吐槽说项目启动的时候一直不断的抛出异常，其实这是系统启动之后自动同步其他节点的最新区块数据导致的,因为 tio 
配置的初始同步节点都没有启动，所以一直抛出连接超时的异常，其实这并不影响系统运行，但是为了方便那些有强迫症的同学（本人也是强迫症患者）, 增加了　node-discover 配置，可以手动关闭　p2p　网络同步，作为单节点测试运行。
6. 添加发送交易后自动挖矿功能，可配置成手动挖矿，默认是自动挖矿，如果初学者建议配置手动挖矿，这样你能更清楚的了解整个区块链交易工作的流程
7. 修复了一些已知的　Bug...

### 下个版本开发计划
完成钱包的 UI 界面开发， 不再使用 postman 工具来测试，做到开箱即用.


### Version 1.0
1. 初步了完成的区块链的各个模块，包括账户，区块链，网络等模块实现
2. 实现了账户创建，发送交易和挖矿功能
3. 实现了网络功能，包括发送广播账户，广播区块，广播交易以及自动同步区块功能。

## 单节点部署
__这个很简单, 直接像运行普通的 SpringBoot 项目一样运行就好了，单节点默认使用的 node1 节点的配置__

## 多节点部署
项目默认部署3个节点，创建了3个配置文件 application-{env}.yml， 
如果想要部署更多的节点，再创建更多的配置文件就 OK 了。

使用 idea 部署测试非常简单，按照下面的方法添加多个 springBoot 启动配置。

![](install.png)

然后分别启动 3 个节点就好了。启动之后节点之间自动连接成 P2P 网络，随后你就可以使用 postman 工具进行测试了，如果没有安装 postman 的话请自行安装，或者和我一样使用 chrome 浏览器的 postman 扩展。

## Web 测试 API

API名称 | 请求方式 | URL 
--------|---------|------
生成钱包 | POST | /account/new
查看钱包列表 | GET | /account/list
启动挖矿 | GET | /chain/mine
发送交易 | POST | /chain/transactions/new
查看最后一个区块 | GET | /chain/block/view
添加节点 | POST | /chain/node/add
查看节点 | GET | /chain/node/view

> 注意：凡是 POST 请求都是使用 RequestBody 的方式传参的， 不是用表单的 form-data 形式， 比如发送交易的参数形式如下：

```
{
    "name" : "value",
    "name2" : "value2"
}
```
## 简单测试
首先依次启动 node1 - node3 3个节点，由于在启动的时候会自动链接初始化的节点，各自连接成为一个 P2P 的网络，所以被链接的节点没有启动的时候会抛出网络异常，不用管它，等其他节点启动好了之后又会自动连接上的。

> 创建钱包账户 http://127.0.0.1:8081/account/new

__响应如下：__

```
{
    "code": 200,
    "message": "New account created, please remember your Address and Private Key.",
    "item": {
        "address": "0xd7dd662e41c66ae31e493ca719a1d1e04a8174fe",
        "balance": 0,
        "privateKey": "aaaf41772d195fd61a236871a0e100589efaceee9f33c12491cb37e99b9a165d"
    }
}
```

> 挖矿， 挖矿之前要先创建挖矿钱包，操作跟上面相同 <br/>
http://127.0.0.1:8081/chain/mine

__响应如下：__

``` 
{
    "code": 200,
    "message": "Create a new block",
    "item": {
        "header": {
            "index": 8,
            "difficulty": 2.8269553036454149273332760011886696253239742350009903329945699220681916416e+73,
            "nonce": 9302,
            "timestamp": 1531739951847,
            "hash": "0002d041d584afb3609bfeb58a1eb25bef5540154f6b672522ce6e455c08c75b",
            "previousHash": "000c3738e3819bb52fc254e185eaae00dd39086a8bc2837cb4faf06d6edc51d6"
        },
        "body": {
            "transactions": [
                {
                    "from": null,
                    "sign": null,
                    "to": "0x69dc11cae775647d495b2c8930a17b31827b286b",
                    "publicKey": null,
                    "amount": 50,
                    "timestamp": 1531739951847,
                    "txHash": "0x472f84eb7488d4b72a5dc4c6b40b496dfa2b281c655fd2d4d1fefbd047b7fbda",
                    "status": "SUCCESS",
                    "errorMessage": null,
                    "data": "Miner Reward."
                }
            ]
        }
    }
}
```

> 发送交易， http://127.0.0.1:8081/chain/transactions/new
 
 请求方式为 POST， 参数如下
````
{
	"from":"0x69dc11cae775647d495b2c8930a17b31827b286b",
	"to":"0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
	"amount":5.5,
	"privateKey":"69c4caa1495e678208f1ee60f578a63ce5d0a6780541877454545a722175d760",
	"data":"hello world"
}
````

__响应如下：__

```
{
    "code": 200,
    "message": "SUCCESS",
    "item": {
        "from": "0x69dc11cae775647d495b2c8930a17b31827b286b",
        "sign": "3045022100AE0606BACCDAFCBA8B84ED27B58B5F0239F243DEAFF46617E56864A6D8A677E702204DE4EBAC8213225D68D6395FD54602FCF24CD71D96E82F21DBEF77CADC43F70F",
        "to": "0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
        "publicKey": "PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCz7FbyM3mvg3favYhhHXarHN6hXgYwKtvLAfXM5YgLDnZx1YPoo4G9pdiR5RQrhtBYriMCh5mGC3RC93HLFkBnAgi",
        "amount": 5.5,
        "timestamp": 1531739924777,
        "txHash": "0x74e5e59d69604b4081c81dae66f429febea9abb77ed6cd7f5b33e6da8ae667f9",
        "status": "APPENDING",
        "errorMessage": null,
        "data": "hello world"
    }
}
```

> 查看账户列表， http://127.0.0.1:8081/account/list

__响应如下：__

``` 
{
    "code": 200,
    "message": "SUCCESS",
    "item": [
        {
            "address": "0x230ee512f454c4cb90e54b730d52a73e726b6db1",
            "balance": 0
        },
        {
            "address": "0x69dc11cae775647d495b2c8930a17b31827b286b",
            "balance": 269
        },
        {
            "address": "0x800c9be60dcec723525432338944dc6a7a8a9bc4",
            "balance": 0
        },
        {
            "address": "0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
            "balance": 5.5
        },
        {
            "address": "0xd7dd662e41c66ae31e493ca719a1d1e04a8174fe",
            "balance": 0
        }
    ]
}
```

> 查看区块， http://127.0.0.1:8081/chain/block/view

__响应如下：__

``` 
{
    "code": 200,
    "message": "SUCCESS",
    "item": {
        "header": {
            "index": 9,
            "difficulty": 2.8269553036454149273332760011886696253239742350009903329945699220681916416e+73,
            "nonce": 18508,
            "timestamp": 1531740497326,
            "hash": "00059e0981bcd08ed4fbaf8973a738a2111ab5887c54e4b685579658cb4bb38c",
            "previousHash": "0002d041d584afb3609bfeb58a1eb25bef5540154f6b672522ce6e455c08c75b"
        },
        "body": {
            "transactions": [
                {
                    "from": null,
                    "sign": null,
                    "to": "0x69dc11cae775647d495b2c8930a17b31827b286b",
                    "publicKey": null,
                    "amount": 50,
                    "timestamp": 1531740497326,
                    "txHash": "0xd48edd85006ae5331fb934b0236944eb5f87761a3784582cd3dd03b793d17e5a",
                    "status": "SUCCESS",
                    "errorMessage": null,
                    "data": "Miner Reward."
                },
                {
                    "from": "0x69dc11cae775647d495b2c8930a17b31827b286b",
                    "sign": "30450220644FC1CAA4342AB7AFBEF200DA80E6870BBB9C5D3638CCE14635713B4E88BA80022100CA60B42FBDD6767E9605E005296499D682525D429BF0ACEECB450B826510534E",
                    "to": "0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
                    "publicKey": "PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCz7FbyM3mvg3favYhhHXarHN6hXgYwKtvLAfXM5YgLDnZx1YPoo4G9pdiR5RQrhtBYriMCh5mGC3RC93HLFkBnAgi",
                    "amount": 5.5,
                    "timestamp": 1531740497291,
                    "txHash": "0x2a06176017345522882bbf6a6e5c4247ecdfc49fc705edab6e820f88af89add6",
                    "status": "APPENDING",
                    "errorMessage": null,
                    "data": "hello world"
                }
            ]
        }
    }
}
```

应广大区块链爱好者的请求，我们还是决定放出社区二维码，为大家提供一个讨论的地方：
![](wechat.jpeg)
