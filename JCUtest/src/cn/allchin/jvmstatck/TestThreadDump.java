package cn.allchin.jvmstatck;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import sun.tools.attach.HotSpotVirtualMachine;
import java.io.InputStream;

public class TestThreadDump {
	private static final int band=998;
	private String from ="japan";
	
	public static void dumpStream(InputStream is) throws Exception {
		byte[] b = new byte[128];
		int n;

		do {
			n = is.read(b);
			if (n > 0) {
				String s = new String(b, "UTF-8");
				System.out.print(s);
			}
		} while (n > 0);
	}

	public static void main(String[] args) {
		HotSpotVirtualMachine vm = null;

		if (args.length == 0) {
			System.out.println("Usage:TestThreadDump pid");
			System.out.println("Choose following process:");
			// jps
			for (VirtualMachineDescriptor jp : VirtualMachine.list()) {
				System.out.println(jp.id() + ":" + jp.displayName());
			}
			return;
		}

		try {
			vm = (HotSpotVirtualMachine) VirtualMachine.attach(args[0]);
			// jstack
			InputStream ins = vm.remoteDataDump("-1");
			dumpStream(ins);

			// jmap -dump
			ins = vm.dumpHeap("stack.dmp");
			dumpStream(ins);
		} catch (Exception e) {
			System.err.println("attach failed");
		} finally {
			try {
				if (vm != null) {
					vm.detach();
				}
			} catch (Exception e) {
			}
		}
	}
}