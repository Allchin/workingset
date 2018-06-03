package cn.allchin.clazz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class HelloWorldGenerator {
	public static void main(String[] args) {
		HelloWorldGenerator generator =new HelloWorldGenerator();
		ClassWriter cw= generator.generatCode();
		generator.writeToFile(cw);
		
		
		
	}
	
	public ClassWriter generatCode() {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, "HelloWorld", null, "java/lang/Object", null);

		// visitMethod() 方法创建构造器
		MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "", "()V", null, null);

		// visitCode() 方法生成构造器体
		constructor.visitCode();

		constructor.visitVarInsn(Opcodes.ALOAD, 0);
		constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		constructor.visitInsn(Opcodes.RETURN);

		constructor.visitMaxs(0, 0);
		constructor.visitEnd();
		//main 方法
		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "(Ljava/lang/String;)V",
				null, null);
		mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

		mv.visitLdcInsn("hello,world!");
		mv.visitMethodInsn(Opcodes.GETSTATIC, "java/lang/System", "println", "Ljava/io/PrintStream;");
		mv.visitInsn(Opcodes.RETURN);

		mv.visitMaxs(0, 0);
		mv.visitEnd();
		return cw;
	}

	private void writeToFile(ClassWriter cw) {
		byte[] data = cw.toByteArray();
		File file = new File("D://Comparable.class");
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(file);
			fout.write(data);
			fout.close();
		} catch  (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
