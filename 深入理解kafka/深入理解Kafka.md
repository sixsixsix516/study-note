### 1. 初识Kafka

zookeeper是kafka用来负责集群元数据的管理, 控制器的选举等操作

- producer 生产者 负责发送消息 将消息投入kafka
- consumer 消费者 负责接收消息 连接到kafka并接收消息
- broker 服务代理节点 独立的kafka服务节点或kafka实例, 一个或多个broker组成了一个kafka集群

Kafka中的消息以主题为单位进行归类, 生产者负责将消息发送到特定的主题, 而消费者负责订阅主题并进行消费

主题可细分为多个分区 ,  一个主题可横跨多个broker

每一条消息被发送到broker之前, 会根据分区规则选择存储到哪个具体的分区

如果一个主题只包含一个文件, 那么这个文件所在的机器I/O将会成为这个主题的性能瓶颈, 而分区解决了这个问题. 

分区还有副本(replica)机制, 可以提升容灾能力, 同一分区的不同副本中保存的是相同的消息(在同一时刻, 副本之间并非完全一样), 副本之间是一主多从的关系, 其中 leader副本负责处理写请求, follower副本只负责leader副本的消息同步. 副本处于不同的broker中,当leader副本出现故障时, 从follower副本中重新选举新的leader副本对外提供服务. kafka通过多副本机制实现了故障的自动转移,当kafka集群中的某个broker失效时仍然能保证服务可用

**生产与消费示例**

进入kafka bin目录

执行 

创建主题 名称,  1个副本 4个分区 ` ./kafka-topics.sh --zookeeper localhost:2181 --create --topic student-topic --replication-factor 1 --partitions 4 ` 

查看主题详情 `./kafka-topics.sh --zookeeper localhost:2181 --describe --topic student-topic`

开启消费者 `./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic student-topic`

开启生产者 `./kafka-console-producer.sh --broker-list localhost:9092 --topic student-topic`



### API的使用

每一个分区的消息只能被一个消费组里的消费者消费



如果所有的消费者都隶属于同一个消费组，那么所有的消息都会被均衡地投递给每一个消费者，即每条消息只会被一个消费者处理，这就相当于点对点模式的应用。· 如果所有的消费者都隶属于不同的消费组，那么所有的消息都会被广播给所有的消费者，即每条消息会被所有的消费者处理，这就相当于发布/订阅模式的应用。

当创建一个主题后 会在data文件夹下 创建相应主题分区目录

















