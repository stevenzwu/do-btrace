package com.netflx.btrace.rtdi;

import com.sun.btrace.annotations.*;

import java.util.concurrent.atomic.AtomicLong;

import static com.sun.btrace.BTraceUtils.*;

// @BTrace annotation tells that this is a BTrace program
@BTrace
public class S3ReqTracing {
    private static AtomicLong putCounter = newAtomicLong(0);
    private static AtomicLong headCounter = newAtomicLong(0);
    private static AtomicLong createCounter = newAtomicLong(0);
    private static AtomicLong constructorCounter = newAtomicLong(0);
    private static AtomicLong flushCounter = newAtomicLong(0);

    @OnMethod(
            clazz="com.amazonaws.services.s3.AmazonS3Client",
            method="putObject"
    )
    public static void trackPut() {
        addAndGet(putCounter, 1L);
    }

    @OnMethod(
            clazz="com.amazonaws.services.s3.AmazonS3Client",
            method="getObjectMetadata",
            location=@Location(value=Kind.LINE, line=966)
    )
    public static void trackHead() {
        addAndGet(headCounter, 1L);
        print('[');
        print(threadId(currentThread()));
        print(']');
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        jstack();
        println();
    }

    @OnMethod(
            clazz="org.apache.flink.runtime.state.filesystem.FsCheckpointStreamFactory",
            method="createCheckpointStateOutputStream"
    )
    public static void trackCreate() {
        addAndGet(createCounter, 1L);
    }


    @OnMethod(
            clazz="org.apache.flink.runtime.state.filesystem.FsCheckpointStreamFactory$FsCheckpointStateOutputStream",
            method="<init>"
    )
    public static void trackConstructor() {
        addAndGet(constructorCounter, 1L);
    }

    @OnMethod(
            clazz="org.apache.flink.runtime.state.filesystem.FsCheckpointStreamFactory$FsCheckpointStateOutputStream",
            method="flush"
    )
    public static void trackFlush() {
        addAndGet(flushCounter, 1L);
    }

    @OnTimer(15000)
    public static void dumpCounters() {
        final long putCount = getAndSet(putCounter, 0);
        final long headCount = getAndSet(headCounter, 0);
        final long createCount = getAndSet(createCounter, 0);
        final long constructorCount = getAndSet(constructorCounter, 0);
        final long flushCount = getAndSet(flushCounter, 0);
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        println("15s timer: dumping counter");
        printNumber("put", putCount);
        printNumber("head", headCount);
        printNumber("create", createCount);
        printNumber("constructor", constructorCount);
        printNumber("flush", flushCount);
        println();
    }

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
