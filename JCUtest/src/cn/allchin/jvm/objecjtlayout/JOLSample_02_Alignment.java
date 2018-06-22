package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 
 * 
 * # Running 64-bit HotSpot VM.
# Using compressed oop with 0-bit shift.
# Using compressed klass with 0-bit shift.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

cn.allchin.jvm.objecjtlayout.JOLSample_02_Alignment$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0    12        (object header)                           N/A
     12     4        (alignment/padding gap)                  
     16     8   long A.f                                       N/A
Instance size: 24 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total



 *
 */
public class JOLSample_02_Alignment {
	 /*
     * This is the more advanced field layout example.
     *
     * Because the underlying hardware platform often requires aligned accesses
     * to maintain the performance and correctness, it is expected the fields
     * are aligned by their size. For booleans it does not mean anything, but
     * for longs it's different. In this example, we can see the long field
     * is indeed aligned for 8 bytes, sometimes making the gap after the
     * object header.
     */

    public static void main(String[] args) throws Exception {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(A.class).toPrintable());
    }

    public static class A {
        long f;
    }
}
