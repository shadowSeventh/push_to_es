package com.shadow.push.interceptor;

import java.util.HashSet;

public class ThreadLocalContext {
    private static ThreadLocal<ThreadLocalContext> t = new ThreadLocal<>();

    private ThreadLocalContext() {
    }

    public static ThreadLocalContext getThreadInstance() {
        ThreadLocalContext context = t.get();
        if (null == context) {
            context = new ThreadLocalContext();
            t.set(context);
        }
        return context;
    }

    private HashSet<String> sqlSet = new HashSet<>();

    public HashSet<String> getSqlSet() {
        return sqlSet;
    }

    public void setSqlSet(HashSet<String> sqlSet) {
        this.sqlSet = sqlSet;
    }
}
