package flight.ticketi.web.controller;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Created by kongzheng on 16/3/23.
 */
public class JavassistClassReplaceTest  {

	public static void main(String[] args) throws Exception {
		/**
		 * 如果已经被jvm加载过，再次ctClass.toClass();加载类会异常
		 * */
		//FooService fooService0 = new FooService();
		//System.out.println(fooService0.getClass());
		//
		ClassPool classPool = ClassPool.getDefault();
		CtClass ctClass = classPool.get("flight.ticketi.web.controller.FooService");
		CtMethod ctMethod = ctClass.getDeclaredMethod("foo");
		ctMethod.setBody("{System.out.println(\" \");\nreturn \"哈哈哈哈哈!\";}");
		Class c = ctClass.toClass();
		//
		FooService fooService = (FooService) c.newInstance();
		String result = fooService.foo();
		System.out.println("fs1|fooService" + result+"|"+fooService.getClass());

		//
		FooService fooService2 = new FooService();
		String result2 = fooService2.foo();
		System.out.println("result2=>" + result2+"|"+fooService2.getClass());
	}
}
