package com.cocodan.triplan.aop.logtrace;

import org.springframework.security.core.context.SecurityContextHolder;

public class TraceId {

    private Object id;
    private int level;

    public TraceId() {
        // TODO : 시큐리티 붙으면 사용자 id로 변경
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.id = principal;
        this.level = 0;
    }

    private TraceId(Object id, int level) {
        this.id = id;
        this.level = level;
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public Object getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
