package com.netflx.btrace.rtdi;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;

import static com.sun.btrace.BTraceUtils.currentThread;
import static com.sun.btrace.BTraceUtils.jstack;
import static com.sun.btrace.BTraceUtils.print;
import static com.sun.btrace.BTraceUtils.println;
import static com.sun.btrace.BTraceUtils.threadId;
import static com.sun.btrace.BTraceUtils.timestamp;

// @BTrace annotation tells that this is a BTrace program
@BTrace
public class NPE {

    @OnMethod(
            clazz="java.lang.NullPointerException",
            method="<init>"
    )
    public static void trackNPE() {
        print('[');
        print(threadId(currentThread()));
        print(']');
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        jstack();
        println();
    }

}
