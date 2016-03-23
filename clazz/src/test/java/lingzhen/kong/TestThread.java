package lingzhen.kong;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kongzheng on 16/2/25.
 */
public class TestThread {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition a = lock.newCondition();
        Condition b = lock.newCondition();
        Condition c = lock.newCondition();
//        lock.lock();
//        try {
//            c.signal();
//        } finally {
//            lock.unlock();
//        }
        for (int i = 0; i < 3; i++) {
            new Worker(lock, a, b, c, i + 1).start();
        }
    }


    static class Worker extends Thread {

        private final ReentrantLock lock;
        private final Condition a;
        private final Condition b;
        private final Condition c;
        private final int id;

        public Worker(ReentrantLock lock, Condition a, Condition b, Condition c, int id) {
            this.lock = lock;
            this.a = a;
            this.b = b;
            this.c = c;
            this.id = id;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                lock.lock();
                try {
                    switch (id) {
                        case 1:
                            System.out.println("线程" + id + "--------> A");
                            b.signal();//相当于Object#notify
                            a.await();
                            break;
                        case 2:
                            System.out.println("线程" + id + "--------> B");
                            c.signal();
                            b.await();
                            break;
                        case 3:
                            System.out.println("线程" + id + "--------> C");
                            a.signal();
                            c.await();
                            break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}