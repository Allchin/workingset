package cn.allchin.jvm.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.util.FileUtils;

/**
 * 解析  启动jvm参数
 * @author renxing.zhang
 *
 */
public class JvmStartUpParamParser {
	public static Map<String,String> paramMap=new HashMap();
	static{
 
		paramMap.put("-Xms", "初始堆内存");
		paramMap.put("-Xmx", "最大堆内存");
		
		
		paramMap.put("-server", "服务器模式");
		paramMap.put("-XX:+DisableExplicitGC", "禁止在运行期显式地调用System.gc()");
		paramMap.put("-verbose:gc", "输出虚拟机中GC的详细情况[Full GC 168K->97K(1984K)， 0.0253873 secs]");
		paramMap.put("-XX:+PrintGCDateStamps", "输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）");
		paramMap.put("-XX:+PrintGCDetails", "输出GC的详细日志");
		paramMap.put("-Xloggc", "gc日志产生的路径");
		
		paramMap.put("-Djava.endorsed.dirs", "指定的目录面放置的jar文件，将有覆盖系统API的功能");
		paramMap.put("-classpath", "类路径");
		
		paramMap.put("-Xss", "线程栈大小");
		paramMap.put("-XX:PermSize", "永久带初始值");
		paramMap.put("-XX:MaxPermSize", "永久带最大值");
		paramMap.put("-XX:MaxDirectMemorySize", "直接内存最大值");
		paramMap.put("-cp", "");
		paramMap.put("-n", "");
		paramMap.put("-XX:NewRatio", "新生代和年老代的堆内存占用比例, 例如2表示新生代占年老代的1/2");
		paramMap.put("-XX:NewSize", "新生代占整个堆内存的最大值");
		 
		
	}
	
	public static void main(String[] args) throws IOException {
		String[] params=readToLine("startup.param.txt").split(" ");
		for(int i=0;i<params.length;i++){
			String param=params[i];
			parseParam(param,i,params);
		}
		
	}
	
	public static void parseParam(String param,int idx,String[] params){
		
		if(StringUtils.isEmpty(param) || StringUtils.isNumeric(param)){
			//ignore 
			return ;
		}
		//FIXME 第一期先打印出来中文释义
		 
		for(String p:paramMap.keySet()){
			if (param.startsWith(p)){//匹配 
				 
				formatOut(param, paramMap.get(p) );
				return;
			} 
		} 
		
		formatOut(param, "unknown");
	} 
	
	public static void formatOut(String key,String desc){
		System.out.println(key+"|	"+desc );
	}
	public static String readToLine(String fileName) throws IOException{
		InputStream is=JvmStartUpParamParser.class.getResourceAsStream(fileName);
		return FileUtils.readWholeFileAsUTF8(is);
		
	}
}
