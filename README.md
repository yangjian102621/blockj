# blockchain-java

> 区块链 Java 的简易实现版，自学了一段时间的区块链理论知识，自己尝试着写一个具有钱包雏形的区块链项目，开源出来，供像我一样想开发区块链又不知从何下手初学者参考，也希望大牛们能够给点指导。现在大部门区块链从业者都是从事 DApp 开发，做底层链开发的还是比较少，目前也没有很大必要，不过尝试写链可以更好的理解区块链底层的运行逻辑。本人在开发的过程中最大的体会是，在理论学习的时候觉得区块链知识并不复杂，理论自己都懂了，但是真要自己实现了就会发现有各种坑，最后才发现自己对某些细节理解还是一知半解。希望这个项目能对初学区块链者有所帮助，大家一起学习，一起成长。

v1.0.0
1. 初步了完成的区块链的各个模块，包括账户，区块链，网络等模块实现
2. 实现了账户创建，发送交易和挖矿功能
3. 实现了网络功能，包括发送广播账户，广播区块，广播交易以及自动同步区块功能。

## 运行部署
项目默认部署4个节点，创建了4个配置文件 application-{env}.yml， 
如果想要部署更多的节点，再创建更多的配置文件就 OK 了。

使用 idea 部署测试非常简单，按照下面的方法添加多个 springBoot 启动配置。

![](./install.png)

然后分别启动 4 个节点就好了。启动之后节点之间自动连接成 P2P 网络，随后你就可以使用 postman 工具进行测试了，如果没有安装 postman 的话请自行安装，或者和我一样使用 chrome 浏览器的 postman 扩展。

## Web 测试 API

API名称 | 请求方式 | URL 
--------|---------|------
生成钱包 | POST | /account/new
查看钱包列表 | GET | /account/list
启动挖矿 | GET | /chain/mine
发送交易 | POST | /chain/transactions/new
查看最后一个区块 | GET | /block/view
添加节点 | POST | /node/add
查看节点 | GET | node/view

> 注意：凡是 POST 请求都是使用 RequestBody 的方式传参的， 不是用表单的 form-data 形式， 比如发送交易的参数形式如下：

````
{
	"sender":"1LptJ6Zs47eVG5aUoekgTCKcxqFZcHP4jM",
	"recipient":"13VjqfiRBNYSeTF2BXNZ4R17f3G3GbF93j",
	"amount":"30",
	"privateKey":"24f939c2bdc4a278e340bfa35c4695698d4d0fdce7316b7348557c58f9ada0c2",
	"data": "This is a test transaction"
}
````

![](./transaction.png)



