package com.cocodan.triplan.aop.logtrace;

public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus traceStatus);

    void exception(TraceStatus traceStatus, Exception exception);
}
