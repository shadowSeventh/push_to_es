package com.shadow.push.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;

@Aspect
@Component
@Slf4j
public class SqlAspect {

    @Around(value = "execution(* org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(org.springframework.transaction.TransactionStatus))")
    public void interceptorTranCommit(ProceedingJoinPoint pjp) throws Throwable {
        try {
            ThreadLocalContext context = ThreadLocalContext.getThreadInstance();
            HashSet<String> sqlSet = context.getSqlSet();

            if (sqlSet != null) {
                //TODO pushToRedis
            }
            pjp.proceed();


            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            sqlSet.forEach(
                    sql -> {
                        try {
                            String type = sql.split(" ")[0];
                            if ("insert".equals(type.toLowerCase())) {
                                Insert insert = (Insert) parserManager.parse(new StringReader(sql));
                                List<Column> columns = insert.getColumns();
                            } else if ("update".equals(type.toLowerCase())) {
                                Update update = (Update) parserManager.parse(new StringReader(sql));
                                List<Column> columns = update.getColumns();
                            } else if ("delete".equals(type.toLowerCase())) {
                                Delete delete = (Delete) parserManager.parse(new StringReader(sql));
                                Table table = delete.getTable();
                            }
                        } catch (JSQLParserException e) {
                            e.printStackTrace();
                        }
                    }
            );


            //TODO 发MQ
            context.setSqlSet(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}