package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

java.util.ArrayList@21b8d17cd footprint:
     COUNT       AVG       SUM   DESCRIPTION
         1      4016      4016   [Ljava.lang.Object;
      1000        16     16000   java.lang.Integer
         1        24        24   java.util.ArrayList
      1002               20040   (total)


java.util.LinkedList@694e1548d footprint:
     COUNT       AVG       SUM   DESCRIPTION
      1000        16     16000   java.lang.Integer
         1        32        32   java.util.LinkedList
      1000        24     24000   java.util.LinkedList$Node
      2001               40032   (total)


java.util.ArrayList@21b8d17cd, java.util.LinkedList@694e1548d footprint:
     COUNT       AVG       SUM   DESCRIPTION
         1      4016      4016   [Ljava.lang.Object;
      1000        16     16000   java.lang.Integer
         1        24        24   java.util.ArrayList
         1        32        32   java.util.LinkedList
      1000        24     24000   java.util.LinkedList$Node
      2003               44072   (total)




 */
public class JOLSample_16_AL_LL {

    /*
     * The example of traversing the reachability graphs.
     *
     * In addition to introspecting the object internals, we can also
     * see the object externals, that is, the objects referenced from the
     * object in question. There are multiple ways to illustrate this,
     * the summary table seems to work well.
     *
     * In this example, you can clearly see the difference between
     * the shadow heap (i.e. space taken by the reachable objects)
     * for ArrayList and LinkedList.
     *
     * When several roots are handed over to JOL, it tracks the objects reachable
     * from either root, and also avoids double-counting.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        ArrayList<Integer> al = new ArrayList<Integer>();
        LinkedList<Integer> ll = new LinkedList<Integer>();

        for (int i = 0; i < 1000; i++) {
            Integer io = i; // box once
            al.add(io);
            ll.add(io);
        }

        al.trimToSize();

        PrintWriter pw = new PrintWriter(out);
        pw.println(GraphLayout.parseInstance(al).toFootprint());
        pw.println(GraphLayout.parseInstance(ll).toFootprint());
        pw.println(GraphLayout.parseInstance(al, ll).toFootprint());
        pw.close();
    }

}