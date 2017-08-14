package cn.allchin.clazz.asm;

public  class MethodRunMonitor {
		public static long start = 0;

		public static void start() {
		System.out.println("MethodRunMonitor|start");
			start = System.currentTimeMillis();

		}

		public static void end() {
			long end = System.currentTimeMillis();
			System.out.println("MethodRunMonitor|end|方法执行时间" + (end - start));
		}
	}
