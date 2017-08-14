package cn.allchin.clazz.asm;

import org.objectweb.asm.Opcodes;

public class AsmClassLoader extends ClassLoader   {
	 public final Class<?> defineClass0(String name, byte[] b, int off, int len){
		return  defineClass(name, b, off, len);
	 }
}
