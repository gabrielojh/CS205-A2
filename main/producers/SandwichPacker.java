package main.producers;

import main.SandwichManager;
import main.models.Bread;
import main.models.Egg;
import main.utils.Timer;

public class SandwichPacker {

    // Keeps count of how many SandwichPackers have been made so far to assign id
    private static int count = 0;

    // Keeps count of how many Sandwiches have been packed by all SandwichPackers
    private volatile static int totalPacked = 0;

    private int packingRate;
    private String packerId;
    private int sandwichId;
    private int numPacked;
    public Runnable runnable;

    public SandwichPacker(int packingRate) {
        this.packingRate = packingRate;
        this.sandwichId = 0;
        this.numPacked = 0;
        this.packerId = "S" + count;
        count++;

        this.runnable = new Runnable() {

            @Override
            public void run() {

                while (totalPacked++ < SandwichManager.nSandwiches) {

                    // Pack Sandwich (Call Timer to wait)
                    Timer.gowork(packingRate);

                    // Get ingredients
                    Bread bread1 = SandwichManager.breadPool.get();
                    Egg egg = SandwichManager.eggPool.get();
                    Bread bread2 = SandwichManager.breadPool.get();

                    // Increment numPacked by each Sandwich Packer
                    numPacked++;

                    // Log
                    SandwichManager.printer.log(String.format(
                            "%s packs sandwich %d with bread %d from %s and egg %d from %s and bread %d from %s",
                            packerId,
                            sandwichId++, bread2.breadId, bread2.makerId, egg.eggId, egg.makerId, bread1.breadId,
                            bread1.makerId));

                    // Debugging
                    // System.out.println("Packer = " + packerId + " Total = " + totalPacked + " Num Packed = " + numPacked);
                }
            }
        };
    }

    public String getPackerId() {
        return packerId;
    }

    public int getNumPacked() {
        return numPacked;
    }

    public static int totalPacked() {
        return totalPacked;
    }
}
