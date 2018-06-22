package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

 object externals:
          ADDRESS       SIZE TYPE                                           PATH                           VALUE
         d649a640         24 java.lang.String                               .table[7].key                  (object)
         d649a658         24 [C                                             .table[7].key.value            [F, o, o]
         d649a670         24 java.lang.String                               .table[7].val                  (object)
         d649a688         24 [C                                             .table[7].val.value            [B, a, r]
         d649a6a0         80 [Ljava.util.concurrent.ConcurrentHashMap$Node; .table                         [null, null, null, null, null, null, null, (object), null, null, null, null, null, (object), null, null]
         d649a6f0         32 java.util.concurrent.ConcurrentHashMap$Node    .table[7]                      (object)


 object externals:
          ADDRESS       SIZE TYPE                                        PATH                           VALUE
         d64a3518         24 java.lang.String                            .table[13].key                 (object)
         d64a3530         24 [C                                          .table[13].key.value           [F, o, o, 2]
         d64a3548         24 java.lang.String                            .table[13].val                 (object)
         d64a3560         24 [C                                          .table[13].val.value           [B, a, r, 2]
         d64a3578         32 java.util.concurrent.ConcurrentHashMap$Node .table[13]                     (object)





 */
public class JOLSample_24_Difference {

    /*
     * This is the example how would one use the GraphLayout differences to
     * figure out the object graph changes.
     *
     * Here, we have the ConcurrentHashMap, and three measurements:
     *   1) The initial CHM that has no backing storage;
     *   2) After adding the first KV pair, when both KV pair is allocated,
     *      and the backing storage is allocated;
     *   3) After adding the second KV pair.
     *
     * An API for subtracting the GraphLayouts helps to show the difference
     * between the snapshots. Note that differences are based on object
     * addresses, so if GC moves under our feet, the difference is unreliable.
     * It is a good idea to keep the allocations at minimum between the snapshots.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        Map<String, String> chm = new ConcurrentHashMap<String, String>();

        GraphLayout gl1 = GraphLayout.parseInstance(chm);

        chm.put("Foo", "Bar");
        GraphLayout gl2 = GraphLayout.parseInstance(chm);

        chm.put("Foo2", "Bar2");
        GraphLayout gl3 = GraphLayout.parseInstance(chm);

        System.out.println(gl2.subtract(gl1).toPrintable());
        System.out.println(gl3.subtract(gl2).toPrintable());
    }

}