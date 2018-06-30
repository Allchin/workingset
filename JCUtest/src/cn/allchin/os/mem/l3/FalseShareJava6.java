package cn.allchin.os.mem.l3;

/**
 * <pre>
 * 问题：演示java 6 下的伪缓存。
 * 
 * JAVA 6下的方案 解决伪共享的办法是使用缓存行填充， 
 * 使一个对象占用的内存大小刚好为64bytes或它的整数倍，
 * 这样就保证了一个缓存行里不会有多个对象。 
 * 《剖析Disruptor:为什么会这么快？(三)伪共享》提供了缓存行填充的例子：
 
  
   @see envJava6项目中的FalseShareJava6
 * @author citi0
 *
 */
public class FalseShareJava6 implements FalseShare{}