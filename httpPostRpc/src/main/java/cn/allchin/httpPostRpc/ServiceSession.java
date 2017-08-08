package cn.allchin.httpPostRpc;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.qunar.base.meerkat.http.QunarHttpClient;
import com.qunar.base.meerkat.http.util.StringUtil;
import com.qunar.flight.nts.commons.xmlpost.HttpPostMethod.MethodSign;
import com.qunar.flight.qmonitor.QMonitor;

/**
 * �����������Ự
 * @author renxing.zhang
 *
 */
public class ServiceSession {
	private static QunarHttpClient client = QunarHttpClient.createDefaultClient(20000, 20000, 6000, 1000);
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * ֧������ģʽ��
	 * ��xmlUtil.serialise ���ص��ַ������ݲ�Ϊ�գ���ʹ��entry���ͣ�
	 * ����ʹ��postParameter����
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
			
			QMonitor.recordOne("xmlpost_request_success_" + methodName);
			return result;
		} catch (Exception e) {
			logger.error("ssPost2|�����쳣|", e);
			QMonitor.recordOne("xmlpost_request_error_" + methodName);
		}
		finally {
			long during=System.currentTimeMillis()-start;
			logger.info("xmlPost|{}|��ʱ|{}",methodName,during);
			QMonitor.recordOne("xmlpost_request_time_"+methodName, during);
		
		}
		return null;
	}

	public <T> T doPost(Object parameter, MethodSign method) throws ClientProtocolException, IOException {
		ProtocolUtil xmlUtil = method.getSerialiseUtil();
		String url = method.getUrl();
		// url��method����/ע��������

		HttpPost bodyRequest = new HttpPost(url);
		String xml = xmlUtil.serialise(parameter, bodyRequest);
		String contentType = bodyRequest.getLastHeader("Content-Type").getValue();

		if (logger.isDebugEnabled()) {
			logger.debug("ssPost1|׼������|url|{}|body|{}", url, xml);
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
			QMonitor.recordOne("seat_http_deseria_error");
			logger.error("ssD1|�����л��쳣",e);
		}
		return null;

	}
	
 

}
