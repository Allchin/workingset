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

cn.allchin.jvm.objecjtlayout.JOLSample_11_ClassWord$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb ca 16 (11111000 11111011 11001010 00010110) (382401528)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

cn.allchin.jvm.objecjtlayout.JOLSample_11_ClassWord$B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           78 08 cb 16 (01111000 00001000 11001011 00010110) (382404728)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total


 */
public class JOLSample_11_ClassWord {

    /*
     * This is the example to have insight into object headers.
     *
     * In HotSpot, object header consists of two parts: mark word,
     * and class word. We can clearly see the class word by analysing
     * two syntactically equivalent instances of two distinct classes.
     * See the difference in headers, that difference is the reference
     * to class.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());
        out.println(ClassLayout.parseInstance(new A()).toPrintable());
        out.println(ClassLayout.parseInstance(new B()).toPrintable());
    }

    public static class A {
        // no fields
    }

    public static class B {
        // no fields
    }

}