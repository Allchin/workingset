package cn.allchin.jvm.objecjtlayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 打印对象内存的工具
 * demo of 
 * http://hg.openjdk.java.net/code-tools/jol/file/03064c057dc9/jol-samples/src/main/java/org/openjdk/jol/samples/JOLSample_01_Basic.java
 * 
 * 
 * 更多jol的使用示例：
 * http://hg.openjdk.java.net/code-tools/jol/file/tip/jol-samples/src/main/java/org/openjdk/jol/samples/
 * 
 * 
 * # Running 64-bit HotSpot VM.
# Using compressed oop with 3-bit shift.
# Using compressed klass with 3-bit shift.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

cn.allchin.jvm.objecjtlayout.JOLSample_01_Basic object internals:
 OFFSET  SIZE             TYPE DESCRIPTION                               VALUE
      0    12                  (object header)                           N/A
     12     4   java.util.List JOLSample_01_Basic.strList                N/A
     16     4    java.util.Map JOLSample_01_Basic.hMap                   N/A
     20     4                  (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total



 * @author renxing.zhang
 *
 */
public class JOLSample_01_Basic {
	private List<String> strList=Arrays.asList(new String[]{"a","b"});
	private Map<String,String> hMap=new HashMap<String,String>(2);
	 
	
	public static void main(String[] args) {
		System.out.println(VM.current().details() );
		String layout=ClassLayout.parseClass(JOLSample_01_Basic.class).toPrintable();
		System.out.println(layout);
	}
}
