### 场景

#### 现状：

现在推送ES的代码分布在各个业务子模块中，由业务代码执行完成后把相关数据push到ES中

##### 缺点： 
1. 推送ES代码分散，可维护性差

2. 业务侵入性太强
      
3. A应用调用B应用，事务回滚后，需要手动删除ES上的数据，做类似TCC的操作，风险较大，可能会露数据，无法保证最终强一致性。

#### 现在遇到的问题
    
因为使用的是DRDS,没有row级别的binlog，所以使用不了ES官方推荐的logstash-input-jdbc，并且由于业务需求，一些推送的ES的数据需要计算
    
#### 现有的方案
    
现在的方案是在事务commit时候去推送ES，具体方案如下：

1. Spring Transactional 在事务commit时候，收集数据，先将数据扔到Redis上面，然后发送异步消息，由消费方去推送ES

2. ali GTS 阿里GTS，由于有阿里的协调器处理各个APP之间的事务一致性，单个APP commit成功，事务仍有可能rollback掉。
    所以将收集数据同步到Redis和发送MQ消息分为两步去做，具体方案如下。
        1.1 在执行com.taobao.txc.resourcemanager.executor.branchCommit()方法时，会提交commit，此时收集数据，并将数据同步至Redis中，RedisKey为TxcId（TxcId为事务ID，同一事务中各个APP ID相同）
        1.2 在执行com.taobao.txc.resourcemanager.executor.TxcLogManager.deleteUndoLog()方法时，发送MQ消息，通知消费方推送ES。阿里GTS会在整个事务完成时，清空undo_log表中记录的反向sql。
        
3. 消费方，负责解析sql，提取更新系操作的元数据，根据ES的Mapping，将数据同步至ES中。