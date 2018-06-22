package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import static java.lang.System.out;

/**
 * @author Aleksey Shipilev
 * <pre>
 * # Running 64-bit HotSpot VM.
# Using compressed oop with 0-bit shift.
# Using compressed klass with 0-bit shift.
# Objects are 8 bytes aligned.
# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

[J object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           48 0d d0 16 (01001000 00001101 11010000 00010110) (382733640)
     12     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     16     0   long [J.<elements>                             N/A
Instance size: 16 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     16     0   byte [B.<elements>                             N/A
Instance size: 16 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     16     1   byte [B.<elements>                             N/A
     17     7        (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 7 bytes external = 7 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           02 00 00 00 (00000010 00000000 00000000 00000000) (2)
     16     2   byte [B.<elements>                             N/A
     18     6        (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 6 bytes external = 6 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           03 00 00 00 (00000011 00000000 00000000 00000000) (3)
     16     3   byte [B.<elements>                             N/A
     19     5        (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 5 bytes external = 5 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           04 00 00 00 (00000100 00000000 00000000 00000000) (4)
     16     4   byte [B.<elements>                             N/A
     20     4        (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
     16     5   byte [B.<elements>                             N/A
     21     3        (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 3 bytes external = 3 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           06 00 00 00 (00000110 00000000 00000000 00000000) (6)
     16     6   byte [B.<elements>                             N/A
     22     2        (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 2 bytes external = 2 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           07 00 00 00 (00000111 00000000 00000000 00000000) (7)
     16     7   byte [B.<elements>                             N/A
     23     1        (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 1 bytes external = 1 bytes total

[B object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           a8 07 d0 16 (10101000 00000111 11010000 00010110) (382732200)
     12     4        (object header)                           08 00 00 00 (00001000 00000000 00000000 00000000) (8)
     16     8   byte [B.<elements>                             N/A
Instance size: 24 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total


 * 
 */
public class JOLSample_25_ArrayAlignment {

    /*
     * This sample showcases that the alignment requirements are also
     * affecting arrays. This test introspects the byte[] arrays of different
     * small sizes. It may be seen that many arrays are actually consuming the
     * same space, since they are also required to be externally aligned.
     *
     * The internal alignment can be demonstrated in some specific VM modes, e.g.
     * on long[] arrays with 32-bit modes. There, the zero-th element of long[]
     * array should be aligned by 8.
     *
     * Or, even on byte[] arrays in 64-bit mode with compressed references disabled,
     * on some VMs:
     *   https://bugs.openjdk.java.net/browse/JDK-8139457
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());
        out.println(ClassLayout.parseInstance(new long[0]).toPrintable());
        for (int size = 0; size <= 8; size++) {
            out.println(ClassLayout.parseInstance(new byte[size]).toPrintable());
        }
    }

}