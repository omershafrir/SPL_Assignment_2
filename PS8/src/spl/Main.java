package spl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;


public class Main {

    private static class InterruptedWorker implements Runnable{
        public InterruptedWorker() {}

        public void doSomeWork() {
            WorkerHelper workerHelper = new WorkerHelper();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    workerHelper.doSomeWork();
                } catch (InterruptedException e)
                {
                    // raise the interrupt. This is very important!
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                doSomeWork();
            }
            System.out.println("stopping ;)");
        }
    }

    private static class WorkerHelper{
        public synchronized void doSomeWork() throws InterruptedException {
            try {
                // doing some work...
                Thread.sleep(10);
                // waiting for some condition...
                this.wait();
            } catch (InterruptedException e) {
                // re-throwing the exception to let the thread itself handle the exception.
                throw e;
            }
        }
    }

    private static class Worker implements Callable<Worker>
    {
        public final int num;
        public int time = 0;
        private final AtomicInteger count;

        public Worker(int num){
            this.num = num;
            this.count = null;
        }

        public Worker(int num, AtomicInteger count)
        {
            this.num = num;
            this.count = count;
        }

        @Override
        public Worker call()
        {
            if(count!=null) {
                for (int i = 0; i < 50; i++) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                    }
                }

                this.time = this.count.get();
                return this;
            }
            else{
                int seconds = (int) (Math.random() * 10);
                try {
                    System.out.println("Worker: " + this.num + " waiting for : " + seconds + "s");
                    Thread.sleep(seconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return this; // return yourself when done
            }
        }
    }

    public static void future(){
        AtomicInteger count = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<Future<Worker>> list = new ArrayList<Future<Worker>>();
        for (int i = 0; i < 30; i++) {
            Callable<Worker> worker = new Worker(10 + i, count);
            Future<Worker> submit = executor.submit(worker);
            list.add(submit);

        }

        for (int i = 0; i < 100; i++) {
            count.incrementAndGet();
            try {Thread.sleep(50);} catch (InterruptedException e) {}

        }

        executor.shutdown();
        // Wait at most 1 minute for all workers to finish:

        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
        }


        for (Future<Worker> future : list) {
            if (future.isDone()) {
                try {
                    System.out.println("worker: " + future.get().num + " \t time:" + future.get().time);
                } catch (InterruptedException e) {} catch (ExecutionException e) {}
            }

        }

        System.out.println("all done!");
    }

    public static void future2() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<Worker> ecs = new ExecutorCompletionService<Worker>(executor);


        final int numOfFutures = 30;
        for (int i = 0; i < numOfFutures; i++) {
            Callable<Worker> worker = new Worker(10 + i);

            ecs.submit(worker);
        }
        // do not accept any more Futures to the executor
        executor.shutdown();

        // ExecutorCompletionService's take() method
        // returns the first finished Future available
        for (int i = 0; i < numOfFutures; i++) {
            Worker finishedWorker = ecs.take().get();
            System.out.println("Worker: " + finishedWorker.num + " finished");
        }



        System.out.println("all done!");
    }

    public static void interruptTest(){
        Thread t = new Thread(new InterruptedWorker());
        t.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) { }

        t.interrupt();
    }

    public static void main(String[] args) {
        //future();
        try{
            //future2();
        }catch(Exception e){
            e.printStackTrace();
        }
        interruptTest();
    }
}
