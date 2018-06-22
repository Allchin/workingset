package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.TimeUnit;

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
cn.allchin.jvm.objecjtlayout.JOLSample_13_BiasedLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb cb 16 (11111000 11111011 11001011 00010110) (382467064)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** With the lock
cn.allchin.jvm.objecjtlayout.JOLSample_13_BiasedLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 80 41 02 (00000101 10000000 01000001 00000010) (37847045)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb cb 16 (11111000 11111011 11001011 00010110) (382467064)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** After the lock
cn.allchin.jvm.objecjtlayout.JOLSample_13_BiasedLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 80 41 02 (00000101 10000000 01000001 00000010) (37847045)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb cb 16 (11111000 11111011 11001011 00010110) (382467064)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total



 */
public class JOLSample_13_BiasedLocking {

    /*
     * This is the example of biased locking.
     *
     * In order to demonstrate this, we first need to sleep for >5 seconds
     * to pass the grace period of biased locking. Then, we do the same
     * trick as the example before. You may notice that the mark word
     * had not changed after the lock was released. That is because
     * the mark word now contains the reference to the thread this object
     * was biased to.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        TimeUnit.SECONDS.sleep(6);

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