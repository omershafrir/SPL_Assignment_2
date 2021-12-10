package spl;

public interface BlockingQueue<E> {
    void put(E o);

    E take();
}
