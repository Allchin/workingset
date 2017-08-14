package cn.allchin.clazz.asm;


public class Foo {
	public static void execute() {
		System.out.println("Foo|execute()");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

}
