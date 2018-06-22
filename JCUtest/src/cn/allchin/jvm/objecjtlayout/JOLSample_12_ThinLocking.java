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
cn.allchin.jvm.objecjtlayout.JOLSample_12_ThinLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb df 16 (11111000 11111011 11011111 00010110) (383777784)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** With the lock
cn.allchin.jvm.objecjtlayout.JOLSample_12_ThinLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           d8 f4 a1 02 (11011000 11110100 10100001 00000010) (44168408)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb df 16 (11111000 11111011 11011111 00010110) (383777784)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** After the lock
cn.allchin.jvm.objecjtlayout.JOLSample_12_ThinLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb df 16 (11111000 11111011 11011111 00010110) (383777784)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total



 */
public class JOLSample_12_ThinLocking {

    /*
     * This is another dive into the mark word.
     *
     * Among other things, mark words store locking information.
     * We can clearly see how the mark word contents change when
     * we acquire the lock, and release it subsequently.
     *
     * This one is the example of thin (displaced) lock. The data
     * in mark word when lock is acquired is the reference to the
     * displaced object header, allocated on stack. Once we leave
     * the lock, the displaced header is discarded, and mark word
     * is reverted to the default value.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        out.println("**** Fresh object");
        out.println(layout.toPrintable());

        synchronized (a) {
            out.println("**** With the lock");
            out.println(layout.toPrintable());
        }

        out.println("**** After the lock");
        out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }

}