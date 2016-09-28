package cn.allchin.guava.rateLimiter;

import java.util.Date;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {
	public static void main(String[] args) {
		RateLimiter  limiter=RateLimiter.create(0.33);//qps
		while(true){
			limiter.acquire();
			System.out.println("cool"+new Date()+"|"+limiter);
		}
		 
	}
}
