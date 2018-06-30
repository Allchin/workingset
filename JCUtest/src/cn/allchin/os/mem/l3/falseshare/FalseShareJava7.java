package cn.allchin.os.mem.l3.falseshare;

/**
 * <pre>
 * 问题：
 * 演示java 7 下的伪缓存
 * 因为JAVA 7会优化掉无用的字段，可以参考《False Sharing && Java 7》。
 
因此，JAVA 7下做缓存行填充更麻烦了，需要使用继承的办法来避免填充被优化掉，

 * @author citi0
 *
 */
public class FalseShareJava7 implements FalseShare{

}
