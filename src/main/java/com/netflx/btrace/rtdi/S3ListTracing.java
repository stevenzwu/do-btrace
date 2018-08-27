package com.netflx.btrace.rtdi;

import com.sun.btrace.annotations.*;

import java.util.concurrent.atomic.AtomicLong;

import static com.sun.btrace.BTraceUtils.*;

// @BTrace annotation tells that this is a BTrace program
@BTrace
public class S3ListTracing {
    private static AtomicLong putCounter = newAtomicLong(0);
    private static AtomicLong listCounter = newAtomicLong(0);

    @OnMethod(
            clazz="com.amazonaws.services.s3.AmazonS3Client",
            method="putObject",
            location=@Location(value=Kind.LINE, line=1623)
    )
    public static void trackPut() {
        addAndGet(putCounter, 1L);
    }

    @OnMethod(
            clazz="com.amazonaws.services.s3.AmazonS3Client",
            method="listObjects",
            location=@Location(value=Kind.LINE, line=829)
    )
    public static void trackList() {
        addAndGet(listCounter, 1L);
        print('[');
        print(threadId(currentThread()));
        print(']');
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        jstack();
        println();
    }

    @OnTimer(60000)
    public static void dumpCounters() {
        final long putCount = getAndSet(putCounter, 0);
        final long headCount = getAndSet(listCounter, 0);
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        println("dumping counter: ");
        printNumber("put", putCount);
        printNumber("list", headCount);
        println();
    }

}
