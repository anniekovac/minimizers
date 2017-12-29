package hr.fer.bioinf.minimizer;

import hr.fer.bioinf.minimizer.extractor.MinimizerExtractor;
import hr.fer.bioinf.minimizer.hash.MinimizerHashTable;
import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MinimizerHashtableQuery {
    private final static String USAGE = "USAGE: java MinimizerHashtableQuery <w> <k> <hashtable-path> <string>";

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println(USAGE);
            return;
        }

        int w = 0;
        int k = 0;
        String hashTablePath = null;
        String string = null;

        try {
            w = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
            hashTablePath = args[2];
            string = args[3];

        } catch (NumberFormatException ex) {
            System.out.println(USAGE);
            return;
        }

        MinimizerHashTable hashTable = null;
        try {
            System.out.println("Loading hashtable from file...");
            hashTable = new MinimizerHashTable(new File(hashTablePath));
        } catch (IOException e) {
            System.out.println("Could not read hashtable file " + hashTablePath);
            return;
        }

        List<Minimizer> foundMinimizers = hashTable.get(string);

        if (foundMinimizers == null) {
            System.out.println("No instances of " + string + " found");
        }

        for (Minimizer foundMinimizer : foundMinimizers) {
            System.out.println("Found: " + foundMinimizer);
        }
    }
}
