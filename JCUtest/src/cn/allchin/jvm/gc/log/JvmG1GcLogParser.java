package cn.allchin.jvm.gc.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.util.FileUtils;

/**
 * G1 GC日志分析工具
 * 
 * @author renxing.zhang
 * 
 *
 */
public class JvmG1GcLogParser {
	public static void main(String[] args) throws IOException {

		parseFile("g1GcLog.txt");
	}

	public static void parseFile(String file) throws IOException {
		String fileText = JvmG1GcLogParser.readToLine(file);
		String[] g1Logs = StringUtils.split(fileText, "[");
		JvmG1GcLogParser.parse(g1Logs);
	}

	public static void parse(String fileText) {
		String[] g1Logs = StringUtils.split(fileText, "[");
		JvmG1GcLogParser.parse(g1Logs);
	}

	public static void parse(String[] g1Logs) {
		for (int i = 0; i < g1Logs.length; i++) {
			String line = g1Logs[i];
			if (StringUtils.isEmpty(line)) {
				continue;
			}
			formatOut("-----------------------------");
			formatOut(line);

			for (String key : constantsMap.keySet()) {

				String desc = constantsMap.get(key);
				//
				if (line.startsWith(key)) {
					formatOut(desc);
				}
			}
			if (line.startsWith("Eden:")) {
				parseRegionChange(line);
			}
			if (line.startsWith("Times:")) {
				parseTime(line);
			}
		}

	}

	public static void parseRegionChange(String str) {
		String[] regionChanges = StringUtils.split(str, ":()-> ");
		for (int i = 0; i < regionChanges.length; i++) {
			String region = StringUtils.trim(regionChanges[i]);
			switch (region) {
			case "Eden":
				formatOut(region + "|gc前使用量|" + regionChanges[i + 1]);
				formatOut(region + "|gc前容量|" + regionChanges[i + 2]);
				formatOut(region + "|gc后使用量|" + regionChanges[i + 3]);
				formatOut(region + "|gc后容量|" + regionChanges[i + 4]);
				break;
			case "Survivors":
				formatOut(region + "|gc前使用量|" + regionChanges[i + 1]);
				formatOut(region + "|gc前容量|" + regionChanges[i + 2]);
				break;
			case "Heap":
				formatOut(region + "|gc前使用量|" + regionChanges[i + 1]);
				formatOut(region + "|gc前容量|" + regionChanges[i + 2]);
				formatOut(region + "|gc后使用量|" + regionChanges[i + 3]);
				formatOut(region + "|gc后容量|" + regionChanges[i + 4]);
				break;
			}
		}

	}

	public static void parseTime(String str) {
		formatOut(str);
	}

	public static void formatOut(String str) {
		System.out.println(str);

	}

	public static Map<String, String> constantsMap = new HashMap<String, String>();

	static {
		
		constantsMap.put("Parallel Time", "并行GC耗时");
		constantsMap.put("GC Worker Start", "每个工作线程启动时间，时间根据线程id排序,所有工作线程启动时间的平均值、最小值、最大值、差别");
		constantsMap.put("Ext Root Scanning", "每个扫描root的线程耗时");
		constantsMap.put("Update RS", "每个执行更新RS（Remembered Sets）的线程的耗时");
		constantsMap.put("Processed Buffers", "每个工作线程执行UB（Update Buffers）的数量");

		constantsMap.put("Scan RS", "每个工作线程扫描RS的耗时");
		constantsMap.put("Object Copy", "每个工作线程执行OC（Object Copy）的耗时");
		constantsMap.put("Termination (", "每个工作线程执行终止的耗时");
		constantsMap.put("Termination Attempts", "每个工作线程执行终止的重试的次数");

		constantsMap.put("Clear CT", "清理CT（Card Table）的耗时");
		constantsMap.put("Other", "其他任务（上述未统计的内容）的耗时");
		constantsMap.put("Choose CSet", "选择分区的耗时");
		constantsMap.put("Ref Proc", "执行关联（Reference objects）的耗时");
		constantsMap.put("Ref Enq", "将references放入ReferenceQueues的耗时");

		constantsMap.put("Free CSet", "释放CS（collection set）的耗时");


		
		constantsMap.put("GC Worker Total", "unknow");
		constantsMap.put("GC Worker End", "每个工作线程终止时的时间");
	 
		constantsMap.put("GC Worker (", "每个工作线程的生命时间");

		constantsMap.put("GC Worker Other", "每个工作线程执行其他任务（上述未统计的内容）的耗时");
		constantsMap.put("Code Root Scanning", "unknow");
		constantsMap.put("Code Root Fixup", "unknow");
		constantsMap.put("Code Root Purge", "unknow");
		constantsMap.put("Redirty Cards", "unknow");
		constantsMap.put("Humongous Register", "unknow");
		constantsMap.put("Humongous Reclaim", "巨型对象收集");
		
		constantsMap.put("GC concurrent-root-region-scan-start", "并发根分区扫描开始");
		constantsMap.put("GC concurrent-root-region-scan-end", "并发根分区扫描结束");
		constantsMap.put("GC concurrent-mark-start", "并发标记开始");
		constantsMap.put("GC concurrent-mark-end", "并发标记结束");
		constantsMap.put("GC remark", "标记");
		
		constantsMap.put("Finalize Marking", "最终标记");
		constantsMap.put("GC ref-proc", "unknow");
		constantsMap.put("Unloading", "unknow");
		constantsMap.put("GC cleanup", "GC清理阶段");
	 
		constantsMap.put("GC pause (young)", "发生一个Evacuation Pause");
		constantsMap.put("GC pause (G1 Humongous Allocation) (young) (initial-mark)", "年轻带初始化标记，举行对象分配");
		
		constantsMap.put("Code Root Scanning", "根分区扫描");
		constantsMap.put("Full GC (Allocation Failure)", "FullGC");
		constantsMap.put("Metaspace", "元数据");
		 

	}

	public static String readToLine(String fileName) throws IOException {
		InputStream is = JvmG1GcLogParser.class.getResourceAsStream(fileName);
		return FileUtils.readWholeFileAsUTF8(is);

	}

}
