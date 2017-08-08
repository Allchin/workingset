package cn.allchin.httpPostRpc;

import org.apache.http.client.methods.HttpPost;

/**
 * <pre>
 * ְ�����л�����http��ҵ��Э�����κͷ��ؽ��
 * 
 * </pre>
 * TAT xml���л������鷳��һ���ֶβ��Ծ͹���
 * @author renxing.zhang
 *
 */
public interface ProtocolUtil {
	/**
	 * ��http�����������л������ݲ�ͬϵͳ����Э�飬�Լ�ʵ��ҵ���������л�ϸ��
	 * 
	 * @param obj  ���͸��Է��Ķ���
	 * @param postParameter һ����������http ɽ���ĵ����ͣ�post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"); 
	 * @return  ���ݸ��Է�������body���֣����԰���ҵ��Э��ϸ�ڣ����л��� json ��xml[����ws],key=value[����http post]
	 */
	public   String serialise(Object obj,HttpPost postParameter  ) ;
	/**
	 * ���Է����ص����ݷ����е�����
	 * @param str
	 * @param clazz
	 * @return
	 */
	public   <T> T deserialize(String str, Class<T> clazz);
 
	
}
