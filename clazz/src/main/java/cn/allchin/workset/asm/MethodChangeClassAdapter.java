package cn.allchin.workset.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodChangeClassAdapter extends ClassVisitor implements Opcodes {

		public MethodChangeClassAdapter(final ClassVisitor cv) {
			super(Opcodes.ASM4, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if ("execute".equals(name)) {
				MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);// 先得到最原始的方法
				MethodVisitor newMethod = null;
				newMethod = new AsmMethodVisit(mv);
				return newMethod;
			}
			
			return null;
		}
	}

	