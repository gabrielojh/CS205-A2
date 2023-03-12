package main.producers;

import main.SandwichManager;
import main.models.Egg;
import main.utils.Timer;

public class EggMaker {

    // Keeps count of how many eggMakers have been made so far to assign id
    private static int count = 0;

    // Keeps count of how many Eggs have been made by all eggMakers
    private volatile static int totalEggsMade = 0;
    
    private int eggRate;
    private String makerId;
    private int eggId;
    private int eggsMade;
    public Runnable runnable;

    
    public EggMaker(int eggRate) {
        this.eggRate = eggRate;
        this.eggId = 0;
        this.eggsMade = 0;
        this.makerId = "E" + count;
        count++;

        runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    // Make Egg (Call Timer to wait)
                    Egg egg = new Egg(eggId, makerId);
                    eggId++;
                    Timer.gowork(eggRate);

                    // Add Egg to Pool if there's still lesser than the required amount needed
                    SandwichManager.eggPool.put(egg);
                    if (totalEggsMade++ >= SandwichManager.nSandwiches) {
                        return;
                    }
                    eggsMade++;

                    // Add record to log file
                    SandwichManager.printer.log(String.format("%s puts egg %d", egg.makerId, egg.eggId));
                }
            }
        };
    }

    public int getEggsMade() {
        return eggsMade;
    }

    public String getMakerId() {
        return makerId;
    }

    @Override
    public String toString() {
        return "Egg Maker Id = " + makerId;
    }
}
