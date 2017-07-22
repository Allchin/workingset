package cn.allchin.jvm.gc.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.util.FileUtils;

/**
 * G1 GC��־��������
 * @author renxing.zhang
 *   
 *
 */
public class JvmGcLogParser {
	public static void main(String[] args) throws IOException {
		String fileText=JvmGcLogParser.readToLine("g1GcLog.txt");
	
		String[] g1Logs= StringUtils.split(fileText, "[");
		JvmGcLogParser.parse(g1Logs);
	}
	public static void parse(String[] g1Logs){
		for(int i=0;i<g1Logs.length;i++){
			String line=g1Logs[i];
			if(StringUtils.isEmpty(line)){
				continue;
			}
			formatOut("-----------------------------");
			formatOut(line);
		 
			
			
			for(String key:constantsMap.keySet()){
				
				String desc=constantsMap.get(key);
				//
				if(line.startsWith(key)){
					formatOut(desc);
				} 
			}
			if(line.startsWith("Eden:")){
				parseRegionChange(line);
			}
			if(line.startsWith("Times:")){
				parseTime(line);
			}
		}
		
	}
	public static void parseRegionChange(String str){
		String[] regionChanges=StringUtils.split(str, ":()-> ");
		for(int i=0;i<regionChanges.length;i++){
			String region=StringUtils.trim( regionChanges[i]);
			switch(region){
			case "Eden":
				formatOut(region+"|gcǰʹ����|"+regionChanges[i+1]);
				formatOut(region+"|gcǰ����|"+regionChanges[i+2]);
				formatOut(region+"|gc��ʹ����|"+regionChanges[i+3]);
				formatOut(region+"|gc������|"+regionChanges[i+4]);
				break;
			case "Survivors":
				formatOut(region+"|gcǰ����|"+regionChanges[i+1]);
				formatOut(region+"|gcǰ����|"+regionChanges[i+2]);
				break;
			case "Heap":
				formatOut(region+"|gcǰʹ����|"+regionChanges[i+1]);
				formatOut(region+"|gcǰ����|"+regionChanges[i+2]);
				formatOut(region+"|gc��ʹ����|"+regionChanges[i+3]);
				formatOut(region+"|gc������|"+regionChanges[i+4]);
				break;
			}
		}
	 
		
		
	}
	public static void parseTime(String str){
		formatOut(str);
	}
	
	public static void formatOut(String str){
		System.out.println(str);
	 
	}
	
	public static Map<String,String> constantsMap=new HashMap<String,String>();
	static{
		constantsMap.put("GC pause (young)", "����һ��Evacuation Pause");
		constantsMap.put("Parallel Time", "����GC��ʱ");
		constantsMap.put("GC Worker Start", "ÿ�������߳�����ʱ�䣬ʱ������߳�id����,���й����߳�����ʱ���ƽ��ֵ����Сֵ�����ֵ�����");
		constantsMap.put("Ext Root Scanning", "ÿ��ɨ��root���̺߳�ʱ");
		constantsMap.put("Update RS", "ÿ��ִ�и���RS��Remembered Sets�����̵߳ĺ�ʱ");
		constantsMap.put("Processed Buffers", "ÿ�������߳�ִ��UB��Update Buffers��������");
		
		constantsMap.put("Scan RS", "ÿ�������߳�ɨ��RS�ĺ�ʱ");
		constantsMap.put("Object Copy", "ÿ�������߳�ִ��OC��Object Copy���ĺ�ʱ");
		constantsMap.put("Termination (", "ÿ�������߳�ִ����ֹ�ĺ�ʱ");
		constantsMap.put("Termination Attempts", "ÿ�������߳�ִ����ֹ�����ԵĴ���");
		constantsMap.put("GC Worker End (", "ÿ�������߳���ֹʱ��ʱ��");
		constantsMap.put("GC Worker (", "ÿ�������̵߳�����ʱ��");
		
		constantsMap.put("GC Worker Other", "ÿ�������߳�ִ��������������δͳ�Ƶ����ݣ��ĺ�ʱ");
		constantsMap.put("Clear CT", "����CT��Card Table���ĺ�ʱ");
		constantsMap.put("Other", "������������δͳ�Ƶ����ݣ��ĺ�ʱ");
		constantsMap.put("Choose CSet", "ѡ������ĺ�ʱ");
		constantsMap.put("Ref Proc", "ִ�й�����Reference objects���ĺ�ʱ");
		constantsMap.put("Ref Enq", "��references����ReferenceQueues�ĺ�ʱ");
		
		constantsMap.put("Free CSet", "�ͷ�CS��collection set���ĺ�ʱ");
		 
		
	}
	
	public static String readToLine(String fileName) throws IOException{
		InputStream is=JvmGcLogParser.class.getResourceAsStream(fileName);
		return FileUtils.readWholeFileAsUTF8(is);
		
	}
	
	
}
