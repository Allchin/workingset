package cn.allchin.httpPostRpc.serialize;

import org.apache.http.client.methods.HttpPost;

/**
 * <pre>
 * 职责，序列化基于http的业务协议的入参和返回结果
 * 
 * </pre>
 * TAT xml序列化真心麻烦，一个字段不对就跪了
 * @author renxing.zhang
 *
 */
public interface ProtocolUtil {
	/**
	 * 对http参数进行序列化，根据不同系统交互协议，自己实现业务数据序列化细节
	 * 
	 * @param obj  发送给对方的对象
	 * @param postParameter 一般用来设置http 山下文的类型，post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"); 
	 * @return  传递给对方的请求body部分，可以按照业务协议细节，序列化成 json ，xml[类似ws],key=value[类似http post]
	 */
	public   String serialise(Object obj,HttpPost postParameter  ) ;
	/**
	 * 将对方返回的内容反序列到对象
	 * @param str
	 * @param clazz
	 * @return
	 */
	public   <T> T deserialize(String str, Class<T> clazz);
 
	
}
