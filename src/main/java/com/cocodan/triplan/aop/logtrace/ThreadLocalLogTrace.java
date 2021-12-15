package com.cocodan.triplan.aop.logtrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ThreadLocalLogTrace implements LogTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        long startTime = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTime, message);
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    private String addSpace(String prefix, int level) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            if (i == level - 1) {
                stringBuilder.append("|").append(prefix);
                continue;
            }
            stringBuilder.append("|  ");
        }

        return stringBuilder.toString();
    }

    @Override
    public void end(TraceStatus traceStatus) {
        complete(traceStatus, null);
    }

    private void complete(TraceStatus traceStatus, Exception exception) {
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - traceStatus.getStartTime();
        TraceId traceId = traceStatus.getTraceId();
        if (exception == null) {
            log.info("[{}] {}{} time = {}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), traceStatus.getMessage(), resultTime);
        } else {
            log.info("[{}] {}{} time = {}ms", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), traceStatus.getMessage(), resultTime);
        }

        releaseTraceId();
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    @Override
    public void exception(TraceStatus traceStatus, Exception exception) {
        complete(traceStatus, exception);
    }
}
