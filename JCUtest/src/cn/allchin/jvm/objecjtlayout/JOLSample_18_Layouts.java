package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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

java.util.HashMap@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                                                    PATH                           VALUE
         81601b30         48 java.util.HashMap                                                                      (object)
         81601b60     445040 (something else)                                        (somewhere else)               (something else)
         8166e5d0         80 [Ljava.util.HashMap$Node;                               .table                         [null, (object), (object), null, null, null, null, null, null, null, null, null, null, null, null, null]
         8166e620         32 java.util.HashMap$Node                                  .table[2]                      (object)
         8166e640         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].key                  (object)
         8166e658         32 java.util.HashMap$Node                                  .table[1]                      (object)
         8166e678         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[1].key                  (object)


java.util.HashMap@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                                                    PATH                           VALUE
         81601b30         48 java.util.HashMap                                                                      (object)
         81601b60     437272 (something else)                                        (somewhere else)               (something else)
         8166c778         80 [Ljava.util.HashMap$Node;                               .table                         [null, (object), (object), null, null, null, null, null, null, null, null, null, null, null, null, null]
         8166c7c8         32 java.util.HashMap$Node                                  .table[2]                      (object)
         8166c7e8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].key                  (object)
         8166c800         32 java.util.HashMap$Node                                  .table[1]                      (object)
         8166c820         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[1].key                  (object)
         8166c838     801720 (something else)                                        (somewhere else)               (something else)
         817303f0         32 java.util.HashMap$Node                                  .table[2].next                 (object)
         81730410         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].next.key             (object)
         81730428         32 java.util.HashMap$Node                                  .table[2].next.next            (object)
         81730448         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].next.next.key        (object)
         81730460         32 java.util.HashMap$Node                                  .table[2].next.next.next       (object)
         81730480         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].next.next.next.key   (object)
         81730498         32 java.util.HashMap$Node                                  .table[2].next.next.next.next  (object)
         817304b8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].next.next.next.next.key (object)


java.util.HashMap@21b8d17cd object externals:
          ADDRESS       SIZE TYPE                                                    PATH                           VALUE
         81601b30         48 java.util.HashMap                                                                      (object)
         81601b60     437184 (something else)                                        (somewhere else)               (something else)
         8166c720         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].next.key             (object)
         8166c738         32 java.util.HashMap$Node                                  .table[1]                      (object)
         8166c758         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[1].key                  (object)
         8166c770     798776 (something else)                                        (somewhere else)               (something else)
         8172f7a8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].left.key             (object)
         8172f7c0         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].left.right.key       (object)
         8172f7d8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].key                  (object)
         8172f7f0         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].left.right.next.key  (object)
         8172f808      18320 (something else)                                        (somewhere else)               (something else)
         81733f98        272 [Ljava.util.HashMap$Node;                               .table                         [null, (object), (object), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]
         817340a8         56 java.util.HashMap$TreeNode                              .table[2]                      (object)
         817340e0         56 java.util.HashMap$TreeNode                              .table[2].next                 (object)
         81734118         56 java.util.HashMap$TreeNode                              .table[2].left                 (object)
         81734150         56 java.util.HashMap$TreeNode                              .table[2].left.right           (object)
         81734188         56 java.util.HashMap$TreeNode                              .table[2].left.right.next      (object)
         817341c0         56 java.util.HashMap$TreeNode                              .table[2].right.left           (object)
         817341f8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.left.key       (object)
         81734210         56 java.util.HashMap$TreeNode                              .table[2].right.prev           (object)
         81734248         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.prev.key       (object)
         81734260         56 java.util.HashMap$TreeNode                              .table[2].right                (object)
         81734298         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.key            (object)
         817342b0         56 java.util.HashMap$TreeNode                              .table[2].right.next           (object)
         817342e8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.next.key       (object)
         81734300         56 java.util.HashMap$TreeNode                              .table[2].right.right.left     (object)
         81734338         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.left.key (object)
         81734350         56 java.util.HashMap$TreeNode                              .table[2].right.right.prev     (object)
         81734388         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.prev.key (object)
         817343a0         56 java.util.HashMap$TreeNode                              .table[2].right.right          (object)
         817343d8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.key      (object)
         817343f0         56 java.util.HashMap$TreeNode                              .table[2].right.right.next     (object)
         81734428         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.next.key (object)
         81734440         56 java.util.HashMap$TreeNode                              .table[2].right.right.right    (object)
         81734478         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.right.key (object)
         81734490         56 java.util.HashMap$TreeNode                              .table[2].right.right.right.next (object)
         817344c8         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.right.next.key (object)
         817344e0         56 java.util.HashMap$TreeNode                              .table[2].right.right.right.right (object)
         81734518         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.right.right.key (object)
         81734530         56 java.util.HashMap$TreeNode                              .table[2].right.right.right.right.right (object)
         81734568         24 cn.allchin.jvm.objecjtlayout.JOLSample_18_Layouts$Dummy .table[2].right.right.right.right.right.key (object)





 */
public class JOLSample_18_Layouts {

    /*
     * This is the example of more verbose reachability graph.
     *
     * In this example, we see that under collisions, HashMap
     * degrades to the linked list. With JDK 8, we can also see
     * it further "degrades" to the tree.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(System.out, true);

        Map<Dummy, Void> map = new HashMap<Dummy, Void>();

        map.put(new Dummy(1), null);
        map.put(new Dummy(2), null);

        System.gc();
        pw.println(GraphLayout.parseInstance(map).toPrintable());

        map.put(new Dummy(2), null);
        map.put(new Dummy(2), null);
        map.put(new Dummy(2), null);
        map.put(new Dummy(2), null);

        System.gc();
        pw.println(GraphLayout.parseInstance(map).toPrintable());

        for (int c = 0; c < 12; c++) {
            map.put(new Dummy(2), null);
        }

        System.gc();
        pw.println(GraphLayout.parseInstance(map).toPrintable());

        pw.close();
    }

    /**
     * Dummy class which controls the hashcode and is decently Comparable.
     */
    public static class Dummy implements Comparable<Dummy> {
        static int ID;
        final int id = ID++;
        final int hc;

        public Dummy(int hc) {
            this.hc = hc;
        }

        @Override
        public boolean equals(Object o) {
            return (this == o);
        }

        @Override
        public int hashCode() {
            return hc;
        }

        @Override
        public int compareTo(Dummy o) {
            return (id < o.id) ? -1 : ((id == o.id) ? 0 : 1);
        }
    }

}