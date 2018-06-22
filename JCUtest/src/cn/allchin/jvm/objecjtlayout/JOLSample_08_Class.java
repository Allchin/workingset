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

java.lang.Class object internals:
 OFFSET  SIZE                                              TYPE DESCRIPTION                               VALUE
      0    12                                                   (object header)                           N/A
     12     4                     java.lang.reflect.Constructor Class.cachedConstructor                   N/A
     16     4                                   java.lang.Class Class.newInstanceCallerCache              N/A
     20     4                                  java.lang.String Class.name                                N/A
     24     4                                                   (alignment/padding gap)                  
     28     4                       java.lang.ref.SoftReference Class.reflectionData                      N/A
     32     4   sun.reflect.generics.repository.ClassRepository Class.genericInfo                         N/A
     36     4                                java.lang.Object[] Class.enumConstants                       N/A
     40     4                                     java.util.Map Class.enumConstantDirectory               N/A
     44     4                    java.lang.Class.AnnotationData Class.annotationData                      N/A
     48     4             sun.reflect.annotation.AnnotationType Class.annotationType                      N/A
     52     4                java.lang.ClassValue.ClassValueMap Class.classValueMap                       N/A
     56    32                                                   (alignment/padding gap)                  
     88     4                                               int Class.classRedefinedCount                 N/A
     92     4                                                   (loss due to the next object alignment)
Instance size: 96 bytes
Space losses: 36 bytes internal + 4 bytes external = 40 bytes total


 */
public class JOLSample_08_Class {

    /*
     * Another example of special treatment for some fields.
     *
     * If you run this example, you can see the large gap in instance field block.
     * There are no Java fields that could claim that block, hence there are no
     * "hidden" fields, like in the example before. This time, VM "injects" some
     * of the fields into the Class, to store some of the meta-information there.
     *
     * See:
     *  http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/tip/src/share/vm/classfile/javaClasses.hpp
     *  http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/tip/src/share/vm/classfile/javaClasses.cpp
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());
        out.println(ClassLayout.parseClass(Class.class).toPrintable());
    }

}