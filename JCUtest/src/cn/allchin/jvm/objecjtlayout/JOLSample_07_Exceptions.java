package cn.allchin.jvm.objecjtlayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

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

java.lang.Throwable object internals:
 OFFSET  SIZE                            TYPE DESCRIPTION                               VALUE
      0    12                                 (object header)                           N/A
     12     4                                 (alignment/padding gap)                  
     16     4                java.lang.String Throwable.detailMessage                   N/A
     20     4             java.lang.Throwable Throwable.cause                           N/A
     24     4   java.lang.StackTraceElement[] Throwable.stackTrace                      N/A
     28     4                  java.util.List Throwable.suppressedExceptions            N/A
Instance size: 32 bytes
Space losses: 4 bytes internal + 0 bytes external = 4 bytes total


 */
public class JOLSample_07_Exceptions {

    /*
     * This example shows some of the fields are treated specially in VM.
     *
     * See the suspicious gap in Throwable class. If you look in the Java
     * source, you will see the Throwable.backtrace field, which is not
     * listed in the dump. This is because this field handles the VM internal
     * info which should not be accessible to users under no conditions.
     *
     * See also:
     *    http://bugs.openjdk.java.net/browse/JDK-4496456
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());
        out.println(ClassLayout.parseClass(Throwable.class).toPrintable());
    }

}