package cn.allchin.httpPostRpc.serialize.support;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializationFeature;

import cn.allchin.httpPostRpc.serialize.ProtocolUtil;

 

/**
 * jackson 的xml序列化工具，感觉行为比较奇怪
 * @author renxing.zhang
 *
 */
public class VoidUtil implements ProtocolUtil {
	  
	public String serialise(Object obj,HttpPost request) {
		request.setHeader("Content-Type", "text/xml; charset=utf-8");
 
		return obj+"";
	}
 
	public <T> T deserialize(String str, Class<T> clazz) {
		 
		return null;
	}
 

	 

}
