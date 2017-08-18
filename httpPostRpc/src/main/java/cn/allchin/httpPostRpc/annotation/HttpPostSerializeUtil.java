package cn.allchin.httpPostRpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.allchin.httpPostRpc.serialize.ProtocolUtil;

/**
 * 标记http rpc 方法使用的序列化协议工具
 * @author citi0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpPostSerializeUtil {
	 Class<? extends ProtocolUtil> value();
}
