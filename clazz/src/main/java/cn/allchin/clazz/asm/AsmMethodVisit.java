package cn.allchin.clazz.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
 
/**
 * 
 * @author citi0
 *
 */
public class AsmMethodVisit extends MethodVisitor {

		public AsmMethodVisit(MethodVisitor mv) {
			super(Opcodes.ASM4, mv);
		}
		private String className="cn/allchin/clazz/asm/MethodRunMonitor";
		
		/**
		 * <pre>
		 * Foo 里面导入了默认包，所以将MethodRunMonitor 放在默认包下面是可以访问到的，但是
		 * 带了包名字就访问不到了。。。
		 * 所以类要使用全限定名字 
		 * 
		 * 
		 * 
		 * ClassFormatError: Illegal class name "cn.allchin.clazz.asm.MethodRunMonitor" in class file cn/allchin/clazz/asm/Foo
    错误的写法
        visitMethodInsn(Opcodes.INVOKESTATIC, MethodRunMonitor.class.getName(), "start", "()V");

    正确的写法
        private String className="cn/allchin/clazz/asm/MethodRunMonitor";
        visitMethodInsn(Opcodes.INVOKESTATIC,className, "end", "()V");
    机智的写法
        visitMethodInsn(Opcodes.INVOKESTATIC, MethodRunMonitor.class.getName().replaceAll("\\.", "/"), "start", "()V");
</pre>
		 */
		@Override
		public void visitCode() {
			//此方法尽在访问方法头部时被访问一次
			System.out.println("AsmMethodVisit|visitCode|修改方法头部");
			visitMethodInsn(Opcodes.INVOKESTATIC, MethodRunMonitor.class.getName().replaceAll("\\.", "/"), "start", "()V");
			super.visitCode();
		}
		@Override
		public void visitInsn(int opcode) {
			//方法返回之前添加新的指令
			if(opcode == Opcodes.RETURN) {
				System.out.println("AsmMethodVisit|visitInsn|修改方法尾巴");
				visitMethodInsn(Opcodes.INVOKESTATIC,className, "end", "()V");
			}
			super.visitInsn(opcode);
		}

	}