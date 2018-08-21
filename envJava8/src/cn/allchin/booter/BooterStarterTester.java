package cn.allchin.booter;

import java.sql.Driver;
import java.util.ServiceLoader;

public class BooterStarterTester {
	public static void main(String[] args) {
		try {
	        ServiceLoader<Driver> s = ServiceLoader.load(Driver.class);  
	        s.iterator().next();//才会访问到构造方法
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
