package hr.fer.bioinf.minimizer.hash;

import hr.fer.bioinf.minimizer.extractor.MinimizerExtractor;
import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.io.*;
import java.util.*;

/**
 * <p>
 * Hash table for finding Minimizers inside sequences in constant time.
 * </p>
 *
 * <p>
 * MinimizerHashTable can be constructed by extracting the minimizers from a group of Sequences in memory.
 * MinimizerHashTable can then be written to a file, and extracted from the same file later in order to save time
 * on extracting minimizers. If the minimizers were extracted directly they will keep the references to Sequence objects
 * from which they were extracted, if they were read from a file, the reference will point to a 'dummy' Sequence object
 * with the appropriate name but no body.
 * Hash table files can be written with the 'saveToFile' method.
 * </p>
 *
 * <p>
 * MinimizerHashTables are saved to files according to the following format:
 * File contains an arbitrary number of key-value combinations in which the key is marked with ':' at the beginning of
 * the line, and the minimizer string following immediately after it.
 * After the key follows an arbitrary number of alternating 'sequence name' and 'position' lines.
 * 'Sequence name' lines start with '>', positions start with '#'.
 * There are no spaced between the line marker (':', '>' or '#') and the content of the line.
 * </p>
 *
 * <p>
 * Example: <br/>
 * :FQEFS <br/>
 * >Host-nuclease inhibitor protein gam OS=Escherichia coli <br/>
 * #35 <br/>
 * :FQEFT <br/>
 * >Uncharacterized protein OS=Escherichia coli <br/>
 * #13 <br/>
 * >Conjugal transfer protein OS=Escherichia coli <br/>
 * #375 <br/>
 * </p>
 */
public class MinimizerHashTable {

    private Map<String, List<Minimizer>> minimizerMap;

    /**
     * Constructs MinimizerHashTable from a given file using the above format.
     * @param hashTableFile File in which the hash table is written.
     * @throws IOException If the given file cannot be read for any reason or if it is in a wrong format.
     */
    public MinimizerHashTable(File hashTableFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(hashTableFile), 65536)) {
            minimizerMap = new HashMap<>(500000);
            Map<String, Sequence> sequenceMap = new HashMap<>();

            String key = null;
            String seqName = null;
            List<Minimizer> minimizerList = null;

            String line = reader.readLine();
            int counter = 0;
            while (line != null && ! line.isEmpty()) {

                switch (line.charAt(0)) {
                    case ':':
                        if (key != null) minimizerMap.put(key, minimizerList);
                        minimizerList = new ArrayList<>();

                        key = line.substring(1);
                        break;
                    case '>':
                        seqName = line.substring(1);
                        break;
                    case '#':
                        int pos = Integer.parseInt(line.substring(1));
                        Sequence seq = sequenceMap.get(seqName);
                        if (seq == null) {
                            seq = new Sequence(seqName, "");
                            sequenceMap.put(seqName, seq);
                        }

                        minimizerList.add(new Minimizer(key, seq, pos));
                        break;

                    default:
                        throw new IOException("Misformatted file");
                }

                line = reader.readLine();
            }

            if (key != null) minimizerMap.put(key, minimizerList);
        }
    }

    /**
     * Construct a MinimizerHashTable from a collection of sequences.
     * @param sequences Collection of sequences from which minimizers will be extracted.
     * @param w Window size of the minimizers.
     * @param k Size of the minimizers strings.
     */
    public MinimizerHashTable(Collection<Sequence> sequences, int w, int k) {
        if (sequences == null || sequences.isEmpty()) {
            throw new IllegalArgumentException("sequences must be a non-null, non-empty collection");
        }

        int sizeSum = sequences
                .stream()
                .mapToInt(seq -> seq.getString().length())
                .sum();

        minimizerMap = new HashMap<>(sizeSum * 2 / (w + 1));

        for (Sequence seq : sequences) {
            List<Minimizer> minimizers = MinimizerExtractor.extract(seq, w, k);

            for (Minimizer minimizer : minimizers) {
                String minimizerStr = minimizer.getString();

                if (! minimizerMap.containsKey(minimizerStr)) {
                    ArrayList<Minimizer> newList = new ArrayList<>();
                    newList.add(minimizer);
                    minimizerMap.put(minimizerStr, newList);

                } else {
                    minimizerMap.get(minimizerStr).add(minimizer);
                }
            }
        }
    }

    /**
     * Writes the hash table to a file in the aforementioned format.
     * @param file File to which the hash table will be written.
     * @throws IOException If the given file cannot be written to for any reason.
     */
    public void saveToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (Map.Entry<String, List<Minimizer>> entry : minimizerMap.entrySet()) {
                writer.write(":");
                writer.write(entry.getKey());
                writer.newLine();

                for (Minimizer minimizer : entry.getValue()) {
                    writer.write(">");
                    writer.write(minimizer.getSeq().getName());
                    writer.newLine();

                    writer.write("#");
                    writer.write(Integer.toString(minimizer.getPos()));
                    writer.newLine();
                }
            }
        }
    }

    /**
     * Get the list of minimizers with the given string.
     * @param minimizerStr Minimizer string.
     * @return List of minimizers with the same string.
     */
    public List<Minimizer> get(String minimizerStr) {
        return minimizerMap.get(minimizerStr);
    }


}
