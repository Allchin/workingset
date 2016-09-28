package cn.allchin.guava.rateLimiter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterWarmTest {
	public static void main(String[] args) {
		RateLimiter limiter=RateLimiter.create(5, 10, TimeUnit.SECONDS);
		
		while(true){
			for(int i=0;i<Integer.MAX_VALUE;i++){
				if(limiter.tryAcquire()){
					System.out.println(i+"|ok|"+new Date());  
					
				}
				 
			}
		 
			try {
				Thread.sleep(10*1000);
			} catch ( Exception e) { }
			
		}
	}
}
