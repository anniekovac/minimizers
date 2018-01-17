package hr.fer.bioinf.minimizer;

import hr.fer.bioinf.minimizer.hash.MinimizerHashTable;
import hr.fer.bioinf.minimizer.minimizer.Minimizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Demo program which loads a hash table from a file and queries the hash table for the given string.<br/>
 * Usage is: java MinimizerHashtableQuery &lt;w&gt; &lt;k&gt; &lt;hashtable-path&gt; &lt;string&gt; <br/>
 * Where &lt;w&gt; is the window size, &lt;k&gt; is the minimizer size, &lt;hashtable-path&gt; is he path to the
 * hash table file and &lt;string&gt; is the string to query
 */
public class MinimizerHashtableQuery {
    private final static String USAGE = "USAGE: java MinimizerHashtableQuery <hashtable-path> <string>";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(USAGE);
            return;
        }

        String hashTablePath = null;
        String string = null;

        try {
            hashTablePath = args[0];
            string = args[1];

        } catch (NumberFormatException ex) {
            System.out.println(USAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("out.txt"))) {
            MinimizerHashTable hashTable = null;
            try {
                hashTable = new MinimizerHashTable(new File(hashTablePath));
            } catch (IOException e) {
                System.out.println("Could not read hashtable file " + hashTablePath);
                return;
            }

            List<Minimizer> foundMinimizers = hashTable.get(string);

            if (foundMinimizers == null) {
                writer.write("No instances of " + string + " found");
                writer.newLine();
                return;
            }

            for (Minimizer foundMinimizer : foundMinimizers) {
                writer.write("Found: " + foundMinimizer);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
