package cn.allchin.os.mem.l3.falseshare;

/**<pre>
 * 什么是伪共享
 * 
 * 缓存系统中是以缓存行（cache line）为单位存储的。
 * 缓存行是2的整数幂个连续字节，一般为32-256个字节。
 * 最常见的缓存行大小是64个字节。当多线程修改互相独立的变量时，
 * 如 果这些变量共享同一个缓存行，就会无意中影响彼此的性能，这就是伪共享。
 * 
 * 
 * https://www.cnblogs.com/Binhua-Liu/p/5620339.html
 * http://developer.51cto.com/art/201306/398232.htm
 * 
 * 
 * @author citi0
 *
 */
public interface FalseShare {

}
