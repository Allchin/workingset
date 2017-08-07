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
public class AsmMethodVisit extends MethodVisitor {

		public AsmMethodVisit(MethodVisitor mv) {
			super(Opcodes.ASM4, mv);
		}

		@Override
		public void visitCode() {
			//此方法尽在访问方法头部时被访问一次
			visitMethodInsn(Opcodes.INVOKESTATIC, Monitor.class.getName(), "start", "()V");
			super.visitCode();
		}
		@Override
		public void visitInsn(int opcode) {
			//方法返回之前添加新的指令
			if(opcode == Opcodes.RETURN) {
				visitMethodInsn(Opcodes.INVOKESTATIC, Monitor.class.getName(), "end", "()V");
			}
			super.visitInsn(opcode);
		}

	}