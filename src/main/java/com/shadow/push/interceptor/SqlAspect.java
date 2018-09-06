package com.shadow.push.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.datayoo.moql.MoqlException;
import org.datayoo.moql.sql.SqlDialectType;
import org.datayoo.moql.translator.MoqlTranslator;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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

            sqlSet.clear();
            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            List<HashMap> sqlList = new ArrayList<>();
            // + in 操作

            sqlSet.add("update dl_b_pur_ret set STATUS=2 where BILL_NO='PR0118072800004'");
            sqlSet.add("delete from User where id in (1, 2, 3) and userName = '123'");
//            sqlSet.add("select u.* from User as u where u.id in (1, 2, 3) and u.userName = '123'");
//            sqlSet.add("select u.* from User u where id in (1, 2, 3) and userName = '123'");
            sqlSet.forEach(
                    sql -> {

                        HashMap<String, Object> sqlDetail = new HashMap<>();
                        try {
                            String type = sql.split(" ")[0];

                            Table table = null;
                            Expression where = null;
                            List<Table> tables = new ArrayList<>();
                            List<Column> columns = new ArrayList<>();
                            List<HashMap> operators = new ArrayList<>();

                            if ("insert".equals(type.toLowerCase())) {
                                Insert insert = (Insert) parserManager.parse(new StringReader(sql));
                                columns = insert.getColumns();
                                table = insert.getTable();
                            } else if ("update".equals(type.toLowerCase())) {
                                Update update = (Update) parserManager.parse(new StringReader(sql));
                                columns = update.getColumns();
                                tables = update.getTables();
                                where = update.getWhere();
                            } else if ("delete".equals(type.toLowerCase())) {
                                Delete delete = (Delete) parserManager.parse(new StringReader(sql));
                                table = delete.getTable();
                                where = delete.getWhere();

                            }
                            String newSql = null;
                            if ("update".equals(type.toLowerCase()) || "delete".equals(type.toLowerCase())) {
                                if (tables != null && tables.size() > 0) {
                                    newSql = "select " + tables.get(0).toString().charAt(0) + ".* from " + tables.get(0).toString() + " " + tables.get(0).toString().charAt(0) + " where " + where;
                                } else if (table != null) {
                                    newSql = "select " + table.toString().charAt(0) + ".* from " + table + " " + table.toString().charAt(0) + " where " + where;
                                }
                            }
                            newSql="SELECT DL_B_PUR_IN.ID b0,DL_B_PUR_IN.BILL_NO b1,DL_B_PUR_IN.MODIFY_BILL_NO b2,DL_B_PUR_IN.ORDER_BILL_NO b3,DL_B_PUR_IN.BILL_DATE b4,DL_B_PUR_IN.CP_C_SUPPLIER_ID b5,DL_B_PUR_IN.CP_C_STORE_ID b6,DL_B_PUR_IN.BILL_TYPE b7,DL_B_PUR_IN.FROM_BILL_NO b8,DL_B_PUR_IN.SOURCE_BILL_NO b9,DL_B_PUR_IN.SUM_QTY_BILL b10,DL_B_PUR_IN.SUM_QTY_IN b11,DL_B_PUR_IN.PS_C_PRO_ECODE_LIST b12,a0.BILL_NO b13,a0.ID b14,DL_B_PUR_IN.SUM_AMT_LIST_IN b15,DL_B_PUR_IN.SUM_AMT_LIST b16,DL_B_PUR_IN.SUM_AMT_ACTUAL b17,DL_B_PUR_IN.SUM_AMT_IN b18,DL_B_PUR_IN.OUT_BILL_ID b19,DL_B_PUR_IN.REMARK b20,DL_B_PUR_IN.STATUS b21,DL_B_PUR_IN.ACCEPTE_STATUS b22,DL_B_PUR_IN.ISACTIVE b23,DL_B_PUR_IN.WMS_IN b24,DL_B_PUR_IN.CREATIONDATE b25,DL_B_PUR_IN.MODIFIERENAME b26,DL_B_PUR_IN.MODIFIEDDATE b27 FROM DL_B_PUR_IN left join DL_B_PUR_ORDER a0 on a0.ID =DL_B_PUR_IN.SOURCE_BILL_ID WHERE ( (DL_B_PUR_IN.ID IN (20180709020873,20180709020872,20180709020871,20180709020870,20180709020869,20180709020868,20180709020867,20180709020866,20180709020865,20180709020864)) )";
                            try {
                                String es = MoqlTranslator
                                        .translateMoql2Dialect(newSql, SqlDialectType.ELASTICSEARCH);
                                es = es.trim();
                                System.out.println(es);
                            } catch (MoqlException e) {
                                e.printStackTrace();
                            }


                            sqlDetail.put("tables", tables);
                            sqlDetail.put("table", table);
                            sqlDetail.put("columns", columns);
                        } catch (JSQLParserException e) {
                            e.printStackTrace();
                        }
                        sqlList.add(sqlDetail);

                    }
            );
            //TODO 发MQ
            context.setSqlSet(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public List<HashMap> judgeExp(List<HashMap> operators, Expression exp) {
//        if (exp instanceof EqualsTo) {
//            operators.add(parseEqualsExp(exp));
//        } else if (exp instanceof AndExpression) {
//            parseAndExp(operators, exp);
//        } else if (exp instanceof InExpression) {
//            operators.add(parseInExp(exp));
//        }
//        return operators;
//    }
//
//
//    public List<HashMap> parseAndExp(List<HashMap> operators, Expression where) {
//        HashMap<String, Object> operator = new HashMap<>();
//        AndExpression equalsTo = (AndExpression) where;
//        operator.put("operator", "AND");
//        Expression leftExp = equalsTo.getLeftExpression();
//        Expression rightExp = equalsTo.getRightExpression();
//        judgeExp(operators, leftExp);
//        judgeExp(operators, rightExp);
//
//        return operators;
//    }
//
//
//    // =操作
//    public HashMap parseEqualsExp(Expression where) {
//        HashMap<String, Object> operator = new HashMap<>();
//        EqualsTo equalsTo = (EqualsTo) where;
//        if (((EqualsTo) where).isNot()) {
//            operator.put("operator", "!=");
//        } else {
//            operator.put("operator", "=");
//        }
//        operator.put("leftExp", equalsTo.getLeftExpression());
//        operator.put("rightExp", equalsTo.getRightExpression());
//        return operator;
//    }
//
//    public HashMap parseInExp(Expression where) {
//        HashMap<String, Object> operator = new HashMap<>();
//        InExpression inExpression = (InExpression) where;
//        if (inExpression.isNot()) {
//            operator.put("operator", "not in");
//        } else {
//            operator.put("operator", "in");
//        }
//
//        operator.put("leftExp", inExpression.getLeftExpression());
//        operator.put("rightExp", inExpression.getRightItemsList());
//        return operator;
//    }
//
//    public HashMap parseBetweenExp(Expression where) {
//        HashMap<String, Object> operator = new HashMap<>();
//        EqualsTo equalsTo = (EqualsTo) where;
//        if (((EqualsTo) where).isNot()) {
//            operator.put("operator", "!=");
//        } else {
//            operator.put("operator", "=");
//        }
//        operator.put("leftExp", equalsTo.getLeftExpression());
//        operator.put("rightExp", equalsTo.getRightExpression());
//        return operator;
//    }
}