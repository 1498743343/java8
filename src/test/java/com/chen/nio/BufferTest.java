package com.chen.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

/**
 * Buffer 测试：Buffer类中比较重要的几个属性如下
 * <ul>
 *      <li>position-游标，读和写默认都是从0开始，最大值为 limit-1</li>
 *      <li>limit-最大上限，写模式下默认是 capacity 的值，读模式下为缓冲区的数据量大小</li>
 *      <li>capacity-容量，并不是字节数的大小，指的是写入的数据对象的数量</li>
 * </ul>
 *
 * @author chenzihan
 * @date 2022/02/23
 */
@Slf4j
public class BufferTest {
    /**
     * 创建 Buffer 必须使用静态方法 allocate，创建完成后默认是写状态，具体的 api 可以参见具体的 Buffer 子类
     */
    @Test
    public void allocate() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        log.info("position = {}", intBuffer.position());
        log.info("limit = {}", intBuffer.limit());
        log.info("capacity = {}", intBuffer.capacity());
    }

    /**
     * 在 buffer 中放数据
     */
    @Test
    public void put() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        IntStream.rangeClosed(1, 5)
                .forEach(intBuffer::put);
        log.info("position = {}", intBuffer.position());
        log.info("limit = {}", intBuffer.limit());
        log.info("capacity = {}", intBuffer.capacity());
    }

    /**
     * 翻转：从写模式转换为读模式。
     */
    @Test
    public void flip() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        IntStream.rangeClosed(1, 5)
                .forEach(intBuffer::put);
        intBuffer.flip();
        log.info("position = {}", intBuffer.position());
        log.info("limit = {}", intBuffer.limit());
        log.info("capacity = {}", intBuffer.capacity());
    }

    /**
     * 从缓冲区中读取数据，默认从 index 为 0 开始读取，可以自己指定 index 进行读取
     */
    @Test
    public void get() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        IntStream.rangeClosed(1, 5)
                .forEach(intBuffer::put);
        intBuffer.flip();
        IntStream.rangeClosed(1, 2)
                .forEach(i -> {
                    // 指定 index 读取不会改变 position 的值
                    int result = intBuffer.get(i);
                    log.info("result = {}", result);
                    log.info("position = {}", intBuffer.position());
                });
        IntStream.rangeClosed(1, 4)
                .forEach(i -> {
                    // 不指定 index 的值，会改变 position 的值
                    int result = intBuffer.get();
                    log.info("result = {}", result);
                    log.info("position = {}", intBuffer.position());
                });
        log.info("position = {}", intBuffer.position());
        log.info("limit = {}", intBuffer.limit());
        log.info("capacity = {}", intBuffer.capacity());
    }

    /**
     * 倒带：读取完以后，调用 rewind 方法可以重置 position 和 mark 的状态，从头开始读取
     */
    @Test
    public void rewind() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        IntStream.rangeClosed(1, 5)
                .forEach(intBuffer::put);
        intBuffer.flip();
        IntStream.rangeClosed(1, 2)
                .forEach(i -> intBuffer.get());
        intBuffer.rewind();
        log.info("position = {}", intBuffer.position());
        log.info("limit = {}", intBuffer.limit());
        log.info("capacity = {}", intBuffer.capacity());
    }

    /**
     * mark & reset，这两个方法一般配套使用，mark 记录 position 某一刻的状态，后续改变后，可以调用 reset 方法还原到 mark 时的状态
     */
    @Test
    public void reset() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        IntStream.rangeClosed(1, 5)
                .forEach(intBuffer::put);
        intBuffer.flip();
        IntStream.rangeClosed(0, 4)
                .forEach(i -> {
                    if (i == 2) {
                        intBuffer.mark();
                    }
                    int result = intBuffer.get();
                    log.info("result = {}", result);
                });
        log.info("position = {}", intBuffer.position());
        log.info("limit = {}", intBuffer.limit());
        log.info("capacity = {}", intBuffer.capacity());
        intBuffer.reset();
        log.info("position = {}", intBuffer.position());
        log.info("limit = {}", intBuffer.limit());
        log.info("capacity = {}", intBuffer.capacity());
    }

    /**
     * 清空缓冲区：会将 buffer 重置到初始的写状态，即 position = 0;limit = capacity;mark = -1;
     * 但是 clear 不会清空 buffer 的数组，所以通过 get(index) 还是可以获取固定位置下的元素
     */
    @Test
    public void clear() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        IntStream.rangeClosed(1, 5)
                .forEach(intBuffer::put);
        intBuffer.flip();
        intBuffer.clear();
        intBuffer.compact();
        IntStream.rangeClosed(0, 4)
                .forEach(i -> {
                    int result = intBuffer.get(i);
                    log.info("result = {}", result);
                });
    }

    /**
     * 压缩 buffer，具体规则参见 IntBuffer.compact 的注释
     */
    @Test
    public void compact() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        IntStream.rangeClosed(0, 4)
                .forEach(intBuffer::put);
        intBuffer.flip();
        intBuffer.compact();
        IntStream.rangeClosed(0, 9)
                .forEach(i -> {
                    int result = intBuffer.get(i);
                    log.info("result = {}", result);
                });
    }
}
