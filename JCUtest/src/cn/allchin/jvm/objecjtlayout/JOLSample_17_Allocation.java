package cn.allchin.jvm.objecjtlayout;



import org.openjdk.jol.vm.VM;

import java.io.PrintWriter;

import static java.lang.System.out;

/**
 * @author Aleksey Shipilev
 */
public class JOLSample_17_Allocation {

    /*
     * The example of allocation addresses.
     *
     * This example shows the addresses of newly allocated objects
     * grow linearly in HotSpot. This is because the allocation in
     * parallel collectors is linear. We can also see it rewinds back
     * to the same offsets -- that's the start of some GC generation.
     * The address of the generation is changing, while GC adjusts
     * for the allocation rate.
     */

    public static void main(String[] args) throws Exception {
        out.println(VM.current().details());

        PrintWriter pw = new PrintWriter(out, true);

        long last = VM.current().addressOf(new Object());
        for (int l = 0; l < 1000 * 1000 * 1000; l++) {
            long current = VM.current().addressOf(new Object());

            long distance = Math.abs(current - last);
            if (distance > 16 * 1024) {
                pw.printf("Jumping from %x to %x (distance = %d bytes, %dK, %dM)%n",
                        last,
                        current,
                        distance,
                        distance / 1024,
                        distance / 1024 / 1024);
            }

            last = current;
        }

        pw.close();
    }

}
