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

cn.allchin.jvm.objecjtlayout.JOLSample_09_Contended$B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0    12        (object header)                           N/A
     12     4    int A.a                                       N/A
     16     4    int A.b                                       N/A
     20     4    int A.c                                       N/A
     24     4    int A.d                                       N/A
     28     4    int B.e                                       N/A
Instance size: 32 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total


 */
public class JOLSample_09_Contended {

    /*
     * This is an example of special annotations that can affect the field layout.
     * (This example requires JDK 8 to run, -XX:-RestrictContended should also be used)
     *
     * In order to dodge false sharing, users can put the @Contended annotation
     * on the selected fields/classes. The conservative effect of this annotation
     * is laying out the fields at sparse offsets, effectively providing the
     * artificial padding.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());
        out.println(ClassLayout.parseClass(B.class).toPrintable());
    }

    public static class A {
                             int a;
                             int b;
        @sun.misc.Contended  int c;
                             int d;
    }

    public static class B extends A {
        int e;
    }

}