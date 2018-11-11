package com.netflx.btrace.rtdi;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

import java.util.concurrent.atomic.AtomicLong;

import static com.sun.btrace.BTraceUtils.*;

// @BTrace annotation tells that this is a BTrace program
@BTrace
public class MetacatTracing {

    @OnMethod(
            clazz="com.netflix.iceberg.BaseMetastoreTableOperations",
            method="refreshFromMetadataLocation"
    )
    public static void putObjectArgs(@ProbeClassName String cn,
                                 @ProbeMethodName String mn,
                                 AnyType[] args) {
        print('[');
        print(threadId(currentThread()));
        print(']');
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        jstack();
        println();
    }

}
