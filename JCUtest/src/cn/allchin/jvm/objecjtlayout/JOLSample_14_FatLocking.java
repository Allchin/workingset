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
cn.allchin.jvm.objecjtlayout.JOLSample_14_FatLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb c1 16 (11111000 11111011 11000001 00010110) (381811704)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** Before the lock
cn.allchin.jvm.objecjtlayout.JOLSample_14_FatLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           e8 f6 80 5a (11101000 11110110 10000000 01011010) (1518401256)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb c1 16 (11111000 11111011 11000001 00010110) (381811704)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** With the lock
cn.allchin.jvm.objecjtlayout.JOLSample_14_FatLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           1a 7a 53 57 (00011010 01111010 01010011 01010111) (1465088538)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb c1 16 (11111000 11111011 11000001 00010110) (381811704)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** After the lock
cn.allchin.jvm.objecjtlayout.JOLSample_14_FatLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           1a 7a 53 57 (00011010 01111010 01010011 01010111) (1465088538)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb c1 16 (11111000 11111011 11000001 00010110) (381811704)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

**** After System.gc()
cn.allchin.jvm.objecjtlayout.JOLSample_14_FatLocking$A object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           09 00 00 00 (00001001 00000000 00000000 00000000) (9)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           f8 fb c1 16 (11111000 11111011 11000001 00010110) (381811704)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total


 */
public class JOLSample_14_FatLocking {

    /*
     * This is the example of fat locking.
     *
     * If VM detects contention on thread, it needs to delegate the
     * access arbitrage to OS. That involves associating the object
     * with the native lock, i.e. "inflating" the lock.
     *
     * In this example, we need to simulate the contention, this is
     * why we have the additional thread. You can see the fresh object
     * has the default mark word, the object before the lock was already
     * acquired by the auxiliary thread, and when the lock was finally
     * acquired by main thread, it had been inflated. The inflation stays
     * there after the lock is released. You can also see the lock is
     * "deflated" after the GC (the lock cleanup proceeds in safepoints,
     * actually).
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        out.println("**** Fresh object");
        out.println(layout.toPrintable());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a) {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });

        t.start();

        TimeUnit.SECONDS.sleep(1);

        out.println("**** Before the lock");
        out.println(layout.toPrintable());

        synchronized (a) {
            out.println("**** With the lock");
            out.println(layout.toPrintable());
        }

        out.println("**** After the lock");
        out.println(layout.toPrintable());

        System.gc();

        out.println("**** After System.gc()");
        out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }

}
