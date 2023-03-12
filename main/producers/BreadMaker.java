package main.producers;

import main.SandwichManager;
import main.models.Bread;
import main.utils.Timer;

public class BreadMaker {

    // Keeps count of how many breadMakers have been made so far to assign id
    private static int count = 0;

    // Keeps count of how many Breads have been made by all BreadMakers
    private volatile static int totalBreadsMade = 0;
    
    private int breadRate;
    private String makerId;
    private int breadId;
    private int breadsMade;
    public Runnable runnable;

    public BreadMaker(int breadRate) {
        this.breadRate = breadRate;
        this.breadId = 0;
        this.breadsMade = 0;
        this.makerId = "B" + count;
        count++;

        runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    // Make Bread (Call Timer to wait)
                    Bread bread = new Bread(breadId, makerId);
                    breadId++;
                    Timer.gowork(breadRate);

                    // Add Bread to Pool if there's still lesser than the required amount needed
                    SandwichManager.breadPool.put(bread);
                    if (totalBreadsMade++ >= SandwichManager.nSandwiches * 2) {
                        return;
                    }
                    breadsMade++;

                    // Add record to log file
                    SandwichManager.printer.log(String.format("%s puts bread %d", bread.makerId, bread.breadId));
                }
            }
        };
    }

    public String getMakerId() {
        return makerId;
    }

    public int getBreadsMade() {
        return breadsMade;
    }

    @Override
    public String toString() {
        return "Bread Maker Id = " + makerId;
    }
}
