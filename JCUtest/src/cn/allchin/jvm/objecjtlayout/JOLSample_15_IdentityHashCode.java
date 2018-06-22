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

**** Fresh object
cn.allchin.jvm.objecjtlayout.JOLSample_15_IdentityHashCode$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb d3 16 (11111000 11111011 11010011 00010110) (382991352)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

hashCode: 533ddba

**** After identityHashCode()
cn.allchin.jvm.objecjtlayout.JOLSample_15_IdentityHashCode$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 ba dd 33 (00000001 10111010 11011101 00110011) (870169089)
      4     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      8     4        (object header)                           f8 fb d3 16 (11111000 11111011 11010011 00010110) (382991352)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total


 */
public class JOLSample_15_IdentityHashCode {

    /*
     * The example for identity hash code.
     *
     * The identity hash code, once computed, should stay the same.
     * HotSpot opts to store the hash code in the mark word as well.
     * You can clearly see the hash code bytes in the header once
     * it was computed.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        out.println("**** Fresh object");
        out.println(layout.toPrintable());

        out.println("hashCode: " + Integer.toHexString(a.hashCode()));
        out.println();

        out.println("**** After identityHashCode()");
        out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }

}