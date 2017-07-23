package cn.allchin.jvm.gc.makegc;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.util.FileUtils;

import cn.allchin.jvm.gc.log.JvmGcLogParser;

public class TrashGcParser {
	public static void parseFile(String file) throws IOException{
	 
		InputStream is=TrashGcParser.class.getResourceAsStream(file);
		String fileText= FileUtils.readWholeFileAsUTF8(is);
		JvmGcLogParser.parse(fileText);
	 
	}
	
	public static void main(String[] args) throws IOException {
		parseFile("TrashLog.txt");
	}
}
