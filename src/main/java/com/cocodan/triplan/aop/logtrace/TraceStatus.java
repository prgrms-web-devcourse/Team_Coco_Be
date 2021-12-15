package com.cocodan.triplan.aop.logtrace;

public class TraceStatus {

    private TraceId traceId;

    private Long startTime;

    private String message;

    public TraceStatus(TraceId traceId, Long startTime, String message) {
        this.traceId = traceId;
        this.startTime = startTime;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public String getMessage() {
        return message;
    }
}
