package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintWriter;
import java.util.Random;

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

Fresh object is at d646b788
*** Move  1, L1 is at d7d90468
*** Root is class cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1
cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                                               PATH                           VALUE
         d7d85970         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2 .l                             (object)
         d7d85980         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3 .l.l                           (object)
         d7d85990         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4 .l.l.l                         (object)
         d7d859a0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5 .l.l.l.l                       (object)
         d7d859b0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6 .l.l.l.l.l                     (object)
         d7d859c0      43688 (something else)                                   (somewhere else)               (something else)
         d7d90468         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1                                (object)


*** Move  2, L1 is at d828d6b0
*** Root is class cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2
cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2@2ef1e4fad object externals:
          ADDRESS       SIZE TYPE                                               PATH                           VALUE
         d828d670         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3 .l                             (object)
         d828d680         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4 .l.l                           (object)
         d828d690         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5 .l.l.l                         (object)
         d828d6a0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6 .l.l.l.l                       (object)
         d828d6b0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1 .l.l.l.l.l                     (object)
         d828d6c0      43704 (something else)                                   (somewhere else)               (something else)
         d8298178         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2                                (object)


*** Move  3, L1 is at d7dc8138
*** Root is class cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6
cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6@5ce65a89d object externals:
          ADDRESS       SIZE TYPE                                               PATH                           VALUE
         d7daa390         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6                                (object)
         d7daa3a0     122264 (something else)                                   (somewhere else)               (something else)
         d7dc8138         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1 .l                             (object)
         d7dc8148         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2 .l.l                           (object)
         d7dc8158         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3 .l.l.l                         (object)
         d7dc8168         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4 .l.l.l.l                       (object)
         d7dc8178         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5 .l.l.l.l.l                     (object)


*** Move  4, L1 is at da350070
*** Root is class cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5
cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5@421faab1d object externals:
          ADDRESS       SIZE TYPE                                               PATH                           VALUE
         da310a60         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5                                (object)
         da310a70     259568 (something else)                                   (somewhere else)               (something else)
         da350060         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6 .l                             (object)
         da350070         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1 .l.l                           (object)
         da350080         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2 .l.l.l                         (object)
         da350090         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3 .l.l.l.l                       (object)
         da3500a0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4 .l.l.l.l.l                     (object)


*** Move  5, L1 is at d9e58080
*** Root is class cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4
cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4@27fa135ad object externals:
          ADDRESS       SIZE TYPE                                               PATH                           VALUE
         d9e01060         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4                                (object)
         d9e01070     356336 (something else)                                   (somewhere else)               (something else)
         d9e58060         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5 .l                             (object)
         d9e58070         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6 .l.l                           (object)
         d9e58080         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1 .l.l.l                         (object)
         d9e58090         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2 .l.l.l.l                       (object)
         d9e580a0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3 .l.l.l.l.l                     (object)


*** Move  6, L1 is at de134ba0
*** Root is class cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3
cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3@b81eda8d object externals:
          ADDRESS       SIZE TYPE                                               PATH                           VALUE
         de118608         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3                                (object)
         de118618     116056 (something else)                                   (somewhere else)               (something else)
         de134b70         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4 .l                             (object)
         de134b80         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5 .l.l                           (object)
         de134b90         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6 .l.l.l                         (object)
         de134ba0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1 .l.l.l.l                       (object)
         de134bb0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2 .l.l.l.l.l                     (object)


*** Move  7, L1 is at 81608eb0
*** Root is class cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1
cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                                               PATH                           VALUE
         81608eb0         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L1                                (object)
         81608ec0     161816 (something else)                                   (somewhere else)               (something else)
         816306d8         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L2 .l                             (object)
         816306e8         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L3 .l.l                           (object)
         816306f8         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L4 .l.l.l                         (object)
         81630708         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L5 .l.l.l.l                       (object)
         81630718         16 cn.allchin.jvm.objecjtlayout.JOLSample_20_Roots$L6 .l.l.l.l.l                     (object)



 */
public class JOLSample_20_Roots {

    /*
     * The example how VM traverses the root sets.
     *
     * During the GC, object reachability graph should be traversed
     * starting from somewhere. The root set is the set of intrinsically
     * reachable objects. Static fields are the part of root set, local
     * variables are the part of root set as well.
     *
     * In this example, we build the "ring" of objects, and reference
     * only the single link from that ring from the local variable.
     * This will have the effect of having the different parts of ring
     * in the root set, which will, in the end, change the ring layout
     * in memory.
     */

    static volatile Object sink;

    public interface L {
        L link();
        void bind(L l);
    }

    public static abstract class AL implements L {
        L l;
        public L link() { return l; }
        public void bind(L l) { this.l = l; }
    }

    public static class L1 extends AL {}
    public static class L2 extends AL {}
    public static class L3 extends AL {}
    public static class L4 extends AL {}
    public static class L5 extends AL {}
    public static class L6 extends AL {}

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(System.out, true);

        // create links
        L l1 = new L1();
        L l2 = new L2();
        L l3 = new L3();
        L l4 = new L4();
        L l5 = new L5();
        L l6 = new L6();

        // bind the ring
        l1.bind(l2);
        l2.bind(l3);
        l3.bind(l4);
        l4.bind(l5);
        l5.bind(l6);
        l6.bind(l1);

        // current root
        L r = l1;

        // break all other roots
        l1 = l2 = l3 = l4 = l5 = l6 = null;

        long lastAddr = VM.current().addressOf(r);
        pw.printf("Fresh object is at %x%n", lastAddr);

        int moves = 0;
        for (int i = 0; i < 100000; i++) {

            // scan for L1 and determine it's address
            L s = r;
            while (!((s = s.link()) instanceof L1)) ;

            long cur = VM.current().addressOf(s);
            s = null;

            // if L1 had moved, then probably the entire ring had also moved
            if (cur != lastAddr) {
                moves++;
                pw.printf("*** Move %2d, L1 is at %x%n", moves, cur);
                pw.println("*** Root is " + r.getClass());

                pw.println(GraphLayout.parseInstance(r).toPrintable());

                // select another link
                Random random = new Random();
                for (int c = 0; c < random.nextInt(100); c++) {
                    r = r.link();
                }

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