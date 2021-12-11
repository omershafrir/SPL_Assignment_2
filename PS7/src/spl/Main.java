package spl;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    static class Producer implements Runnable {
        private final ArrayBlockingQueue<Integer> queue;
        private final int id;
        private final int amount; //Number of products to produce
        private final int PERIOD = 700; //Time period required for producing one product
        Producer(int id, int amount, ArrayBlockingQueue<Integer> q) {
            this.id = id;
            this.amount = amount;
            queue =q;
        }
        public void run() {
            try {
                for (int i =0; i < amount; i++){
                    Thread.sleep(PERIOD);
                    queue.put(produce(id,i));
                }

            } catch (InterruptedException ignored) { }
        }

        /**
         * produce an Integer product composed of producer's id and current iteration
         * @param id - producer id
         * @param iteration - current iteration
         * @return Integer product
         */
        private Integer produce(int id, int iteration) {
            int product = id * amount + iteration;
            System.out.println("Producing: " + product);
            return product;

        }
    }

    static class Consumer implements Runnable {
        private final ArrayBlockingQueue<Integer> queue;
        private final int id;
        private final int amount; //Number of products to consume
        private final int PERIOD = 2000;  //Time period required for consuming one product
        Consumer(int id, int amount, ArrayBlockingQueue<Integer> q) {
            this.id = id;
            this.amount = amount;
            queue =q;
        }
        public void run() {
            try {
                for (int i =0; i < amount; i++){
                    consume(queue.take());
                    Thread.sleep(PERIOD);
                }

            } catch (InterruptedException ignored) { }
        }
        /**
         * consume an Integer product
         * @param prod - the product to be consumed
         */
        private void consume(Integer prod){
            System.out.println("Consumed:  " + prod);
        }
    }

    static class Fork{
        private Diner owner;
        public Fork(Diner d) { owner = d; }
        public Diner getOwner() { return owner; }
        public synchronized void setOwner(Diner d) { owner = d; }
        public synchronized void use() { System.out.printf("%s has eaten!", owner.name); }
    }

    static class Diner{
        private String name;
        private boolean isHungry;

        public Diner(String n) { name = n; isHungry = true; }
        public String getName() { return name; }
        public boolean isHungry() { return isHungry; }

        public void eatWith(Fork fork, Diner spouse){
            while (isHungry)
            {
                // Don't have the fork, so wait patiently for spouse.
                if (fork.owner != this){
                    try { Thread.sleep(1); } catch(InterruptedException e) { continue; }
                    continue;
                }

                // If spouse is hungry, insist upon passing the fork.
                if (spouse.isHungry()) {
                    System.out.printf("%s: You eat first my darling %s!%n", name, spouse.getName());
                    fork.setOwner(spouse);
                    continue;
                }

                // Spouse wasn't hungry, so finally eat
                fork.use();
                isHungry = false;
                System.out.printf("%s: I am stuffed, my darling %s!%n", name, spouse.getName());
                fork.setOwner(spouse);
            }
        }
    }

    private static void deadlock(){
        final StringBuffer resource1 = new StringBuffer("resource1");
        final StringBuffer resource2 = new StringBuffer("resource2");
        // Here's the first thread.  It tries to lock resource1 then resource2
        Thread t1 = new Thread(()->{
                // Lock resource 1
                synchronized(resource1) {
                    System.out.println("Thread 1: locked " + resource1);

                    // Pause for a bit, simulating some file I/O or something.
                    // Basically, we just want to give the other thread a chance to
                    // run.  Threads and deadlock are asynchronous things, but we're
                    // trying to force deadlock to happen here...
                    try { Thread.sleep(50); } catch (InterruptedException e) {}

                    // Now wait 'till we can get a lock on resource 2
                    synchronized(resource2) {
                        System.out.println("Thread 1: locked " + resource2);
                    }
                }
        });

        // Here's the second thread.  It tries to lock resource2 then resource1
        Thread t2 = new Thread(()->{
                // This thread locks resource 2 right away
                synchronized(resource2) {
                    System.out.println("Thread 2: locked " + resource2);

                    // Then it pauses, for the same reason as the first thread does
                    try { Thread.sleep(50); } catch (InterruptedException e) {}

                    // Then it tries to lock resource1.  But wait!  Thread 1 locked
                    // resource1, and won't release it 'till it gets a lock on
                    // resource2.  This thread holds the lock on resource2, and won't
                    // release it 'till it gets resource1.  We're at an impasse. Neither
                    // thread can run, and the program freezes up.
                    synchronized(resource1) {
                        System.out.println("Thread 2: locked locked " + resource1);
                    }
                }
        });

        // Start the two threads. If all goes as planned, deadlock will occur,
        // and the program will never exit.
        t1.start();
        t2.start();
    }


    private static void prodCons(){
        final int BOUND = 5;
        final int AMOUNT = 1;
        ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(BOUND);
        Producer p1 = new Producer(1,AMOUNT,q);
        Producer p2 = new Producer(2,AMOUNT,q);
        Consumer c = new Consumer(3,2*AMOUNT,q);
        new Thread(p1).start();
        new Thread(p2).start();
        new Thread(c).start();
    }

    private static void livelock(){
        final Diner husband = new Diner("Bob");
        final Diner wife = new Diner("Alice");

        final Fork fork = new Fork(husband);

        //anonymous class
        Thread t1 = new Thread(()-> { husband.eatWith(fork, wife); });
        Thread t2 = new Thread(()->{ wife.eatWith(fork, husband); });

        t1.start();
        t2.start();
    }

    public static void main(String[] args) {
        //prodCons();
        deadlock();
        //livelock();
    }
}
