package cn.allchin.workset.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class AsmAopExample extends ClassLoader implements Opcodes {
	
	
	public static void main(String[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, ClassNotFoundException {
		ClassReader cr=new ClassReader(Foo.class.getName());
		ClassWriter cw=new ClassWriter(cr,ClassWriter.COMPUTE_MAXS);
		
		ClassVisitor cv=new MethodChangeClassAdapter(cw);
		cr.accept(cv, Opcodes.ASM4); 
		
		byte[] code=cw.toByteArray();
		AsmAopExample loader=new AsmAopExample();
		System.out.println(Foo.class.getName());
		String classPath=Foo.class.getName().replaceAll("\\u002e", "/");
 
		loader.findClass(Foo.class.getName());
		
		Class<?> exampleClass=loader.defineClass( Foo.class.getName(), code, 0,code.length);
		
		for(Method method:exampleClass.getMethods()) {
			System.out.println(method);
		}
		
		exampleClass.getMethods()[0].invoke(null, null);//调用executor
		
		FileOutputStream fos=new FileOutputStream("E:\\temp");
		
		fos.write(code);
		fos.close();
				
	}
}
