package com.shadow.push.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.txc.common.ContextStep2;
import com.taobao.txc.common.TxcContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import java.util.List;

@Aspect
@Service
@Slf4j
public class TxcAspect {

    /**
     * 环绕通知
     */

    @Around(value = "execution(* com.taobao.txc.resourcemanager.executor.TxcLogManager.branchCommit(java.util.List<com.taobao.txc.common.ContextStep2>)))")
    public void interceptorTxcCommit(ProceedingJoinPoint pjp) throws Throwable {

        try {
            Object[] args = pjp.getArgs();
            if (args != null && args.length > 0) {
                for (Object arg : args) {
                    JSONArray txcInfos = new JSONArray();
                    List<ContextStep2> contextStep2s = (List<ContextStep2>) arg;
                    contextStep2s.forEach(
                            contextStep2 -> {
                                JSONObject txcInfo = new JSONObject();
                                txcInfo.put("dbName", contextStep2.getDbname());
                                txcInfo.put("sql", contextStep2.getRetrySql());
                                txcInfo.put("txcId", contextStep2.getXid());
                                txcInfo.put("branchId", contextStep2.getBranchId());
                                txcInfos.add(txcInfo);
                            }
                    );
                    String txcId = TxcContext.getCurrentXid();
//                    log.debug("***************TxcCommitId" + txcId);
//                    log.debug("***************TxcCommitSql" + JSON.toJSONString(txcInfos));

                    //TODO push2Redis
//                    CusRedisTemplate<String, String> redisTemplate = RedisOpsUtil.getObjRedisTemplate();
//                    Boolean isLock = redisTemplate.opsForValue().setIfAbsent(txcId, JSON.toJSONString(txcInfos));
//                    redisTemplate.expire(txcId, 2, TimeUnit.HOURS);
//                    if (!isLock) {
//                        throw new NDSException("当前记录正在被操作，请稍后重试！");
//                    }
                }

            }
            pjp.proceed();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 对txcCommit注解进行aop，对切点加锁
     */
    @Pointcut(value = "execution(* com.taobao.txc.resourcemanager.executor.TxcLogManager.deleteUndoLog(java.util.List<com.taobao.txc.common.ContextStep2>,..))")
    public void txcUndoPoint() {
    }

    /**
     * 环绕通知
     */

    @Around(value = "txcUndoPoint()")
    public void interceptorTxcUndo(ProceedingJoinPoint pjp) throws Throwable {

        try {
            Object[] args = pjp.getArgs();
            if (args != null && args.length > 0) {
                for (Object arg : args) {
                    JSONArray txcInfos = new JSONArray();
                    List<ContextStep2> contextStep2s = (List<ContextStep2>) arg;
                    contextStep2s.forEach(
                            contextStep2 -> {
                                JSONObject txcInfo = new JSONObject();
                                txcInfo.put("dbName", contextStep2.getDbname());
                                txcInfo.put("sql", contextStep2.getRetrySql());
                                txcInfo.put("txcId", contextStep2.getXid());
                                txcInfo.put("branchId", contextStep2.getBranchId());
                                txcInfos.add(txcInfo);
                            }
                    );
                    String txcId = TxcContext.getCurrentXid();
//                    log.debug("***************TxcUndoId" + txcId);
//                    log.debug("***************TxcUndoSql" + JSON.toJSONString(txcInfos));

                    // TODO sendMqMessage
//                    CusRedisTemplate<String, String> redisTemplate = RedisOpsUtil.getObjRedisTemplate();
//                    Boolean isLock = redisTemplate.opsForValue().setIfAbsent(txcId, JSON.toJSONString(txcInfos));
//                    redisTemplate.expire(txcId, 2, TimeUnit.HOURS);
//                    if (!isLock) {
//                        throw new NDSException("当前记录正在被操作，请稍后重试！");
//                    }
                }

            }
            pjp.proceed();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 环绕通知
     */

    @Around(value = "execution(* org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(org.springframework.transaction.TransactionStatus))")
    public void interceptorTranCommit(ProceedingJoinPoint pjp) throws Throwable {

        Object[] args = pjp.getArgs();
        try {
            pjp.proceed();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
