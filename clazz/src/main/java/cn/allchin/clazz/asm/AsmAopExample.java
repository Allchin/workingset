package cn.allchin.clazz.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class AsmAopExample {

	public static void main(String[] args) throws IOException, IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		// 对需要侵入的类 生命一个读取器
		String targetClass = Foo.class.getName();
		ClassReader cr = new ClassReader(targetClass);

		// 声明写出方式
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);

		// 让类访问器使用写出器
		ClassVisitor cv = new MethodChangeClassAdapter(cw);

		
		System.out.println("// 让类访问器，使用类读取器，读取它侵入的类");
		cr.accept(cv, Opcodes.ASM4);

		
		System.out.println("// 将侵入的类写出到数组");
		byte[] code = cw.toByteArray();

		// 类加载器
		AsmClassLoader loader = new AsmClassLoader();

		
		System.out.println("targetClass|" + targetClass);
		
		
		System.out.println("//使用自定义加载器加载被侵入的类");
		Class<?> exampleClass = loader.defineClass0(targetClass, code, 0, code.length);

		for (Method method : exampleClass.getMethods()) {
			System.out.println("被入侵的类|method|"+method);
		}
		System.out.println("调用被入侵的类的被入侵方法|execute");
		exampleClass.getMethod("execute", null).invoke(null, null);
		 
		// gets the bytecode of the Example class, and loads it dynamically

		FileOutputStream fos = new FileOutputStream("E:\\temp\\Example.class");
		fos.write(code);
		fos.close();
		
 
	}

}