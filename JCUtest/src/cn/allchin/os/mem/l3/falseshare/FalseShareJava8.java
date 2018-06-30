package cn.allchin.os.mem.l3.falseshare;

/**
 * <pre>
 * 问题：
 * 演示java 8 下伪缓存和缓存填充。
 * 在JAVA 8中，缓存行填充终于被JAVA原生支持了。JAVA 8中添加了一个@Contended的注解，添加这个的注解，将会在自动进行缓存行填充。 
 * 
 * @see envJava8工程中的FalseShareJava8
 * @author renxing.zhang
 *
 */
public class FalseShareJava8 implements FalseShare {

}
