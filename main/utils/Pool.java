package main.utils;

import main.SandwichManager;
import main.models.Bread;
import main.models.Egg;

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

    /**
     * Adds in a new food item made by a Maker into the circular queue if it has capacity still and the
     * required amount of the particular food isn't reached
     * 
     * @param food Type of food in the Pool (Bread / Egg)
     */
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

    /**
     * Removes food from the circular queue if it is not empty
     * 
     * @return Food of type Bread / Egg in the circular queue in FIFO manner
     */
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
