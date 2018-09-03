package cn.allchin.jvm.objecjtlayout.digest;

import org.openjdk.jol.info.ClassLayout;

import com.tdunning.math.stats.AVLTreeDigest;
import com.tdunning.math.stats.TDigest;

public class DegistMemTest {
	public static void main(String[] args) {
		new DegistMemTest().howManyMem();

	}

	public void howManyMem() {
		AVLTreeDigest obj = testPercentRecord();

		//System.out.println(ClassLayout.parseClass(AVLTreeDigest.class).toPrintable());
		//System.out.println(ClassLayout.parseClass(AVLTreeDigest.class).toPrintable(obj));
		 
	 
	}

	public AVLTreeDigest testPercentRecord() {

		AVLTreeDigest tdi = (AVLTreeDigest) TDigest.createAvlTreeDigest(10);
		// 加点
		for (int i = 0; i < 5*60*1000; i++) {
			tdi.add(2*i+1);
		}
		for (int i = 0; i < 300; i++) {
			tdi.add(3*i+1);
			tdi.add(4*i+1);
			tdi.add(5*i+1);

		}
		System.out.println(tdi.toString());
	 

		System.out.println("点数:" + tdi.centroids().size());
		System.out.println("压缩率：" + tdi.compression());
		System.out.println("90 cdf |" + tdi.cdf(90));
		System.out.println("p95|" + tdi.quantile(0.95));
		tdi.compress();
		System.out.println("压缩后点数:" + tdi.centroids().size());
	 

		//
		System.out.println(tdi.cdf(90));
		System.out.println("p95|" + tdi.quantile(0.95));
		System.out.println("p90|" + tdi.quantile(0.90));
		System.out.println("p99|" + tdi.quantile(0.99));
		System.out.println(tdi.quantile(0.75));
		//
		tdi.add(0);
		return tdi;
	}

}
