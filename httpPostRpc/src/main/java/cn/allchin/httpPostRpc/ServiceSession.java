package cn.allchin.httpPostRpc;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qunar.base.meerkat.http.util.StringUtil;
import com.google.common.base.Charsets;
import com.qunar.base.meerkat.http.QunarHttpClient;
 

import cn.allchin.httpPostRpc.proxy.HttpPostMethod.MethodSign;
import cn.allchin.httpPostRpc.serialize.ProtocolUtil;

/**
 * 向服务器发起会话
 * @author renxing.zhang
 *
 */
public class ServiceSession {
	private static QunarHttpClient client = QunarHttpClient.createDefaultClient(20000, 20000, 6000, 1000);
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 支持两种模式，
	 * 当xmlUtil.serialise 返回的字符串内容不为空，就使用entry发送，
	 * 否则，使用postParameter发送
	 * 
	 * 
	 * @param parameter
	 * @param method
	 * @return
	 */
	public <T> T post(Object parameter, MethodSign method) {
		String methodName = null;
		long start=System.currentTimeMillis();
		try {
			methodName = method.getMethod().getName();
			T result = doPost(parameter, method);
		 
			return result;
		} catch (Exception e) {
			logger.error("ssPost2|请求异常|", e);
			 
		}
		finally {
			long during=System.currentTimeMillis()-start;
			logger.info("xmlPost|{}|耗时|{}",methodName,during);
			 
		
		}
		return null;
	}

	public <T> T doPost(Object parameter, MethodSign method) throws ClientProtocolException, IOException {
		ProtocolUtil xmlUtil = method.getSerialiseUtil();
		String url = method.getUrl();
		// url从method配置/注解上面拿

		HttpPost bodyRequest = new HttpPost(url);
		String xml = xmlUtil.serialise(parameter, bodyRequest);
		String contentType = bodyRequest.getLastHeader("Content-Type").getValue();

		if (logger.isDebugEnabled()) {
			logger.debug("ssPost1|准备请求|url|{}|body|{}", url, xml);
		}

		String requestBody = xml;
		String xmlResp = null;

		bodyRequest.setEntity(new StringEntity(requestBody));
		bodyRequest.setHeader("Content-Type", contentType); 
		
		HttpResponse response = client.execute(bodyRequest);

		xmlResp = StringUtil.toString(response.getEntity(), Charsets.UTF_8.toString());
		EntityUtils.consumeQuietly(response.getEntity());

		logger.debug("ssPost3|response|{}", xmlResp);

		try {
			T result = (T) xmlUtil.deserialize(xmlResp, method.getReturnType());
			
			return result;	
		} catch (Exception e) {
			 
			logger.error("ssD1|反序列化异常",e);
		}
		return null;

	}
	
 

}
