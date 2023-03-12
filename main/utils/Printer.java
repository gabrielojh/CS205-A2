package main.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.producers.BreadMaker;
import main.producers.EggMaker;
import main.producers.SandwichPacker;

public class Printer {

    private static Object lock = new Object();
    
    /**
     * Append string to new line in log file
     * 
     * @param str Text to be logged to log file
     */
    public void log(String str) {
        try {
            // Ensure that only one thread can use the logger at anytime
            synchronized (lock) {
                // Create Directory and File if it doesn't already exist
                Files.createDirectories(Paths.get("logs"));
                File file = new File("logs/log.txt");
                file.createNewFile();
    
                // Initialize FileWriter
                BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true));
    
                // // Debugging
                // System.out.println("Log Output: " + str);
    
                // Append string to file
                bWriter.write(str);
                bWriter.newLine();
                bWriter.flush();
                bWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called at the start of a new program execution to overwrite current log file
     * 
     * @param args Original Input to setup all the parameters
     */
    public void log(String[] args) {
        try {

            // Create Directory and File if it doesn't already exist
            Files.createDirectories(Paths.get("logs"));
            File file = new File("logs/log.txt");
            file.createNewFile();

            // Initialize FileWriter
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));

            // Append parameters to log file
            bWriter.write("sandwiches:" + args[0]);
            bWriter.newLine();
            bWriter.write("bread capacity:" + args[1]);
            bWriter.newLine();
            bWriter.write("egg capacity:" + args[2]);
            bWriter.newLine();
            bWriter.write("bread makers:" + args[3]);
            bWriter.newLine();
            bWriter.write("egg makers:" + args[4]);
            bWriter.newLine();
            bWriter.write("sandwich packers:" + args[5]);
            bWriter.newLine();
            bWriter.write("bread rate:" + args[6]);
            bWriter.newLine();
            bWriter.write("egg rate:" + args[7]);
            bWriter.newLine();
            bWriter.write("packing rate:" + args[8]);
            bWriter.newLine();
            bWriter.newLine();

            bWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Method called after all Sandwich has been packed to log the summary and show how much ingredient each machine
     * have created
     * 
     * @param breadMakers Array of Bread Makers
     * @param eggMakers Array of Egg Makers
     * @param sandwichPackers Array of Sandwich Packers
     */
    public void log(BreadMaker[] breadMakers, EggMaker[] eggMakers, SandwichPacker[] sandwichPackers) {

        try {

            // Create Directory and File if it doesn't already exist
            Files.createDirectories(Paths.get("logs"));
            File file = new File("logs/log.txt");
            file.createNewFile();

            // Initialize FileWriter
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true));

            // Append parameters to log file

            // Summary Headers
            bWriter.newLine();
            bWriter.write("summary:");
            bWriter.newLine();

            // Bread Makers
            for (BreadMaker breadMaker : breadMakers) {
                bWriter.write(String.format("%s made %d", breadMaker.getMakerId(), breadMaker.getBreadsMade()));
                bWriter.newLine();
            }

            // Egg Makers
            for (EggMaker eggMaker : eggMakers) {
                bWriter.write(String.format("%s made %d", eggMaker.getMakerId(), eggMaker.getEggsMade()));
                bWriter.newLine();
            }

            // Sandwich Packer
            for (SandwichPacker sandwichPacker : sandwichPackers) {
                bWriter.write(String.format("%s made %d", sandwichPacker.getPackerId(), sandwichPacker.getNumPacked()));
                bWriter.newLine();
            }

            bWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
