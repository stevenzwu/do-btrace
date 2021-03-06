package com.netflx.btrace.rtdi;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

import java.util.concurrent.atomic.AtomicLong;

import static com.sun.btrace.BTraceUtils.*;

// @BTrace annotation tells that this is a BTrace program
@BTrace
public class S3ListTracing {
    private static AtomicLong putCounter = newAtomicLong(0);
    private static AtomicLong listCounter = newAtomicLong(0);
    private static AtomicLong listV2Counter = newAtomicLong(0);
    private static AtomicLong initMultipartUploadCounter = newAtomicLong(0);
    private static AtomicLong transMgrUploadCounter = newAtomicLong(0);
    private static AtomicLong transMgrCopyCounter = newAtomicLong(0);

    @OnMethod(
            clazz="com.amazonaws.services.s3.AmazonS3Client",
            method="putObject"
    )
    public static void putObject(@ProbeClassName String cn,
                                 @ProbeMethodName String mn,
                                 AnyType[] args) {
        BTraceUtils.printFields(args[0]);
    }

    @OnMethod(
            clazz="com.facebook.presto.s3fs.PrestoS3FileSystem",
            method="listPrefix"
    )
    public static void listPrefix(@ProbeClassName String cn,
                                  @ProbeMethodName String mn,
                                  AnyType[] args) {
        BTraceUtils.printFields(args[0]);
    }

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

    @OnMethod(
            clazz="com.amazonaws.services.s3.AmazonS3Client",
            method="listObjectsV2",
            location=@Location(value=Kind.LINE, line=865)
    )
    public static void trackListV2() {
        addAndGet(listV2Counter, 1L);
        print('[');
        print(threadId(currentThread()));
        print(']');
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        jstack();
        println();
    }

    @OnMethod(
            clazz="com.amazonaws.services.s3.AmazonS3Client",
            method="initiateMultipartUpload",
            location=@Location(value=Kind.LINE, line=3129)
    )
    public static void trackInitMultipartUpload() {
        addAndGet(initMultipartUploadCounter, 1L);
    }

    @OnMethod(
            clazz="com.amazonaws.services.s3.transfer.TransferManager",
            method="doUpload"
    )
    public static void trackTransMgrUpload() {
        addAndGet(transMgrUploadCounter, 1L);
    }

    @OnMethod(
            clazz="com.amazonaws.services.s3.transfer.TransferManager",
            method="copy",
            location=@Location(value=Kind.LINE, line=1953)
    )
    public static void trackTransMgrCopy() {
        addAndGet(transMgrCopyCounter, 1L);
    }

    @OnTimer(60000)
    public static void dumpCounters() {
        final long putCount = getAndSet(putCounter, 0);
        final long listCount = getAndSet(listCounter, 0);
        final long listV2Count = getAndSet(listV2Counter, 0);
        final long initMultipartUploadCount = getAndSet(initMultipartUploadCounter, 0);
        final long transMgrUploadCount = getAndSet(transMgrUploadCounter, 0);
        final long transMgrCopyCount = getAndSet(transMgrCopyCounter, 0);
        println(timestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        println("dumping counter: ");
        printNumber("put", putCount);
        printNumber("list", listCount);
        printNumber("listV2", listV2Count);
        printNumber("initMultipartUpload", initMultipartUploadCount);
        printNumber("transMgrUpload", transMgrUploadCount);
        printNumber("transMgrCopy", transMgrCopyCount);
        println();
    }

}
