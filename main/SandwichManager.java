package main;

import main.models.Bread;
import main.models.Egg;
import main.producers.BreadMaker;
import main.producers.EggMaker;
import main.producers.SandwichPacker;
import main.utils.Pool;
import main.utils.Printer;

public class SandwichManager {

    public volatile static Pool<Egg> eggPool;
    public volatile static Pool<Bread> breadPool;
    public static Printer printer = new Printer();
    public static int nSandwiches;
    public static void main(String[] args) {
        
        // Check for valid input
        if (args.length < 9) {
            printer.log("Invalid Input!");
            return;
        }

        // Getting inputs
        nSandwiches = Integer.parseInt(args[0]);
        int breadCapacity = Integer.parseInt(args[1]);
        int eggCapacity = Integer.parseInt(args[2]);
        int nBreadMakers = Integer.parseInt(args[3]);
        int nEggMakers = Integer.parseInt(args[4]);
        int nSandwichPackers = Integer.parseInt(args[5]);
        int breadRate = Integer.parseInt(args[6]);
        int eggRate = Integer.parseInt(args[7]);
        int packingRate = Integer.parseInt(args[8]);

        // Log Parameters
        printer.log(args);

        // Create Egg / Bread Pools
        eggPool = new Pool<>(eggCapacity);
        breadPool = new Pool<>(breadCapacity);

        // Create Egg / Bread / Packer Thread Arrays
        BreadMaker[] breadMakers = new BreadMaker[nBreadMakers];
        Thread[] breadMakerThreads = new Thread[nBreadMakers];

        EggMaker[] eggMakers = new EggMaker[nEggMakers];
        Thread[] eggMakerThreads = new Thread[nEggMakers];

        SandwichPacker[] sandwichPackers = new SandwichPacker[nSandwichPackers];
        Thread[] sandwichPackerThreads = new Thread[nSandwichPackers];

        for (int i = 0; i < nBreadMakers; i++) {
            breadMakers[i] = new BreadMaker(breadRate);
        }

        for (int i = 0; i < nEggMakers; i++) {
            eggMakers[i] = new EggMaker(eggRate);
        }

        for (int i = 0; i < nSandwichPackers; i++) {
            sandwichPackers[i] = new SandwichPacker(packingRate);
        }

        // Run threads
        for (int i = 0; i < nBreadMakers; i++) {
            breadMakerThreads[i] = new Thread(breadMakers[i].runnable);
            breadMakerThreads[i].start();
        }

        for (int i = 0; i < nEggMakers; i++) {
            eggMakerThreads[i] = new Thread(eggMakers[i].runnable);
            eggMakerThreads[i].start();
        }

        for (int i = 0; i < nSandwichPackers; i++) {
            sandwichPackerThreads[i] = new Thread(sandwichPackers[i].runnable);
            sandwichPackerThreads[i].start();
        }

        // Join threads
        for (int i = 0; i < nBreadMakers; i++) {
            try {
                breadMakerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < nEggMakers; i++) {
            try {
                eggMakerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Do nothing and wait till Sandwich Packers finish making all sandwiches then join the sandwich threads
        while (SandwichPacker.totalPacked() < nSandwiches) { }
        
        // Debugging
        // System.out.println("Egg Pool Size = " + eggPool.getSize());
        // System.out.println("Bread Pool Size = " + breadPool.getSize());

        for (int i = 0; i < nSandwichPackers; i++) {
            try {
                sandwichPackerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printer.log(breadMakers, eggMakers, sandwichPackers);
    }

}
