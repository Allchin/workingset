package cn.allchin.jvm.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.util.FileUtils;

/**
 * ����  ����jvm����
 * @author renxing.zhang
 *
 */
public class JvmStartUpParamParser {
	public static Map<String,String> paramMap=new HashMap();
	static{
 
		paramMap.put("-Xms", "��ʼ���ڴ�");
		paramMap.put("-Xmx", "�����ڴ�");
		
		
		paramMap.put("-server", "������ģʽ");
		paramMap.put("-XX:+DisableExplicitGC", "��ֹ����������ʽ�ص���System.gc()");
		paramMap.put("-verbose:gc", "����������GC����ϸ���[Full GC 168K->97K(1984K)�� 0.0253873 secs]");
		paramMap.put("-XX:+PrintGCDateStamps", "���GC��ʱ����������ڵ���ʽ���� 2013-05-04T21:53:59.234+0800��");
		paramMap.put("-XX:+PrintGCDetails", "���GC����ϸ��־");
		paramMap.put("-Xloggc", "gc��־������·��");
		
		paramMap.put("-Djava.endorsed.dirs", "ָ����Ŀ¼����õ�jar�ļ������и���ϵͳAPI�Ĺ���");
		paramMap.put("-classpath", "��·��");
		
		paramMap.put("-Xss", "�߳�ջ��С");
		paramMap.put("-XX:PermSize", "���ô���ʼֵ");
		paramMap.put("-XX:MaxPermSize", "���ô����ֵ");
		paramMap.put("-XX:MaxDirectMemorySize", "ֱ���ڴ����ֵ");
		paramMap.put("-cp", "");
		paramMap.put("-n", "");
		paramMap.put("-XX:NewRatio", "�����������ϴ��Ķ��ڴ�ռ�ñ���, ����2��ʾ������ռ���ϴ���1/2");
		paramMap.put("-XX:NewSize", "������ռ�������ڴ�����ֵ");
		
		paramMap.put("", "");
		paramMap.put("", "");
		paramMap.put("", "");
		
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
		//FIXME ��һ���ȴ�ӡ������������
		for(String p:paramMap.keySet()){
			if (param.startsWith(p)){//ƥ��
				
				System.out.println(param+"|"+paramMap.get(p));
			};
		} 
	} 
	
	public static String readToLine(String fileName) throws IOException{
		InputStream is=JvmStartUpParamParser.class.getResourceAsStream(fileName);
		return FileUtils.readWholeFileAsUTF8(fileName);
		
	}
}
