package cn.allchin.httpPostRpc;

import cn.allchin.httpPostRpc.annotation.HttpPostApi;
import cn.allchin.httpPostRpc.annotation.HttpPostCall;
import cn.allchin.httpPostRpc.serialize.support.VoidUtil;
 

@HttpPostApi
public interface VoidReturnServcie {
	@HttpPostCall(url = "http://www.baidu.com")
	@cn.allchin.httpPostRpc.annotation.HttpPostSerializeUtil(VoidUtil.class)
	public String visit(String str) ;
}
