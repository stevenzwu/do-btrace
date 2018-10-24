package com.netflx.btrace.rtdi;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

import java.util.concurrent.atomic.AtomicLong;

import static com.sun.btrace.BTraceUtils.*;

// @BTrace annotation tells that this is a BTrace program
@BTrace
public class Cancel {
    private static AtomicLong open414Counter = newAtomicLong(0);
    private static AtomicLong open418Counter = newAtomicLong(0);
    private static AtomicLong open419Counter = newAtomicLong(0);
    private static AtomicLong runCounter = newAtomicLong(0);
    private static AtomicLong cancelCounter = newAtomicLong(0);

    @OnMethod(
            clazz="org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase",
            method="open",
            location=@Location(value=Kind.LINE, line=414)
    )
    public static void trackOpen414() {
        addAndGet(open414Counter, 1L);
    }

    @OnMethod(
            clazz="org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase",
            method="open",
            location=@Location(value=Kind.LINE, line=418)
    )
    public static void trackOpen418() {
        addAndGet(open418Counter, 1L);
    }

    @OnMethod(
            clazz="org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase",
            method="open",
            location=@Location(value=Kind.LINE, line=517)
    )
    public static void trackOpen419() {
        addAndGet(open419Counter, 1L);
    }

    @OnMethod(
            clazz="org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase",
            method="run"
    )
    public static void trackRun() {
        addAndGet(runCounter, 1L);
    }

    @OnMethod(
            clazz="org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase",
            method="cancel"
    )
    public static void trackCancel() {
        addAndGet(cancelCounter, 1L);
    }

    @OnTimer(2000)
    public static void dumpCounters() {
        final long open414Count = getAndSet(open414Counter, 0);
        final long open418Count = getAndSet(open418Counter, 0);
        final long open419Count = getAndSet(open419Counter, 0);
        final long runCount = getAndSet(runCounter, 0);
        final long cancelCount = getAndSet(cancelCounter, 0);
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        println("15s timer: dumping counter");
        printNumber("open414Count", open414Count);
        printNumber("open418Count", open414Count);
        printNumber("open419Count", open414Count);
        printNumber("runCount", runCount);
        printNumber("cancelCount", cancelCount);
        println();
    }

}

