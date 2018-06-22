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

cn.allchin.jvm.objecjtlayout.JOLSample_06_Gaps$C object internals:
 OFFSET  SIZE      TYPE DESCRIPTION                               VALUE
      0    12           (object header)                           N/A
     12     1   boolean A.a                                       N/A
     13     3           (alignment/padding gap)                  
     16     1   boolean B.b                                       N/A
     17     3           (alignment/padding gap)                  
     20     1   boolean C.c                                       N/A
     21     3           (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 6 bytes internal + 3 bytes external = 9 bytes total


 */
public class JOLSample_06_Gaps {

    /*
     * This example shows another HotSpot layout quirk.
     *
     * HotSpot rounds up the instance field block up to reference size.
     * That unfortunately yields the artificial gaps at the end of the class.
     *
     * See also:
     *    https://bugs.openjdk.java.net/browse/JDK-8024912
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());
        out.println(ClassLayout.parseClass(C.class).toPrintable());
    }

    public static class A {
        boolean a;
    }

    public static class B extends A {
        boolean b;
    }

    public static class C extends B {
        boolean c;
    }

}