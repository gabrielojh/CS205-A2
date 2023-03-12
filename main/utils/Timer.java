package main.utils;

public class Timer {

    /**
     * Timer for how long it takes to create 1 unit of food
     * 
     * @param n time taken to create 1 unit of food in minutes
     */
    public static void gowork(int n) {
        for (int i = 0; i < n; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
