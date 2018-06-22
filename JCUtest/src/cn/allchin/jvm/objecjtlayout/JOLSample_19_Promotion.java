package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintWriter;

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

Fresh object is at d6464f00
*** Move  1, object is at d7d98000
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           09 00 00 00 (00001001 00000000 00000000 00000000) (9)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           28 0f aa 16 (00101000 00001111 10101010 00010110) (380243752)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

*** Move  2, object is at d8288a28
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           11 00 00 00 (00010001 00000000 00000000 00000000) (17)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           28 0f aa 16 (00101000 00001111 10101010 00010110) (380243752)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

*** Move  3, object is at d7d88ba0
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           19 00 00 00 (00011001 00000000 00000000 00000000) (25)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           28 0f aa 16 (00101000 00001111 10101010 00010110) (380243752)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

*** Move  4, object is at da318028
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           21 00 00 00 (00100001 00000000 00000000 00000000) (33)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           28 0f aa 16 (00101000 00001111 10101010 00010110) (380243752)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

*** Move  5, object is at d9e08a28
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           29 00 00 00 (00101001 00000000 00000000 00000000) (41)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           28 0f aa 16 (00101000 00001111 10101010 00010110) (380243752)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

*** Move  6, object is at de108728
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           31 00 00 00 (00110001 00000000 00000000 00000000) (49)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           28 0f aa 16 (00101000 00001111 10101010 00010110) (380243752)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

*** Move  7, object is at 8160c5b0
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           31 00 00 00 (00110001 00000000 00000000 00000000) (49)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           28 0f aa 16 (00101000 00001111 10101010 00010110) (380243752)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total


 */
public class JOLSample_19_Promotion {

    /*
     * The example of object promotion.
     *
     * Once the object survives the garbage collections, it is getting
     * promoted to another generation. In this example, we can track
     * the addresses of the objects, as it changes over time.
     *
     * VM also needs to record the "age" (that is, the number of GC
     * cycles the object had survived) of the object somewhere, and
     * it is stored in mark word as well. See how particular mark word
     * bits change with each promotion.
     */

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(System.out, true);

        Object o = new Object();

        ClassLayout layout = ClassLayout.parseInstance(o);

        long lastAddr = VM.current().addressOf(o);
        pw.printf("Fresh object is at %x%n", lastAddr);

        int moves = 0;
        for (int i = 0; i < 100000; i++) {
            long cur = VM.current().addressOf(o);
            if (cur != lastAddr) {
                moves++;
                pw.printf("*** Move %2d, object is at %x%n", moves, cur);
                out.println(layout.toPrintable());
                lastAddr = cur;
            }

            // make garbage
            for (int c = 0; c < 10000; c++) {
                sink = new Object();
            }
        }

        pw.close();
    }

}