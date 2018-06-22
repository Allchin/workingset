package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import static java.lang.System.out;

/**
 * <pre>
 * @author Aleksey Shipilev
 * 
 * # Running 64-bit HotSpot VM.
# Using compressed oop with 0-bit shift.
# Using compressed klass with 0-bit shift.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

cn.allchin.jvm.objecjtlayout.JOLSample_05_InheritanceBarrier$C object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0    12        (object header)                           N/A
     12     4        (alignment/padding gap)                  
     16     8   long A.a                                       N/A
     24     8   long B.b                                       N/A
     32     8   long C.c                                       N/A
     40     4    int C.d                                       N/A
     44     4        (loss due to the next object alignment)
Instance size: 48 bytes
Space losses: 4 bytes internal + 4 bytes external = 8 bytes total


 */
public class JOLSample_05_InheritanceBarrier {

    /*
     * This example shows the HotSpot field layout quirk.
     * (Works best with 64-bit VMs)
     *
     * Even though we have the alignment gap before A.a field, HotSpot
     * does not claim it, because it does not track the gaps in the
     * already laid out superclasses. This yields the virtual
     * "inheritance barrier" between super- and sub-class fields blocks.
     *
     * See also:
     *    https://bugs.openjdk.java.net/browse/JDK-8024913
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());
        out.println(ClassLayout.parseClass(C.class).toPrintable());
    }

    public static class A {
        long a;
    }

    public static class B extends A {
        long b;
    }

    public static class C extends B {
        long c;
        int d;
    }

}