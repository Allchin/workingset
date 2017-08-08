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
public   class Monitor {
		static long start = 0;

		public static void start() {
			start = System.currentTimeMillis();

		}

		public static void end() {
			long end = System.currentTimeMillis();
			System.out.println("方法执行时间" + (end - start));
		}
	}

	