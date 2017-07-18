package flight.ticketi.web.controller;

public class FooService {
	public String foo(){
		return ("foo1|@|fooService");
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t=new Thread(){
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) { }
			};
		};
		t.setDaemon(true);
		t.start();
		System.out.println("1");
		 
		System.out.println("2");
		
	}
}
