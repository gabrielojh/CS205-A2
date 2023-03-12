package main.utils;

import main.SandwichManager;
import main.models.Bread;
import main.models.Egg;
import main.producers.SandwichPacker;

public class Pool<T> {
    
    private int front;
    private int end;
    private T[] queue;
    private int capacity;
    private int numMade;
    private int size;

    @SuppressWarnings("unchecked")
    public Pool(int capacity) {
        this.queue = (T[])new Object[capacity];
        this.capacity = capacity;
        this.size = 0;
        this.front = 0;
        this.end = 0;
        this.numMade = 0;
    }

    public int getSize() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }

    public synchronized void put(T food) {

        // Check if pool is full
        while (size == capacity) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Check if limit reached for egg / bread
        if (food instanceof Bread && numMade >= SandwichManager.nSandwiches * 2) {
            return;
        }

        if (food instanceof Egg && numMade >= SandwichManager.nSandwiches) {
            return;
        }
        
        // Add to end of queue
        queue[end] = food;
        end = (end + 1) % queue.length;
        size++;
        numMade++;
        this.notifyAll();
    }

    public synchronized T get() {

        // Check if pool is empty
        while (size == 0) {
            try {
                // System.out.println("Testing");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        T food = queue[front];
        front = (front + 1) % queue.length;
        size--;
        this.notifyAll();

        return food;
    }
}
