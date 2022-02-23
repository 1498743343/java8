package com.chen.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamTest {

    public long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + sum);
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .sum();
    }

    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .sum();
    }

    public static long iterativeSum(long n) {
        long result = 0;
        for (long i = 1L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    @Test
    public void sequentialSum() {
        System.out.println("Sequential sum done in:" +
                measureSumPerf(StreamTest::sequentialSum, 10_000_000) + " msecs");
    }
    @Test
    public void parallelSum() {
        System.out.println("Parallel sum done in: " +
                measureSumPerf(StreamTest::parallelSum, 10_000_000) + " msecs" );
    }
    @Test
    public void iterativeSum() {
        System.out.println("Iterative sum done in:" +
                measureSumPerf(StreamTest::iterativeSum, 10_000_000) + " msecs");
    }

    @Test
    public void rangedSum() {
        System.out.println("Range sum done in:" +
                measureSumPerf(StreamTest::rangedSum, 10_000_000) + " msecs");
    }

    @Test
    public void parallelRangedSum() {
        System.out.println("Parallel range sum done in:" +
                measureSumPerf(StreamTest::parallelRangedSum, 10_000_000) + " msecs");
    }

}
