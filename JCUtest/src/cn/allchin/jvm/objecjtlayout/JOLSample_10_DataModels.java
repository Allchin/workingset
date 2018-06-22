package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.datamodel.X86_32_DataModel;
import org.openjdk.jol.datamodel.X86_64_COOPS_DataModel;
import org.openjdk.jol.datamodel.X86_64_DataModel;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.layouters.CurrentLayouter;
import org.openjdk.jol.layouters.HotSpotLayouter;
import org.openjdk.jol.layouters.Layouter;

/**
 * <pre>
 * @author Aleksey Shipilev
 * 
 * ***** Current VM Layout
cn.allchin.jvm.objecjtlayout.JOLSample_10_DataModels$A object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0    12                    (object header)                           N/A
     12     4   java.lang.Object A.a                                       N/A
     16     4   java.lang.Object A.b                                       N/A
     20     4                    (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

***** VM Layout Simulation (X32 model, 8-byte aligned, compact fields, field allocation style: 1)
cn.allchin.jvm.objecjtlayout.JOLSample_10_DataModels$A object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     8                    (object header)                           N/A
      8     4   java.lang.Object A.a                                       N/A
     12     4   java.lang.Object A.b                                       N/A
Instance size: 16 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

***** VM Layout Simulation (X64 model, 8-byte aligned, compact fields, field allocation style: 1)
cn.allchin.jvm.objecjtlayout.JOLSample_10_DataModels$A object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0    16                    (object header)                           N/A
     16     8   java.lang.Object A.a                                       N/A
     24     8   java.lang.Object A.b                                       N/A
Instance size: 32 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

***** VM Layout Simulation (X64 model (compressed oops), 8-byte aligned, compact fields, field allocation style: 1)
cn.allchin.jvm.objecjtlayout.JOLSample_10_DataModels$A object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0    12                    (object header)                           N/A
     12     4   java.lang.Object A.a                                       N/A
     16     4   java.lang.Object A.b                                       N/A
     20     4                    (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total



 */
public class JOLSample_10_DataModels {

    /*
     * This example shows the differences between the data models.
     *
     * First layout is the actual VM layout, the remaining three
     * are simulations. You can see the reference sizes are different,
     * depending on VM bitness or mode. The header sizes are also
     * a bit different, see subsequent examples to understand why.
     */

    public static void main(String[] args) throws Exception {
        Layouter l;

        l = new CurrentLayouter();
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());

        l = new HotSpotLayouter(new X86_32_DataModel());
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());

        l = new HotSpotLayouter(new X86_64_DataModel());
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());

        l = new HotSpotLayouter(new X86_64_COOPS_DataModel());
        System.out.println("***** " + l);
        System.out.println(ClassLayout.parseClass(A.class, l).toPrintable());
    }

    public static class A {
        Object a;
        Object b;
    }

}