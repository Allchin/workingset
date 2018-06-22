package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.GraphLayout;
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

[Ljava.lang.Integer;@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                 PATH                           VALUE
         d6466ef8         56 [Ljava.lang.Integer;                                [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
         d6466f30         16 java.lang.Integer    [0]                            0
         d6466f40         16 java.lang.Integer    [1]                            1
         d6466f50         16 java.lang.Integer    [2]                            2
         d6466f60         16 java.lang.Integer    [3]                            3
         d6466f70         16 java.lang.Integer    [4]                            4
         d6466f80         16 java.lang.Integer    [5]                            5
         d6466f90         16 java.lang.Integer    [6]                            6
         d6466fa0         16 java.lang.Integer    [7]                            7
         d6466fb0         16 java.lang.Integer    [8]                            8
         d6466fc0         16 java.lang.Integer    [9]                            9


[Ljava.lang.Integer;@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                 PATH                           VALUE
         81601c28         56 [Ljava.lang.Integer;                                [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
         81601c60     157472 (something else)     (somewhere else)               (something else)
         81628380         16 java.lang.Integer    [9]                            9
         81628390         16 java.lang.Integer    [8]                            8
         816283a0         16 java.lang.Integer    [7]                            7
         816283b0         16 java.lang.Integer    [6]                            6
         816283c0         16 java.lang.Integer    [5]                            5
         816283d0         16 java.lang.Integer    [4]                            4
         816283e0         16 java.lang.Integer    [3]                            3
         816283f0         16 java.lang.Integer    [2]                            2
         81628400         16 java.lang.Integer    [1]                            1
         81628410         16 java.lang.Integer    [0]                            0


[Ljava.lang.Integer;@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                 PATH                           VALUE
         81601c28         56 [Ljava.lang.Integer;                                [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
         81601c60     155112 (something else)     (somewhere else)               (something else)
         81627a48         16 java.lang.Integer    [9]                            9
         81627a58         16 java.lang.Integer    [8]                            8
         81627a68         16 java.lang.Integer    [7]                            7
         81627a78         16 java.lang.Integer    [6]                            6
         81627a88         16 java.lang.Integer    [5]                            5
         81627a98         16 java.lang.Integer    [4]                            4
         81627aa8         16 java.lang.Integer    [3]                            3
         81627ab8         16 java.lang.Integer    [2]                            2
         81627ac8         16 java.lang.Integer    [1]                            1
         81627ad8         16 java.lang.Integer    [0]                            0




 */
public class JOLSample_21_Arrays {

    /*
     * This example shows the array layout quirks.
     *
     * If you run it with parallel GC, you might notice that
     * fresh object elements are laid out after the array in
     * the forward order, but after GC then can be rearranged
     * in the reverse order. This is because GC records the
     * to-be-promoted objects on the stack.
     *
     * See also:
     *   https://bugs.openjdk.java.net/browse/JDK-8024394
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(System.out, true);

        Integer[] arr = new Integer[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = new Integer(i);
        }

        String last = null;
        for (int c = 0; c < 100; c++) {
            String current = GraphLayout.parseInstance((Object) arr).toPrintable();

            if (last == null || !last.equalsIgnoreCase(current)) {
                pw.println(current);
                last = current;
            }

            System.gc();
        }

        pw.close();
    }

}