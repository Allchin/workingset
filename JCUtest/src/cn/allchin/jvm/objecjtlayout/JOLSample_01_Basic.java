package cn.allchin.jvm.objecjtlayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * ��ӡ�����ڴ�Ĺ���
 * demo of 
 * http://hg.openjdk.java.net/code-tools/jol/file/03064c057dc9/jol-samples/src/main/java/org/openjdk/jol/samples/JOLSample_01_Basic.java
 * 
 * 
 * ����jol��ʹ��ʾ����
 * http://hg.openjdk.java.net/code-tools/jol/file/tip/jol-samples/src/main/java/org/openjdk/jol/samples/
 * 
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
