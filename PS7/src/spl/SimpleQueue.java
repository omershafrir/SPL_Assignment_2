package spl;

import java.util.Vector;

class SimpleQueue <E> implements BlockingQueue <E>{

    // a vector, used to implement the queue
    private Vector<E> vec_;
    private final int MAX;

    public SimpleQueue(int max) { MAX = max; }

    public synchronized int size(){
        return vec_.size();
    }

    public synchronized void put(E e){
        while(size()>=MAX){
            try{
                this.wait();
            } catch (InterruptedException ignored){}
        }

        vec_.add(e);
        // wakeup everybody. If someone is waiting in the get()
        // method, it can now perform the get.
        this.notifyAll();
    }

    public synchronized E take(){
        while(size()==0){
            try{
                this.wait();
            } catch (InterruptedException ignored){}
        }

        E e = vec_.get(0);
        vec_.remove(0);
        // wakeup everybody. If someone is waiting in the add()
        // method, it can now perform the add.
        this.notifyAll();
        return e;
    }
// Implementations of additional methods...
}