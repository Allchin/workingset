package cn.allchin.workset.asm;
public class Foo {
		public static void execute() {
			System.out.println("execute()");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		}

	}

	